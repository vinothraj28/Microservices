import { Component, Input } from '@angular/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

type MovieCreditsForm = {
  cast: FormArray<FormControl<string>>;
  crew: FormArray<FormControl<string>>;
};

@Component({
  selector: 'app-movie-credits',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './movie-credits.component.html',
})
export class MovieCreditsComponent {
  @Input({ required: true }) formGroup!: FormGroup<MovieCreditsForm>;

  protected get castControls(): FormArray<FormControl<string>> {
    return this.formGroup.controls.cast;
  }

  protected get crewControls(): FormArray<FormControl<string>> {
    return this.formGroup.controls.crew;
  }

  protected addCast(): void {
    this.castControls.push(new FormControl('', { nonNullable: true }));
  }

  protected removeCast(index: number): void {
    if (this.castControls.length > 1) {
      this.castControls.removeAt(index);
    }
  }

  protected addCrew(): void {
    this.crewControls.push(new FormControl('', { nonNullable: true }));
  }

  protected removeCrew(index: number): void {
    if (this.crewControls.length > 1) {
      this.crewControls.removeAt(index);
    }
  }
}
