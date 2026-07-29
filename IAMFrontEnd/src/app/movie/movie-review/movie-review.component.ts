import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

type MovieReviewForm = {
  rating: FormControl<string>;
};

@Component({
  selector: 'app-movie-review',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './movie-review.component.html',
})
export class MovieReviewComponent {
  @Input({ required: true }) formGroup!: FormGroup<MovieReviewForm>;
  protected readonly ratings = ['U', 'UA', 'A', 'R'];
}
