import {
  ChangeDetectionStrategy,
  inject,
  signal,
  Input,
  Component,
} from '@angular/core';
import type { OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { firstValueFrom, map, startWith } from 'rxjs';
import { MovieServiceService } from '../../core/services/movie/movie-service.service';
import type {
  MovieRegisterRequest,
  MovieUpdateRequest,
} from '../../core/services/movie/movie-service.service';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MovieDetailsComponent } from '../movie-details/movie-details.component';
import { MovieCreditsComponent } from '../movie-credits/movie-credits.component';
import { MovieMediaComponent } from '../movie-media/movie-media.component';
import { MovieReviewComponent } from '../movie-review/movie-review.component';
import { UpdatedDetailsComponent } from '../updated-details/updated-details.component';
import { toSignal } from '@angular/core/rxjs-interop';
import { ToastService } from '../../core/services/toast/toast.service';

export interface MovieDetailsForm {
  title: FormControl<string>;
  description: FormControl<string>;
  durationMinutes: FormControl<number | null>;
  genre: FormControl<string>;
  language: FormControl<string>;
  releaseDate: FormControl<string>;
}

export interface MovieMediaForm {
  poster: FormControl<File | null>;
  posterUrl: FormControl<string>;
  trailerUrl: FormControl<string>;
}

export interface MovieCreditsForm {
  cast: FormArray<FormControl<string>>;
  crew: FormArray<FormControl<string>>;
}
export interface MovieReviewForm {
  rating: FormControl<string>;
}

export interface MovieForm {
  details: FormGroup<MovieDetailsForm>;
  media: FormGroup<MovieMediaForm>;
  credits: FormGroup<MovieCreditsForm>;
  review: FormGroup<MovieReviewForm>;
}

export type MovieValue = ReturnType<FormGroup<MovieForm>['getRawValue']>;

