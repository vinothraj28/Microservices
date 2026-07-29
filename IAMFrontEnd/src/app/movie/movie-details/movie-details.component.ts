import { Component, input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

type MovieDetailsForm = {
  title: FormControl<string>;
  description: FormControl<string>;
  durationMinutes: FormControl<number | null>;
  genre: FormControl<string>;
  language: FormControl<string>;
  releaseDate: FormControl<string>;
};

@Component({
  selector: 'app-movie-details',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './movie-details.component.html',
})
export class MovieDetailsComponent {
  //@Input({ required: true }) formGroup!: FormGroup<MovieDetailsForm>;
  readonly formGroup = input.required<FormGroup<MovieDetailsForm>>();
}
