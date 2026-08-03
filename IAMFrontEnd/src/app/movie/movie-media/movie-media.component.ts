import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
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
  @Input() posterPreviewUrlInput: string | null = null;
  @Output() submitError = new EventEmitter<string | null>();
  @Output() isPosterUploading = new EventEmitter<boolean>();
  protected posterPreviewUrl = signal<string | null>(null);

  protected resolvedPreviewUrl(): string | null {
    return this.posterPreviewUrl() ?? this.posterPreviewUrlInput;
  }

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

    if (file) {
      this.formGroup.controls.poster.setValue(file);
      this.posterPreviewUrl.set(URL.createObjectURL(file));
    }
  }
}
