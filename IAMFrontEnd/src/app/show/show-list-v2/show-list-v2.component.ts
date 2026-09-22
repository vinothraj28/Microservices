import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { SearchService } from '../../core/services/search/search.service';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Subject, forkJoin, of, timer } from 'rxjs';

import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  map,
  switchMap,
  takeUntil,
  tap,
} from 'rxjs/operators';

import { ActivatedRoute, Router } from '@angular/router';

import { ShowService } from '../../core/services/show/show.service';
import { ToastService } from '../../core/services/toast/toast.service';
import { MovieServiceService } from '../../core/services/movie/movie-service.service';
import { TheaterService } from '../../core/services/theater/theater.service';

import {
  SearchSuggestion,
  SearchResult,
} from '../../core/services/search/search.model';

import { ShowType } from '../../core/services/show/show.models';

interface DateOption {
  value: string;
  label: string;
  shortDate: string;
}

@Component({
  selector: 'app-show-list',
  standalone: true,

  imports: [CommonModule, FormsModule, ReactiveFormsModule],

  templateUrl: './show-list-v2.component.html',
  styleUrls: ['./show-list-v2.component.css'],
})
export class ShowListComponentV2 implements OnInit, OnDestroy {
  private readonly showService = inject(ShowService);
  private readonly toastService = inject(ToastService);
  private readonly movieService = inject(MovieServiceService);
  private readonly theaterService = inject(TheaterService);
  private readonly searchService = inject(SearchService);

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  private readonly destroy$ = new Subject<void>();

  private readonly cityChanges$ = new Subject<{
    value: string;
    immediate: boolean;
  }>();

  // =========================================================
  // SEARCH
  // =========================================================

  readonly searchControl = new FormControl('', {
    nonNullable: true,
  });

  searchQuery = '';

  searchSuggestions: SearchSuggestion[] = [];

  isSearching = false;

  showSearchSuggestions = false;

  selectedSearchResult: SearchSuggestion | null = null;

  // =========================================================
  // FILTERS
  // =========================================================

  selectedDate = signal(this.getTodayDate());

  selectedCity = '';

  selectedTime: ShowType | '' = '';

  readonly showTypes = Object.values(ShowType);

  selectedLanguage = '';

  selectedGenre = '';

  languages: string[] = [
    // Populate from backend/config later
    'English',
    'Dutch',
    'French',
    'German',
  ];

  genres: string[] = [
    // Populate from backend/config later
    'Action',
    'Comedy',
    'Drama',
    'Horror',
    'Sci-Fi',
    'Thriller',
  ];

  // =========================================================
  // RESULTS
  // =========================================================

  shows = signal<any[]>([]);

  isLoading = signal(false);

  error = signal<string | null>(null);

  totalCount = signal(0);

  page = signal(0);

  pageSize = 12;

  totalPages = signal(0);

  hasNext = signal(false);

  // =========================================================
  // IMAGES
  // =========================================================

  image: Record<string | number, string> = {};

  // =========================================================
  // ADMIN / USER MODE
  // =========================================================

  canManageShows = false;

  // =========================================================
  // DATE OPTIONS
  // =========================================================

  dateOptions: DateOption[] = [];

  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.buildDateOptions();

    this.setupSearch();

    this.setupCityFilter();

    this.readQueryParams();

    this.loadShows();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // =========================================================
  // SEARCH SETUP
  // =========================================================

  private setupSearch(): void {
    this.searchControl.valueChanges
      .pipe(
        tap((value) => {
          this.searchQuery = value.trim();
          console.log(this.searchQuery);
          this.showSearchSuggestions = this.searchQuery.length > 0;

          if (this.searchQuery.length < 2) {
            this.searchSuggestions = [];
            this.isSearching = false;
          }
        }),

        debounceTime(250),

        distinctUntilChanged(),

        switchMap((query) => {
          const value = query.trim();

          if (value.length < 2) {
            return of([]);
          }

          this.isSearching = true;

          return this.searchSuggestionsFromBackend(value).pipe(
            catchError(() => {
              return of([]);
            }),
          );
        }),

        takeUntil(this.destroy$),
      )
      .subscribe((results) => {
        console.log(results);
        this.searchSuggestions = results;

        this.isSearching = false;
      });
  }

  private searchSuggestionsFromBackend(query: string) {
    const normalizedQuery = query.trim().toLocaleLowerCase();

    return this.searchService.searchSuggestions(normalizedQuery).pipe(
      map((suggestions) =>
        suggestions.map(
          (suggestion): SearchSuggestion => ({
            id: suggestion.id,
            type: suggestion.type,
            title: suggestion.title,
            subtitle: suggestion.subtitle,
          }),
        ),
      ),
      catchError((error) => {
        console.error(error);
        return of<SearchSuggestion[]>([]);
      }),
    );
  }

