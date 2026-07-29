import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import type { MovieMediaForm } from '../movie-form/movie-form.component';

@Component({
  selector: 'app-movie-media',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './movie-media.component.html',
})
export class MovieMediaComponent {
  @Input({ required: true }) formGroup!: FormGroup<MovieMediaForm>;
  @Output() submitError = new EventEmitter<string | null>();
  @Output() isPosterUploading = new EventEmitter<boolean>();

  protected async onPosterFileSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    if (!file.type.startsWith('image/')) {
      this.submitError.emit('Please select a valid image file.');
      return;
    }

    this.submitError.emit(null);
    this.isPosterUploading.emit(false);

    file && this.formGroup.controls.poster.setValue(file);
  }
}
