import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ShowService, ShowResponse, ShowType } from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';
import {
  MovieRegisterResponse,
  MovieServiceService,
} from '../../core/services/movie/movie-service.service';
import {
  TheaterResponse,
  TheaterService,
} from '../../core/services/theater/theater.service';

@Component({
  selector: 'app-show-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './show-list.component.html',
  styleUrls: ['./show-list.component.css'],
})
export class ShowListComponent {
  // Dependencies
  protected readonly showService = inject(ShowService);
  protected readonly toastService = inject(ToastService);
  protected readonly route = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  protected readonly movieService = inject(MovieServiceService);
  protected readonly theaterService = inject(TheaterService);

  // Filter options
  protected readonly filterType = signal<'movie' | 'theater' | null>('theater');
  protected readonly movieId = signal<string | null>(null);
  protected readonly theaterId = signal<string | null>(null);
  protected readonly selectedDate = signal<string | null>(null);
  protected readonly selectedCity = signal<string | null>(null);
  protected readonly movies = signal<MovieRegisterResponse[]>([]);
  protected readonly theaters = signal<TheaterResponse[]>([]);
  protected readonly isLoadingOptions = signal(false);

  // Pagination (handled client-side since API doesn't support it)
  protected readonly totalCount = signal(0);
  protected readonly page = signal(0);
  protected readonly pageSize = signal(5);
  protected readonly hasNext = signal(false);
  protected readonly totalPages = signal(0);

