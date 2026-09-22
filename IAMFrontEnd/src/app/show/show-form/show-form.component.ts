import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  ShowService,
  ShowType,
  CreateShowRequest,
  AvailableShowTimesRequest,
  AvailableShowTimeSlot,
  UpdateShowRequest,
  ShowResponse,
} from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';
import { NavigationService } from '../../core/services/navigation/navigation.service';
import {
  MovieServiceService,
  MovieRegisterResponse,
} from '../../core/services/movie/movie-service.service';
import {
  TheaterService,
  TheaterResponse,
} from '../../core/services/theater/theater.service';
import {
  ScreenService,
  screenResponse,
} from '../../core/services/screen/screen.service';
import { forkJoin } from 'rxjs';

export interface ShowFormGroup {
  theaterId: FormControl<string>;
  movieId: FormControl<string>;
  screenId: FormControl<string>;
  showDate: FormControl<string>;
  showTime: FormControl<string>;
  showType: FormControl<ShowType>;
  basePrice: FormControl<number>;
}

@Component({
  selector: 'app-show-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './show-form.component.html',
  styleUrls: ['./show-form.component.css'],
})
export class ShowFormComponent implements OnInit {
  // Dependencies
  protected readonly showService = inject(ShowService);
  protected readonly movieService = inject(MovieServiceService);
  protected readonly theaterService = inject(TheaterService);
  protected readonly screenService = inject(ScreenService);
  protected readonly toastService = inject(ToastService);
  protected readonly router = inject(Router);
  protected readonly route = inject(ActivatedRoute);
  protected readonly navigationService = inject(NavigationService);

  // State
  protected readonly isEditMode = signal(false);
  protected readonly isSubmitting = signal(false);
  protected readonly showId = signal<string | null>(null);
  protected readonly isLoading = signal(false);
  protected readonly currentStep = signal(1);
  protected readonly selectionVersion = signal(0);

  // Data loading states
  protected readonly isLoadingMovies = signal(false);
  protected readonly isLoadingTheaters = signal(false);
  protected readonly isLoadingScreens = signal(false);
  protected readonly isLoadingAvailableSlots = signal(false);

  // Data
  protected readonly movies = signal<MovieRegisterResponse[]>([]);
  protected readonly theaters = signal<TheaterResponse[]>([]);
  protected readonly allScreens = signal<screenResponse[]>([]);
  protected readonly availableShowTimes = signal<AvailableShowTimeSlot[]>([]);

  protected readonly selectedMovie = computed(() => {
    this.selectionVersion();
    return (
      this.movies().find(
        (movie) => movie.movieId === this.showForm.controls.movieId.value,
      ) ?? null
    );
  });

  protected readonly selectedTheater = computed(() => {
    this.selectionVersion();
    return (
      this.theaters().find(
        (theater) =>
          theater.theaterId === this.showForm.controls.theaterId.value,
      ) ?? null
    );
  });

  protected readonly selectedScreen = computed(() => {
    this.selectionVersion();
    return (
      this.allScreens().find(
        (screen) => screen.screenId === this.showForm.controls.screenId.value,
      ) ?? null
    );
  });

  // Filtered screens based on selected theater
  protected readonly filteredScreens = computed(() => {
    this.selectionVersion();
    const theaterId = this.showForm.get('theaterId')?.value;
    if (!theaterId) return [];
    return this.allScreens().filter((screen) => screen.theaterId === theaterId);
  });

  // Search filters
  protected readonly movieSearch = signal('');
  protected readonly theaterSearch = signal('');

  // Filtered data for search
  protected readonly filteredMovies = computed(() => {
    const search = this.movieSearch().toLowerCase();
    if (!search) return this.movies();
    return this.movies().filter(
      (movie) =>
        movie.title.toLowerCase().includes(search) ||
        movie.genre.toLowerCase().includes(search) ||
        movie.language.toLowerCase().includes(search),
    );
  });

  protected readonly filteredTheaters = computed(() => {
    const search = this.theaterSearch().toLowerCase();
    if (!search) return this.theaters();
    return this.theaters().filter(
      (theater) =>
        theater.name.toLowerCase().includes(search) ||
        theater.city.toLowerCase().includes(search),
    );
  });

  // Enum reference for template
  protected readonly ShowType = ShowType;
  protected readonly showTypes = Object.values(ShowType);