@Component({
  selector: 'app-movie-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatStepperModule,
    MatButtonModule,
    MatIconModule,
    MovieDetailsComponent,
    MovieReviewComponent,
    MovieMediaComponent,
    MovieCreditsComponent,
    UpdatedDetailsComponent,
  ],
  templateUrl: './movie-form.component.html',
  styleUrls: ['./movie-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MovieFormComponent implements OnInit {
  //Handle form submission state
  protected readonly isSubmitting = signal(false);
  protected readonly submitSuccess = signal(false);
  protected readonly submitError = signal<string | null>(null);

  // Ratings for the movie review
  protected readonly ratings: string[] = ['U', 'UA', 'A', 'R'];

  // Inject the MovieServiceService and ActivatedRoute to handle movie-related operations
  protected readonly movieService = inject(MovieServiceService);
  protected readonly route = inject(ActivatedRoute);

  // Signals to manage poster upload state and file selection
  protected readonly posterPreviewUrl = signal<string | null>(null);
  protected readonly isPosterUploading = signal(false);
  protected readonly file = signal<string | null>(null);

  // Signals to manage form state and mode (edit or add)
  protected readonly movieId = signal<string | null>(null);
  protected readonly isEditMode = signal<boolean>(false);
  protected readonly isFormLoading = signal<boolean>(false);

  private toastService = inject(ToastService);
  private router = inject(Router);

  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('movieId');
    if (id) {
      this.movieId.set(id);
      this.isEditMode.set(true);
      await this.loadMovieForEdit(id);
    }
  }

  protected readonly form = new FormGroup<MovieForm>({
    details: new FormGroup<MovieDetailsForm>({
      title: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      description: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      durationMinutes: new FormControl<number | null>(null, {
        validators: [Validators.required, Validators.min(1)],
      }),
      genre: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      language: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      releaseDate: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    }),

    media: new FormGroup<MovieMediaForm>({
      poster: new FormControl<File | null>(null),
      posterUrl: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.pattern(/^https?:\/\/.+/i),
        ],
      }),
      trailerUrl: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.pattern(/^https?:\/\/.+/i),
        ],
      }),
    }),

    credits: new FormGroup<MovieCreditsForm>({
      cast: new FormArray([
        new FormControl('', {
          nonNullable: true,
        }),
      ]),
      crew: new FormArray([
        new FormControl('', {
          nonNullable: true,
        }),
      ]),
    }),

    review: new FormGroup<MovieReviewForm>({
      rating: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    }),
  });

  protected get detailsGroup() {
    return this.form.controls.details;
  }

  protected get mediaGroup() {
    return this.form.controls.media;
  }

  protected get creditsGroup() {
    return this.form.controls.credits;
  }

  protected get reviewGroup() {
    return this.form.controls.review;
  }

  protected onSubmitError(error: string | null): void {
    this.submitError.set(error);
  }

  protected onPosterUploading(isUploading: boolean): void {
    this.isPosterUploading.set(isUploading);
  }

  nextStep(stepper: MatStepper, formGroup: FormGroup): void {
    formGroup.markAllAsTouched();
    if (formGroup.invalid) {
      return;
    }
    stepper.next();
  }

  readonly movie = toSignal(
    this.form.valueChanges.pipe(
      startWith(null),
      map(() => this.form.getRawValue()),
    ),
    {
      initialValue: this.form.getRawValue(),
    },
  );

  private async loadMovieForEdit(movieId: string): Promise<void> {
    this.isFormLoading.set(true);
    this.submitError.set(null);

    try {
      const movieData = await firstValueFrom(
        this.movieService.getMovieById(movieId),
      );

      const imageResponse = await firstValueFrom(
        this.movieService.getImageById(movieData?.imageId),
      );

      const posterFile = new File([imageResponse], 'poster.jpg', {
        type: imageResponse.type,
      });

      this.file.set(URL.createObjectURL(imageResponse));

      // Populate the form with the movie data
      this.form.patchValue({
        details: {
          title: movieData.title,
          description: movieData.description,
          durationMinutes: movieData.durationMinutes,
          genre: movieData.genre,
          language: movieData.language,
          releaseDate: movieData.releaseDate,
        },
        media: {
          // Poster file will be handled separately
          poster: posterFile,
          posterUrl: movieData.posterUrl,
          trailerUrl: movieData.trailerUrl,
        },
        credits: {
          cast: movieData.cast,
          crew: movieData.crew,
        },
        review: {
          rating: movieData.rating,
        },
      });
    } catch (error) {
      console.error('Error loading movie for edit:', error);
      this.submitError.set('Unable to load movie details. Please try again.');
    } finally {
      this.isFormLoading.set(false);
    }
  }

  protected async submit(): Promise<void> {
    this.submitSuccess.set(false);
    this.submitError.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    try {
      const posterFile = this.mediaGroup.controls.poster.value ?? undefined;

      if (this.isEditMode()) {
        const requestBody = this.buildUpdateRequest();
        await firstValueFrom(
          this.movieService.updateMovie(requestBody, posterFile),
        );
      } else {
        const requestBody = this.buildRegisterRequest();
        await firstValueFrom(
          this.movieService.registerMovie(requestBody, posterFile),
        );
      }

      this.submitSuccess.set(true);
      this.form.markAsPristine();
      this.form.reset();
      this.toastService.setToast(
        this.isEditMode()
          ? 'Movie details updated successfully!'
          : 'Movie registered successfully!',
        'success',
      );
      this.router.navigate(['base', 'movie']);
    } catch (error) {
      console.error('Error registering movie:', error);
      this.submitError.set('Unable to save movie details. Please try again.');
      this.toastService.setToast(
        this.isEditMode()
          ? 'Failed to update movie details.'
          : 'Failed to register movie.',
        'error',
      );
    } finally {
      this.isSubmitting.set(false);
    }
  }

  private buildUpdateRequest(): MovieUpdateRequest {
    return {
      movieId: this.movieId()!,
      ...this.buildRegisterRequest(),
    };
  }

  private buildRegisterRequest(): MovieRegisterRequest {
    const details = this.detailsGroup.getRawValue();
    const media = this.mediaGroup.getRawValue();
    const review = this.reviewGroup.getRawValue();
    const credits = this.creditsGroup.getRawValue();

    const cast = credits.cast.map((name) => name.trim()).filter(Boolean);

    const crew = credits.crew.map((name) => name.trim()).filter(Boolean);

    if (!cast.length || !crew.length) {
      throw new Error('Cast and crew are required.');
    }

    return {
      title: details.title,
      description: details.description,
      language: details.language,
      releaseDate: details.releaseDate,
      genre: details.genre,
      durationMinutes: Number(details.durationMinutes),
      posterUrl: media.posterUrl,
      trailerUrl: media.trailerUrl,
      rating: review.rating,
      cast,
      crew,
    };
  }

  // protected async onPosterFileSelected(event: Event): Promise<void> {
  //   const input = event.target as HTMLInputElement;
  //   const file = input.files?.[0];

  //   if (!file) {
  //     return;
  //   }

  //   if (!file.type.startsWith('image/')) {
  //     this.submitError.set('Please select a valid image file.');
  //     return;
  //   }

  //   this.submitError.set(null);
  //   this.isPosterUploading.set(false);

  //   file && this.file.set(file);
  // }
}