  // Data and state
  protected readonly shows = signal<ShowResponse[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly image: { [key: string]: string } = {};

  // Helper getters
  protected readonly ShowType = ShowType;

  ngOnInit(): void {
    this.loadFilterOptions();

    // Get filter parameters from route
    this.route.queryParams.subscribe((params) => {
      this.filterType.set('theater');
      this.movieId.set(null);
      this.theaterId.set(null);

      if (params['movieId']) {
        this.filterType.set('movie');
        this.movieId.set(params['movieId']);
      } else if (params['theaterId']) {
        this.filterType.set('theater');
        this.theaterId.set(params['theaterId']);
      }

      this.selectedDate.set(params['date'] || this.getTodayDate());
      if (params['city']) {
        this.selectedCity.set(params['city']);
      }

      this.loadShows();
    });
  }

  protected loadFilterOptions(): void {
    this.isLoadingOptions.set(true);

    this.movieService.getMovieList(0, 100).subscribe({
      next: (response) => this.movies.set(response.movieResponseDTO),
      error: (err) => {
        this.toastService.setToast('Failed to load movie options', 'error');
        console.error('Error loading movie options:', err);
      },
    });

    this.theaterService.getTheaterList(0, 100).subscribe({
      next: (response) => {
        this.theaters.set(response.theaters);
        this.isLoadingOptions.set(false);
      },
      error: (err) => {
        this.toastService.setToast('Failed to load theater options', 'error');
        this.isLoadingOptions.set(false);
        console.error('Error loading theater options:', err);
      },
    });
  }

  protected loadShows(): void {
    this.isLoading.set(true);
    this.error.set(null);
    const filterType = this.filterType();
    const movieId = this.movieId();
    const theaterId = this.theaterId();
    const date = this.selectedDate();
    const city = this.selectedCity();

    if (filterType === 'movie' && movieId) {
      this.showService
        .getShowsByMovie(movieId, date || undefined, city || undefined)
        .subscribe({
          next: (response) => {
            this.shows.set(response.shows);
            this.totalCount.set(response.totalCount);
            this.isLoading.set(false);
          },
          error: (err) => {
            this.error.set('Failed to load shows');
            this.toastService.setToast('Failed to load shows', 'error');
            this.isLoading.set(false);
            console.error('Error loading shows:', err);
          },
        });
    } else if (filterType === 'theater' && theaterId) {
      this.showService
        .getShowsByTheater(theaterId, date || undefined)
        .subscribe({
          next: (response) => {
            this.shows.set(response.shows);
            this.totalCount.set(response.totalCount);
            this.isLoading.set(false);
            response.shows.forEach((show) => {
              this.movieService.getImageById(show.movie.imageId).subscribe({
                next: (imageUrl) => {
                  const url = URL.createObjectURL(imageUrl);
                  this.image[show.movie.movieId] = url;
                },
                error: (err) => {
                  console.error('Error loading movie image:', err);
                },
              });
            });
          },
          error: (err) => {
            this.error.set('Failed to load shows');
            this.toastService.setToast('Failed to load shows', 'error');
            this.isLoading.set(false);
            console.error('Error loading shows:', err);
          },
        });
    } else {
      // No valid filter, show empty state
      this.showService.getShows(this.page(), this.pageSize()).subscribe({
        next: (response) => {
          this.shows.set(response.shows);
          this.totalCount.set(response.totalCount);
          this.totalPages.set(response.totalPages);
          this.hasNext.set(response.hasNext);
          this.isLoading.set(false);
          response.shows.forEach((show) => {
            this.movieService.getImageById(show.movie.imageId).subscribe({
              next: (imageUrl) => {
                const url = URL.createObjectURL(imageUrl);
                this.image[show.movie.movieId] = url;
              },
              error: (err) => {
                console.error('Error loading movie image:', err);
              },
            });
          });
        },
        error: (err) => {
          this.isLoading.set(false);
          this.totalPages.set(0);
          this.toastService.setToast('Failed to load shows', 'error');
          console.error('Error loading shows:', err);
        },
      });

      this.isLoading.set(false);
      this.totalPages.set(0);
    }
  }

  protected prevPage(): void {
    if (this.page() <= 0 || this.isLoading()) return;
    this.page.set(this.page() - 1);
    this.loadShows();
  }

  protected nextPage(): void {
    if (!this.hasNext() || this.isLoading()) return;
    this.page.set(this.page() + 1);
    this.loadShows();
  }

  protected setPageSize(size: number): void {
    if (size === this.pageSize() || this.isLoading()) return;
    this.pageSize.set(size);
    this.page.set(0);
    this.loadShows();
  }

  protected applyFilters(): void {
    this.updateQueryParams();
  }

  protected clearFilters(): void {
    this.filterType.set(null);
    this.movieId.set(null);
    this.theaterId.set(null);
    this.selectedDate.set(this.getTodayDate());
    this.selectedCity.set(null);
    this.updateQueryParams();
  }

  protected selectFilterType(value: string): void {
    const filterType = value === 'movie' || value === 'theater' ? value : null;
    this.filterType.set(filterType);
    this.movieId.set(null);
    this.theaterId.set(null);
    this.selectedCity.set(null);
  }

  protected selectMovie(movieId: string): void {
    this.movieId.set(movieId || null);
  }

  protected selectTheater(theaterId: string): void {
    this.theaterId.set(theaterId || null);
  }

  protected updateQueryParams(): void {
    const queryParams: {
      movieId?: string | null;
      theaterId?: string | null;
      date?: string | null;
      city?: string | null;
    } = {
      movieId: null,
      theaterId: null,
      date: null,
      city: null,
    };

    if (this.filterType() === 'movie' && this.movieId()) {
      queryParams['movieId'] = this.movieId();
    } else if (this.filterType() === 'theater' && this.theaterId()) {
      queryParams['theaterId'] = this.theaterId();
    }

    if (this.selectedDate()) {
      queryParams['date'] = this.selectedDate();
    }
    if (this.selectedCity()) {
      queryParams['city'] = this.selectedCity();
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }

  // Pagination removed since API doesn't support it

  protected formatShowTime(showDateTime: string): string {
    const date = new Date(showDateTime);

    if (Number.isNaN(date.getTime())) {
      return 'Show time unavailable';
    }

    return new Intl.DateTimeFormat('en-IN', {
      weekday: 'short',
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true,
    }).format(date);
  }
  protected getShowTypeLabel(type: ShowType): string {
    const labels: Record<ShowType, string> = {
      [ShowType.MORNING]: '🌅 Morning Show',
      [ShowType.MATINEE]: '🌤️ Matinee',
      [ShowType.EVENING]: '🌆 Evening Show',
      [ShowType.NIGHT]: '🌙 Night Show',
    };
    return labels[type];
  }

  protected viewSeatAvailability(showId: string): void {
    this.router.navigate(['/base/show', showId, 'seats']);
  }

  protected editShow(showId: string): void {
    this.router.navigate(['/base/show', showId, 'edit']);
  }

  protected deleteShow(showId: string): void {
    // Delete functionality not implemented in API yet
    this.toastService.setToast('Delete feature coming soon', 'error');
  }

  protected getTodayDate(): string {
    return new Date().toISOString().split('T')[0];
  }
}