  private setupCityFilter(): void {
    this.cityChanges$
      .pipe(
        switchMap(({ value, immediate }) =>
          immediate ? of(value) : timer(350).pipe(map(() => value)),
        ),
        distinctUntilChanged(),
        takeUntil(this.destroy$),
      )
      .subscribe((city) => {
        this.selectedCity = city;
        this.applyFilters();
      });
  }

  // =========================================================
  // SEARCH SELECTION
  // =========================================================

  selectSearchResult(result: SearchSuggestion): void {
    this.selectedSearchResult = result;

    this.searchControl.setValue(result.title, {
      emitEvent: false,
    });

    this.searchQuery = result.title;

    this.showSearchSuggestions = false;

    this.page.set(0);

    this.updateQueryParams();

    this.loadShows();
  }

  clearSearch(): void {
    this.selectedSearchResult = null;

    this.searchSuggestions = [];

    this.searchQuery = '';

    this.searchControl.setValue('', {
      emitEvent: false,
    });

    this.showSearchSuggestions = false;

    this.page.set(0);

    this.updateQueryParams();

    this.loadShows();
  }

  // =========================================================
  // FILTERS
  // =========================================================

  selectDate(date: string): void {
    this.selectedDate.set(date);

    this.page.set(0);

    this.updateQueryParams();

    this.loadShows();
  }

  applyFilters(): void {
    this.page.set(0);
    this.updateQueryParams();
    this.loadShows();
  }

  onCityChange(value: string, immediate = false): void {
    this.cityChanges$.next({ value: value.trim(), immediate });
  }

  clearAllFilters(): void {
    this.selectedSearchResult = null;

    this.searchQuery = '';

    this.searchSuggestions = [];

    this.searchControl.setValue('', {
      emitEvent: false,
    });

    this.selectedCity = '';

    this.selectedTime = '';

    this.selectedLanguage = '';

    this.selectedGenre = '';

    this.selectedDate.set(this.getTodayDate());

    this.page.set(0);

    this.updateQueryParams();

    this.loadShows();
  }

  // =========================================================
  // LOAD SHOWS
  // =========================================================

  loadShows(): void {
    this.isLoading.set(true);

    this.error.set(null);
    if (this.hasSearchCriteria) {
      this.loadFilteredShows();
      return;
    }

    this.showService.getShows(this.page(), this.pageSize).subscribe({
      next: (response) => {
        this.shows.set(response.shows ?? []);

        this.totalCount.set(response.totalCount ?? this.shows().length);

        this.totalPages.set(
          response.totalPages ?? Math.ceil(this.totalCount() / this.pageSize),
        );

        this.hasNext.set(
          response.hasNext !== undefined
            ? response.hasNext
            : this.page() + 1 < this.totalPages(),
        );

        this.loadImages();

        this.isLoading.set(false);
      },

      error: (err) => {
        console.error('Failed to load shows', err);

        this.error.set('Unable to load shows right now.');

        this.isLoading.set(false);
      },
    });
  }