  // Form
  protected readonly showForm = new FormGroup<ShowFormGroup>({
    theaterId: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    movieId: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    screenId: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    showDate: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    showTime: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    showType: new FormControl(ShowType.EVENING, {
      nonNullable: true,
      validators: [Validators.required],
    }),
    basePrice: new FormControl(200, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0)],
    }),
  });

  ngOnInit(): void {
    // Load initial data
    this.loadInitialData();

    // Watch for theater selection changes to reset screen selection
    this.showForm.get('theaterId')?.valueChanges.subscribe(() => {
      this.showForm.patchValue({ screenId: '' });
    });
    this.showForm.valueChanges.subscribe(() => {
      this.selectionVersion.update((version) => version + 1);
    });

    // Check if we're in edit mode
    this.route.params.subscribe((params) => {
      const id = params['id'];
      if (id) {
        this.showId.set(id);
        this.isEditMode.set(true);
        this.loadShow(id);
      } else {
        // Set default date to today
        const today = new Date().toISOString().split('T')[0];
        this.showForm.patchValue({ showDate: today });
      }
    });
  }

  protected selectMovie(movieId: string): void {
    this.showForm.controls.movieId.setValue(movieId);
  }

  protected selectTheater(theaterId: string): void {
    this.showForm.controls.theaterId.setValue(theaterId);
  }

  protected selectScreen(screenId: string): void {
    this.showForm.controls.screenId.setValue(screenId);
  }

  protected openScheduleStep(): void {
    this.currentStep.set(3);

    if (!this.isEditMode()) {
      this.loadAvailableShowTimes();
    }
  }

  protected onShowDateChange(): void {
    this.showForm.controls.showTime.setValue('');
    this.availableShowTimes.set([]);
    this.loadAvailableShowTimes();
  }

  protected selectShowTime(slot: AvailableShowTimeSlot): void {
    this.showForm.controls.showTime.setValue(slot.startTime.substring(11, 16));
  }

  protected getShowTimeDisplay(slot: AvailableShowTimeSlot): string {
    const timeFormat = new Intl.DateTimeFormat(undefined, {
      hour: 'numeric',
      minute: '2-digit',
    });
    return `${timeFormat.format(new Date(slot.startTime))} - ${timeFormat.format(new Date(slot.endTime))}`;
  }

  protected isShowTimeSelected(slot: AvailableShowTimeSlot): boolean {
    return (
      this.showForm.controls.showTime.value === slot.startTime.substring(11, 16)
    );
  }

  protected nextStep(): void {
    const validForStep =
      (this.currentStep() === 1 && this.showForm.controls.movieId.valid) ||
      (this.currentStep() === 2 &&
        this.showForm.controls.theaterId.valid &&
        this.showForm.controls.screenId.valid) ||
      (this.currentStep() === 3 &&
        this.showForm.controls.showDate.valid &&
        this.showForm.controls.showTime.valid);

    if (!validForStep) {
      this.toastService.setToast(
        'Complete this step before continuing',
        'error',
      );
      return;
    }

    const nextStep = Math.min(this.currentStep() + 1, 4);
    this.currentStep.set(nextStep);

    if (nextStep === 3 && !this.isEditMode()) {
      this.loadAvailableShowTimes();
    }
  }

  protected previousStep(): void {
    this.currentStep.update((step) => Math.max(step - 1, 1));
  }

  protected loadAvailableShowTimes(): void {
    const movie = this.selectedMovie();
    const { theaterId, screenId, showDate } = this.showForm.getRawValue();

    if (!movie || !theaterId || !screenId || !showDate) {
      return;
    }

    const request: AvailableShowTimesRequest = {
      theaterId,
      screenId,
      movieRunTime: movie.durationMinutes,
      requestedShowDateTime: `${showDate}T00:00:00`,
    };

    this.isLoadingAvailableSlots.set(true);
    this.showService.getAvailableShowTimes(request).subscribe({
      next: (response) => {
        this.availableShowTimes.set(response.slots);
        this.isLoadingAvailableSlots.set(false);
      },
      error: (err) => {
        this.availableShowTimes.set([]);
        this.isLoadingAvailableSlots.set(false);
        this.toastService.setToast(
          'Failed to load available show times',
          'error',
        );
        console.error('Error loading available show times:', err);
      },
    });
  }

  protected loadInitialData(): void {
    this.isLoadingMovies.set(true);
    this.isLoadingTheaters.set(true);

    // Load movies (first page with large size to get all)
    this.movieService.getMovieList(0, 100).subscribe({
      next: (response) => {
        this.movies.set(response.movieResponseDTO);
        this.isLoadingMovies.set(false);
      },
      error: (err) => {
        this.toastService.setToast('Failed to load movies', 'error');
        this.isLoadingMovies.set(false);
        console.error('Error loading movies:', err);
      },
    });

    // Load theaters (first page with large size to get all)
    this.theaterService.getTheaterList(0, 100).subscribe({
      next: (response) => {
        this.theaters.set(response.theaters);
        this.isLoadingTheaters.set(false);

        // Load screens for all theaters
        this.loadAllScreens(response.theaters);
      },
      error: (err) => {
        this.toastService.setToast('Failed to load theaters', 'error');
        this.isLoadingTheaters.set(false);
        console.error('Error loading theaters:', err);
      },
    });
  }

  protected loadAllScreens(theaters: TheaterResponse[]): void {
    if (theaters.length === 0) return;

    this.isLoadingScreens.set(true);
    const screenRequests = theaters.map((theater) =>
      this.screenService.listScreensForTheater(theater.theaterId),
    );

    forkJoin(screenRequests).subscribe({
      next: (responses) => {
        const allScreens: screenResponse[] = [];
        responses.forEach((response) => {
          if (response.body) {
            allScreens.push(...response.body);
          }
        });
        this.allScreens.set(allScreens);
        this.isLoadingScreens.set(false);
      },
      error: (err) => {
        this.toastService.setToast('Failed to load screens', 'error');
        this.isLoadingScreens.set(false);
        console.error('Error loading screens:', err);
      },
    });
  }

  protected loadShow(showId: string): void {
    this.isLoading.set(true);
    this.showService.getShowById(showId).subscribe({
      next: (show: ShowResponse) => {
        // Parse the showDateTime to extract date and time
        const startDateTime = new Date(show.showDateTime);
        const showDate = startDateTime.toISOString().split('T')[0];
        const showTime = startDateTime
          .toTimeString()
          .split(' ')[0]
          .substring(0, 5);

        // Find the theater ID from the screen
        const screen = this.allScreens().find(
          (s) => s.screenId === show.screenId,
        );
        const theaterId = screen?.theaterId || '';

        this.showForm.patchValue({
          theaterId: theaterId,
          movieId: show.movieId,
          screenId: show.screenId,
          showDate: showDate,
          showTime: showTime,
          showType: show.showType as ShowType,
          basePrice: show.basePrice,
        });
        this.isLoading.set(false);
      },
      error: (err) => {
        this.toastService.setToast('Failed to load show', 'error');
        this.isLoading.set(false);
        console.error('Error loading show:', err);
      },
    });
  }

  protected onSubmit(): void {
    if (this.showForm.invalid || this.isSubmitting()) {
      this.showForm.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    const formValue = this.showForm.getRawValue();

    // Combine date and time into ISO string
    const showDateTime = `${formValue.showDate}T${formValue.showTime}:00`;

    if (this.isEditMode()) {
      // Update existing show
      const updateRequest: UpdateShowRequest = {
        showId: this.showId()!,
        showDateTime: showDateTime,
        showType: formValue.showType,
        basePrice: formValue.basePrice,
      };

      this.showService.updateShow(this.showId()!, updateRequest).subscribe({
        next: (response) => {
          this.toastService.setToast('Show updated successfully', 'success');
          this.isSubmitting.set(false);
          this.router.navigate(['/base/show']);
        },
        error: (err) => {
          this.handleError(err);
          this.isSubmitting.set(false);
        },
      });
    } else {
      // Create new show
      const createRequest: CreateShowRequest = {
        theaterId: formValue.theaterId,
        movieId: formValue.movieId,
        screenId: formValue.screenId,
        showDateTime: showDateTime,
        showType: formValue.showType,
        basePrice: formValue.basePrice,
      };

      this.showService.createShow(createRequest).subscribe({
        next: (response) => {
          this.toastService.setToast('Show created successfully', 'success');
          this.isSubmitting.set(false);
          this.router.navigate(['/base/show']);
        },
        error: (err) => {
          this.handleError(err);
          this.isSubmitting.set(false);
        },
      });
    }
  }

  protected handleError(error: any): void {
    if (error.status === 400) {
      this.toastService.setToast(
        'Invalid show data. Please check your inputs.',
        'error',
      );
    } else if (error.status === 404) {
      this.toastService.setToast('Show not found', 'error');
    } else if (error.status === 409) {
      this.toastService.setToast(
        'A show already exists for this screen at this time',
        'error',
      );
    } else {
      this.toastService.setToast(
        'An error occurred. Please try again.',
        'error',
      );
    }
    console.error('Error saving show:', error);
  }

  protected cancel(): void {
    this.navigationService.goBack();
  }

  protected getShowTypeDisplay(type: ShowType): string {
    const displays: Record<ShowType, string> = {
      [ShowType.MORNING]: 'Morning Show (6:00 AM - 12:00 PM)',
      [ShowType.MATINEE]: 'Matinee (12:00 PM - 3:00 PM)',
      [ShowType.EVENING]: 'Evening Show (3:00 PM - 6:00 PM)',
      [ShowType.NIGHT]: 'Night Show (6:00 PM - 12:00 AM)',
    };
    return displays[type];
  }

  protected hasError(controlName: keyof ShowFormGroup): boolean {
    const control = this.showForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  protected getErrorMessage(controlName: keyof ShowFormGroup): string {
    const control = this.showForm.get(controlName);
    if (!control || !control.errors) return '';

    if (control.errors['required']) {
      return 'This field is required';
    }
    if (control.errors['min']) {
      return `Minimum value is ${control.errors['min'].min}`;
    }
    return 'Invalid value';
  }

  // Helper methods for template
  protected getMovieDisplay(movie: MovieRegisterResponse): string {
    return `${movie.title} (${movie.language}) - ${movie.genre}`;
  }

  protected getTheaterDisplay(theater: TheaterResponse): string {
    return `${theater.name} - ${theater.city}`;
  }

  protected getScreenDisplay(screen: screenResponse): string {
    return `${screen.screenName} (Screen ${screen.screenNumber}) - ${screen.totalSeats} seats - ${screen.screenType}`;
  }

  protected onMovieSearchChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.movieSearch.set(value);
  }

  protected onTheaterSearchChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.theaterSearch.set(value);
  }
}
