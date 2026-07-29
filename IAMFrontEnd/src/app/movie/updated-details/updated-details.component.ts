import { Component, Input, input, effect } from '@angular/core';
import type { FormGroup } from '@angular/forms';
import { MovieForm, MovieValue } from '../movie-form/movie-form.component';
import { computed } from '@angular/core';

@Component({
  selector: 'app-updated-details',
  imports: [],
  templateUrl: './updated-details.component.html',
  styleUrls: ['./updated-details.component.css'],
})
export class UpdatedDetailsComponent {
  readonly movie = input<MovieValue>();

  constructor() {
    effect(() => {
      console.log('FormGroup value changed:');
      console.log(this.movie());
    });
  }

  readonly details = computed(() => this.movie()?.details);
  readonly media = computed(() => this.movie()?.media);
  readonly review = computed(() => this.movie()?.review);
  readonly credits = computed(() => this.movie()?.credits);
}