  private loadFilteredShows(): void {
    const selection = this.selectedSearchResult;

    this.searchService
      .searchShows({
        movieId: selection?.type === 'MOVIE' ? selection.id : undefined,
        theaterId: selection?.type === 'THEATER' ? selection.id : undefined,
        showType: this.selectedTime || undefined,
        date: this.selectedDate() || undefined,
        city: this.selectedCity || undefined,
        language: this.selectedLanguage || undefined,
        genre: this.selectedGenre || undefined,
        page: this.page(),
        size: this.pageSize,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          const shows = response.shows ?? [];

          this.shows.set(shows);
          this.totalCount.set(response.totalCount ?? shows.length);
          this.totalPages.set(
            response.totalPages ?? Math.ceil(this.totalCount() / this.pageSize),
          );
          this.hasNext.set(
            response.hasNext ?? this.page() + 1 < this.totalPages(),
          );

          this.loadImages();
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Failed to search shows', err);
          this.error.set('Unable to load shows right now.');
          this.isLoading.set(false);
        },
      });
  }

  // =========================================================
  // IMAGES
  // =========================================================

  private loadImages(): void {
    /*
     * Keep your existing image-loading implementation here.
     *
     * The important part is that images should be loaded based
     * on the returned shows, not by loading every movie.
     */

    for (const show of this.shows()) {
      if (show.movie?.imageId && !this.image[show.movieId]) {
        this.movieService.getImageById(show.movie.imageId).subscribe({
          next: (imageBlob: Blob) => {
            this.image[show.movieId] = URL.createObjectURL(imageBlob);
          },

          error: () => {
            // Keep fallback poster.
          },
        });
      }
    }
  }

  // =========================================================
  // PAGINATION
  // =========================================================

  prevPage(): void {
    if (this.page() === 0) {
      return;
    }

    this.page.update((current) => current - 1);

    this.loadShows();
  }

  nextPage(): void {
    if (!this.hasNext()) {
      return;
    }

    this.page.update((current) => current + 1);

    this.loadShows();
  }

  setPageSize(size: number): void {
    this.pageSize = Number(size);

    this.page.set(0);

    this.loadShows();
  }

  // =========================================================
  // SHOW ACTIONS
  // =========================================================

  selectSeats(show: any): void {
    this.router.navigate(['/base/show', show.id ?? show.showId, 'seats']);
  }

  viewBookings(show: any): void {
    this.router.navigate(['/base/show', show.id ?? show.showId, 'bookings']);
  }

  editShow(show: any): void {
    this.router.navigate(['/base/show', show.id ?? show.showId, 'edit']);
  }

  createShow(): void {
    this.router.navigate(['/base/show/create']);
  }

  // =========================================================
  // QUERY PARAMS
  // =========================================================

  private readQueryParams(): void {
    this.route.queryParams
      .pipe(takeUntil(this.destroy$))
      .subscribe((params) => {
        const date = params['date'];

        if (date) {
          this.selectedDate.set(date);
        }
        this.selectedCity = params['city'] ?? '';
        this.selectedTime = params['time'] ?? '';
        this.selectedLanguage = params['language'] ?? '';
        this.selectedGenre = params['genre'] ?? '';
      });
  }

  private updateQueryParams(): void {
    const queryParams: Record<string, string | null> = {
      date: this.selectedDate() || null,

      city: this.selectedCity || null,

      time: this.selectedTime || null,

      language: this.selectedLanguage || null,

      genre: this.selectedGenre || null,

      q: this.selectedSearchResult?.title || null,

      movieId:
        this.selectedSearchResult?.type === 'MOVIE'
          ? String(this.selectedSearchResult.id)
          : null,

      theaterId:
        this.selectedSearchResult?.type === 'THEATER'
          ? String(this.selectedSearchResult.id)
          : null,
    };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge',
    });
  }

  // =========================================================
  // HELPERS
  // =========================================================

  get hasActiveFilters(): boolean {
    return !!(
      this.selectedSearchResult ||
      this.selectedCity ||
      this.selectedTime ||
      this.selectedLanguage ||
      this.selectedGenre
    );
  }

  private get hasSearchCriteria(): boolean {
    return !!(
      this.selectedSearchResult ||
      this.selectedCity ||
      this.selectedTime ||
      this.selectedLanguage ||
      this.selectedGenre ||
      this.selectedDate()
    );
  }

  getSeatStatus(show: any): string {
    if (show.availableSeats === 0 || show.status === 'SOLD_OUT') {
      return 'SOLD OUT';
    }

    const totalSeats =
      show.screen?.totalSeats || show.totalSeats || show.capacity || 0;

    if (totalSeats > 0 && show.availableSeats / totalSeats <= 0.2) {
      return 'FILLING FAST';
    }

    return 'AVAILABLE';
  }

  getShowTypeLabel(show: any): string {
    return show.showType || show.type || '2D';
  }

  formatShowTime(value: string): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);

    return date.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  formatDateForDisplay(value: string): string {
    if (!value) {
      return '';
    }

    return new Date(value).toLocaleDateString([], {
      weekday: 'short',
      day: 'numeric',
      month: 'short',
    });
  }

  private buildDateOptions(): void {
    const options: DateOption[] = [];

    const today = new Date();

    for (let i = 0; i < 7; i++) {
      const date = new Date(today);

      date.setDate(today.getDate() + i);

      const value = this.toDateString(date);

      options.push({
        value,

        label:
          i === 0
            ? 'Today'
            : i === 1
              ? 'Tomorrow'
              : date.toLocaleDateString([], {
                  weekday: 'short',
                }),

        shortDate: date.toLocaleDateString([], {
          day: 'numeric',
          month: 'short',
        }),
      });
    }

    this.dateOptions = options;
  }

  private toDateString(date: Date): string {
    return [
      date.getFullYear(),
      String(date.getMonth() + 1).padStart(2, '0'),
      String(date.getDate()).padStart(2, '0'),
    ].join('-');
  }

  getTodayDate(): string {
    return this.toDateString(new Date());
  }

  trackByShowId(index: number, show: any): number | string {
    return show.id ?? show.showId ?? index;
  }
}
