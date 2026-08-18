// import {
//   ChangeDetectionStrategy,
//   Component,
//   computed,
//   inject,
//   signal,
// } from '@angular/core';
// import { DecimalPipe } from '@angular/common';
// import {
//   AbstractControl,
//   FormArray,
//   FormControl,
//   FormGroup,
//   ReactiveFormsModule,
//   ValidationErrors,
//   Validators,
// } from '@angular/forms';
// import { ActivatedRoute } from '@angular/router';
// import { firstValueFrom, map, startWith } from 'rxjs';
// import { toSignal } from '@angular/core/rxjs-interop';
// import { screenRequest, ScreenService } from '../../core/services/screen/screen.service';

// type ScreenType = 'REGULAR' | 'IMAX' | '4DX' | 'DOLBY';

// type SeatType = 'REGULAR' | 'PREMIUM' | 'RECLINER' | 'VIP';

// type SeatLayoutForm = {
//   rowName: FormControl<string>;
//   startSeatNumber: FormControl<number>;
//   endSeatNumber: FormControl<number>;
//   seatType: FormControl<SeatType>;
//   priceMultiplier: FormControl<number>;
// };

// type ScreenFormModel = {
//   theaterId: FormControl<string>; //Fetched from the route parameter and disabled in the form
//   screenName: FormControl<string>;
//   screenNumber: FormControl<number>;
//   totalRows: FormControl<number>;
//   // seatsPerRow: FormControl<number>;
//   screenType: FormControl<ScreenType>;
//   seatLayout: FormArray<FormGroup<SeatLayoutForm>>;
// };

// @Component({
//   selector: 'app-screen-form',
//   standalone: true,
//   imports: [ReactiveFormsModule, DecimalPipe],
//   templateUrl: './screen-form.component.html',
//   styleUrl: './screen-form.component.css',
//   changeDetection: ChangeDetectionStrategy.OnPush,
// })
// export class ScreenFormComponent {
//   private readonly route = inject(ActivatedRoute);
//   private readonly screenService = inject(ScreenService);

//   protected readonly isSubmitting = signal(false);
//   protected readonly submitError = signal<string | null>(null);
//   protected readonly submitSuccess = signal<string | null>(null);

//   protected readonly selectedLayoutIndex = signal(0);
//   protected readonly seatLayoutApplied = signal(false);

//   protected readonly theaterId = signal(
//     this.route.snapshot.paramMap.get('theaterId') ?? '',
//   );

//   protected readonly screenTypes: ScreenType[] = [
//     'REGULAR',
//     'IMAX',
//     '4DX',
//     'DOLBY',
//   ];

//   protected readonly seatTypes: SeatType[] = [
//     'REGULAR',
//     'PREMIUM',
//     'RECLINER',
//     'VIP',
//   ];

//   private readonly seatRangeValidator = (
//     control: AbstractControl,
//   ): ValidationErrors | null => {
//     const start = control.get('startSeatNumber')?.value;

//     const end = control.get('endSeatNumber')?.value;

//     if (typeof start !== 'number' || typeof end !== 'number') {
//       return null;
//     }

//     return end >= start ? null : { invalidSeatRange: true };
//   };

//   protected readonly form = new FormGroup<ScreenFormModel>({
//     theaterId: new FormControl(
//       {
//         value: this.theaterId(),
//         disabled: true,
//       },
//       {
//         nonNullable: true,
//         validators: [Validators.required],
//       },
//     ),

//     screenName: new FormControl('', {
//       nonNullable: true,
//       validators: [Validators.required, Validators.maxLength(80)],
//     }),

//     screenNumber: new FormControl(1, {
//       nonNullable: true,
//       validators: [Validators.required, Validators.min(1)],
//     }),

//     totalRows: new FormControl(1, {
//       nonNullable: true,
//       validators: [Validators.required, Validators.min(1)],
//     }),

//     // seatsPerRow: new FormControl(18, {
//     //   nonNullable: true,
//     //   validators: [Validators.required, Validators.min(1)],
//     // }),

//     screenType: new FormControl<ScreenType>('REGULAR', {
//       nonNullable: true,
//       validators: [Validators.required],
//     }),

//     seatLayout: new FormArray<FormGroup<SeatLayoutForm>>([
//       this.createSeatLayoutGroup('A', 1, 18),
//       this.createSeatLayoutGroup('B', 1, 18),
//       this.createSeatLayoutGroup('C', 1, 18),
//     ]),
//   });

//   protected readonly screenNameValue = toSignal(
//     this.form.controls.screenName.valueChanges.pipe(
//       startWith(this.form.controls.screenName.value),
//       map((value) => value.trim()),
//     ),
//     { initialValue: this.form.controls.screenName.value.trim() },
//   );

//   protected readonly isStep1Complete = computed(() => {
//     return (
//       this.screenNameValue().length > 0 && this.form.controls.screenName.valid
//     );
//   });

//   protected readonly isStep2Complete = computed(() => {
//     return (
//       this.seatLayouts.length > 0 &&
//       this.seatLayouts.valid &&
//       this.seatLayoutApplied()
//     );
//   });

//   protected readonly isStep3Complete = computed(() => {
//     return this.form.controls.screenType.valid;
//   });

//   protected get seatLayouts(): FormArray<FormGroup<SeatLayoutForm>> {
//     return this.form.controls.seatLayout;
//   }

//   protected readonly selectedLayout = computed(() => {
//     return this.seatLayouts.at(this.selectedLayoutIndex()) ?? null;
//   });

//   protected totalSeatCount(): number {
//     return this.seatLayouts.controls.reduce((total, layout) => {
//       return total + this.getSeatCount(layout);
//     }, 0);
//   }

//   protected regularSeatCount(): number {
//     return this.countSeatsByType('REGULAR');
//   }

//   protected premiumSeatCount(): number {
//     return this.countSeatsByType('PREMIUM');
//   }

//   protected reclinerSeatCount(): number {
//     return this.countSeatsByType('RECLINER');
//   }

//   protected vipSeatCount(): number {
//     return this.countSeatsByType('VIP');
//   }

//   protected selectLayout(index: number): void {
//     this.selectedLayoutIndex.set(index);
//     this.submitError.set(null);
//     this.submitSuccess.set(null);
//   }

//   protected addSeatLayout(): void {
//     const rowName = this.generateRowName();

//     const seatsPerRow = 18;

//     this.seatLayouts.push(this.createSeatLayoutGroup(rowName, 1, seatsPerRow));

//     const newIndex = this.seatLayouts.length - 1;

//     this.selectedLayoutIndex.set(newIndex);

//     this.form.controls.totalRows.setValue(this.seatLayouts.length);
//   }

//   protected removeSeatLayout(index: number): void {
//     if (this.seatLayouts.length === 1) {
//       return;
//     }

//     this.seatLayouts.removeAt(index);

//     let nextIndex = this.selectedLayoutIndex();

//     if (nextIndex >= this.seatLayouts.length) {
//       nextIndex = this.seatLayouts.length - 1;
//     }

//     this.selectedLayoutIndex.set(nextIndex);

//     this.form.controls.totalRows.setValue(this.seatLayouts.length);
//   }

//   protected seatNumbers(layout: FormGroup<SeatLayoutForm>): number[] {
//     const start = layout.controls.startSeatNumber.value;

//     const end = layout.controls.endSeatNumber.value;

//     if (end < start) {
//       return [];
//     }

//     return Array.from({ length: end - start + 1 }, (_, index) => start + index);
//   }

//   protected getSeatCount(layout: FormGroup<SeatLayoutForm>): number {
//     const start = layout.controls.startSeatNumber.value;
//     const end = layout.controls.endSeatNumber.value;
//     return Math.max(0, end - start + 1);
//   }

//   protected seatTypeClass(layout: FormGroup<SeatLayoutForm>): string {
//     return `seat--${layout.controls.seatType.value.toLowerCase()}`;
//   }

//   protected seatTypeLabel(type: SeatType): string {
//     return type.charAt(0) + type.slice(1).toLowerCase();
//   }

//   protected getSelectedSeatCount(): number {
//     const layout = this.selectedLayout();

//     if (!layout) {
//       return 0;
//     }

//     return this.getSeatCount(layout);
//   }

//   protected applyRowChanges(): void {
//     this.selectedLayout()?.markAsDirty();
//     this.selectedLayout()?.markAsTouched();
//     this.seatLayoutApplied.set(true);

//     console.log(
//       this.seatLayouts.length > 0,
//       this.seatLayouts.valid,
//       this.seatLayouts.touched,
//     );

//     this.submitError.set(null);
//   }

//   protected resetForm(): void {
//     this.form.controls.screenName.setValue('');
//     this.form.controls.screenNumber.setValue(1);
//     this.form.controls.screenType.setValue('REGULAR');
//     //this.form.controls.seatsPerRow.setValue(18);

//     this.seatLayouts.clear();

//     this.seatLayouts.push(this.createSeatLayoutGroup('A', 1, 18));
//     this.seatLayouts.push(this.createSeatLayoutGroup('B', 1, 18));
//     this.seatLayouts.push(this.createSeatLayoutGroup('C', 1, 18));

//     this.form.controls.totalRows.setValue(this.seatLayouts.length);

//     this.selectedLayoutIndex.set(0);

//     this.form.markAsPristine();
//     this.form.markAsUntouched();

//     this.submitError.set(null);
//     this.submitSuccess.set(null);
//   }

//   protected async submit(): Promise<void> {
//     this.submitError.set(null);
//     this.submitSuccess.set(null);

//     this.form.markAllAsTouched();

//     if (this.form.invalid) {
//       this.submitError.set('Please fix the highlighted fields before saving.');

//       return;
//     }

//     this.isSubmitting.set(true);

//     try {
//       let payload : screenRequest = { ...this.form.getRawValue(), totalSeats: this.totalSeatCount() };

//       console.log('Screen payload:', payload);

//       const response = await firstValueFrom(
//         this.screenService.registerScreen(payload),
//       );
//       console.log('Screen registered successfully:', response);

//       this.submitSuccess.set('Screen saved successfully.');
//     } catch (error) {
//       console.error(error);
//       this.submitError.set('Unable to save the screen. Please try again.');
//     } finally {
//       this.isSubmitting.set(false);
//     }
//   }

//   private createSeatLayoutGroup(
//     rowName: string,
//     startSeatNumber: number,
//     endSeatNumber: number,
//     seatType: SeatType = 'REGULAR',
//     priceMultiplier = 1,
//   ): FormGroup<SeatLayoutForm> {
//     return new FormGroup<SeatLayoutForm>(
//       {
//         rowName: new FormControl(rowName, {
//           nonNullable: true,
//           validators: [Validators.required, Validators.maxLength(10)],
//         }),

//         startSeatNumber: new FormControl(startSeatNumber, {
//           nonNullable: true,
//           validators: [Validators.required, Validators.min(1)],
//         }),

//         endSeatNumber: new FormControl(endSeatNumber, {
//           nonNullable: true,
//           validators: [Validators.required, Validators.min(1)],
//         }),

//         seatType: new FormControl<SeatType>(seatType, {
//           nonNullable: true,
//           validators: [Validators.required],
//         }),

//         priceMultiplier: new FormControl(priceMultiplier, {
//           nonNullable: true,
//           validators: [Validators.required, Validators.min(0.1)],
//         }),
//       },
//       {
//         validators: [this.seatRangeValidator],
//       },
//     );
//   }

//   private generateRowName(): string {
//     const index = this.seatLayouts.length;

//     if (index < 26) {
//       return String.fromCharCode(65 + index);
//     }

//     return `R${index + 1}`;
//   }

//   private countSeatsByType(type: SeatType): number {
//     return this.seatLayouts.controls
//       .filter((layout) => layout.controls.seatType.value === type)
//       .reduce((total, layout) => total + this.getSeatCount(layout), 0);
//   }
// }
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { DecimalPipe } from '@angular/common';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, startWith } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  screenRequest,
  screenResponse,
  ScreenService,
} from '../../core/services/screen/screen.service';

type ScreenType = 'REGULAR' | 'IMAX' | '4DX' | 'DOLBY';
type SeatType = 'REGULAR' | 'PREMIUM' | 'RECLINER' | 'VIP';

type SeatLayoutForm = {
  rowName: FormControl<string>;
  startSeatNumber: FormControl<number>;
  endSeatNumber: FormControl<number>;
  seatType: FormControl<SeatType>;
  priceMultiplier: FormControl<number>;
};

type ScreenFormModel = {
  theaterId: FormControl<string>;
  screenName: FormControl<string>;
  screenNumber: FormControl<number>;
  totalRows: FormControl<number>;
  screenType: FormControl<ScreenType>;
  seatLayout: FormArray<FormGroup<SeatLayoutForm>>;
};

type ComponentMode = 'add' | 'edit';

@Component({
  selector: 'app-screen-form',
  standalone: true,
  imports: [ReactiveFormsModule, DecimalPipe],
  templateUrl: './screen-form.component.html',
  styleUrl: './screen-form.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ScreenFormComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly screenService = inject(ScreenService);

  // Mode and IDs
  protected readonly mode = signal<ComponentMode>('add');
  protected readonly screenId = signal<string | null>(null);
  protected readonly theaterId = signal<string>('');

  // UI State
  protected readonly isLoading = signal(false);
  protected readonly isSubmitting = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly submitSuccess = signal<string | null>(null);

  // Seat Layout State
  protected readonly selectedLayoutIndex = signal(0);
  protected readonly seatLayoutApplied = signal(false);

  // Constants
  protected readonly DEFAULT_SEATS_PER_ROW = 18;
  protected readonly screenTypes: ScreenType[] = [
    'REGULAR',
    'IMAX',
    '4DX',
    'DOLBY',
  ];
  protected readonly seatTypes: SeatType[] = [
    'REGULAR',
    'PREMIUM',
    'RECLINER',
    'VIP',
  ];

  // Computed Values
  protected readonly pageTitle = computed(() =>
    this.mode() === 'add' ? 'Add New Screen' : 'Edit Screen',
  );

  protected readonly submitButtonText = computed(() => {
    if (this.isSubmitting()) {
      return 'Saving...';
    }
    return this.mode() === 'add' ? 'Save Screen' : 'Update Screen';
  });

  protected readonly isEditMode = computed(() => this.mode() === 'edit');

  // Validator
  private readonly seatRangeValidator = (
    control: AbstractControl,
  ): ValidationErrors | null => {
    const start = control.get('startSeatNumber')?.value;
    const end = control.get('endSeatNumber')?.value;

    if (typeof start !== 'number' || typeof end !== 'number') {
      return null;
    }

    return end >= start ? null : { invalidSeatRange: true };
  };

  // Form Definition
  protected readonly form = new FormGroup<ScreenFormModel>({
    theaterId: new FormControl(
      { value: '', disabled: true },
      {
        nonNullable: true,
        validators: [Validators.required],
      },
    ),

    screenName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(80)],
    }),

    screenNumber: new FormControl(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1)],
    }),

    totalRows: new FormControl(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1)],
    }),

    screenType: new FormControl<ScreenType>('REGULAR', {
      nonNullable: true,
      validators: [Validators.required],
    }),

    seatLayout: new FormArray<FormGroup<SeatLayoutForm>>([]),
  });

  protected readonly screenNameValue = toSignal(
    this.form.controls.screenName.valueChanges.pipe(
      startWith(this.form.controls.screenName.value),
      map((value) => value.trim()),
    ),
    { initialValue: this.form.controls.screenName.value.trim() },
  );

  // Step Completion
  protected readonly isStep1Complete = computed(() => {
    return (
      this.screenNameValue().length > 0 && this.form.controls.screenName.valid
    );
  });

  protected readonly isStep2Complete = computed(() => {
    return (
      this.seatLayouts.length > 0 &&
      this.seatLayouts.valid &&
      this.seatLayoutApplied()
    );
  });

  protected readonly isStep3Complete = computed(() => {
    return this.form.controls.screenType.valid;
  });

  protected get seatLayouts(): FormArray<FormGroup<SeatLayoutForm>> {
    return this.form.controls.seatLayout;
  }

  protected readonly selectedLayout = computed(() => {
    return this.seatLayouts.at(this.selectedLayoutIndex()) ?? null;
  });

  // Lifecycle
  async ngOnInit(): Promise<void> {
    this.initializeFromRoute();
    await this.loadScreenData();
  }

  // Initialization
  private initializeFromRoute(): void {
    const params = this.route.snapshot.paramMap;
    const theaterId = params.get('theaterId');
    const screenId = params.get('screenId');

    if (!theaterId) {
      this.submitError.set('Theater ID is missing');
      return;
    }

    this.theaterId.set(theaterId);
    this.form.controls.theaterId.setValue(theaterId);

    if (screenId) {
      this.mode.set('edit');
      this.screenId.set(screenId);
    } else {
      this.mode.set('add');
      this.initializeDefaultLayout();
    }
  }

  private initializeDefaultLayout(): void {
    this.seatLayouts.clear();
    this.seatLayouts.push(
      this.createSeatLayoutGroup('A', 1, this.DEFAULT_SEATS_PER_ROW),
    );
    this.seatLayouts.push(
      this.createSeatLayoutGroup('B', 1, this.DEFAULT_SEATS_PER_ROW),
    );
    this.seatLayouts.push(
      this.createSeatLayoutGroup('C', 1, this.DEFAULT_SEATS_PER_ROW),
    );
    this.form.controls.totalRows.setValue(this.seatLayouts.length);
  }

  private async loadScreenData(): Promise<void> {
    if (this.mode() !== 'edit' || !this.screenId()) {
      return;
    }

    this.isLoading.set(true);
    this.submitError.set(null);

    try {
      const response = await firstValueFrom(
        this.screenService.getScreenById(this.theaterId(), this.screenId()!),
      );

      if (response.body) {
        this.populateForm(response.body);
      }
    } catch (error) {
      console.error('Error loading screen data:', error);
      this.submitError.set(
        'Failed to load screen data. Please refresh the page.',
      );
    } finally {
      this.isLoading.set(false);
    }
  }

  private populateForm(data: screenResponse): void {
    this.form.patchValue({
      screenName: data.screenName,
      screenNumber: data.screenNumber,
      totalRows: data.seatLayout?.length || 0,
      screenType: data.screenType as ScreenType,
    });

    // Populate seat layout
    this.seatLayouts.clear();
    if (data.seatLayout && data.seatLayout.length > 0) {
      data.seatLayout.forEach((layout) => {
        this.seatLayouts.push(
          this.createSeatLayoutGroup(
            layout.rowName,
            layout.startSeatNumber,
            layout.endSeatNumber,
            layout.seatType as SeatType,
            layout.priceMultiplier,
          ),
        );
      });
    } else {
      this.initializeDefaultLayout();
    }

    this.form.controls.totalRows.setValue(this.seatLayouts.length);
    this.seatLayoutApplied.set(true);
  }

  // Seat Count Methods
  protected totalSeatCount(): number {
    return this.seatLayouts.controls.reduce((total, layout) => {
      return total + this.getSeatCount(layout);
    }, 0);
  }

  protected regularSeatCount(): number {
    return this.countSeatsByType('REGULAR');
  }

  protected premiumSeatCount(): number {
    return this.countSeatsByType('PREMIUM');
  }

  protected reclinerSeatCount(): number {
    return this.countSeatsByType('RECLINER');
  }

  protected vipSeatCount(): number {
    return this.countSeatsByType('VIP');
  }

  private countSeatsByType(type: SeatType): number {
    return this.seatLayouts.controls
      .filter((layout) => layout.controls.seatType.value === type)
      .reduce((total, layout) => total + this.getSeatCount(layout), 0);
  }

  // Layout Management
  protected selectLayout(index: number): void {
    this.selectedLayoutIndex.set(index);
    this.submitError.set(null);
    this.submitSuccess.set(null);
  }

  protected addSeatLayout(): void {
    const rowName = this.generateRowName();
    this.seatLayouts.push(
      this.createSeatLayoutGroup(rowName, 1, this.DEFAULT_SEATS_PER_ROW),
    );

    const newIndex = this.seatLayouts.length - 1;
    this.selectedLayoutIndex.set(newIndex);
    this.form.controls.totalRows.setValue(this.seatLayouts.length);
  }

  protected removeSeatLayout(index: number): void {
    if (this.seatLayouts.length === 1) {
      return;
    }

    this.seatLayouts.removeAt(index);

    let nextIndex = this.selectedLayoutIndex();
    if (nextIndex >= this.seatLayouts.length) {
      nextIndex = this.seatLayouts.length - 1;
    }

    this.selectedLayoutIndex.set(nextIndex);
    this.form.controls.totalRows.setValue(this.seatLayouts.length);
  }

  protected applyRowChanges(): void {
    this.selectedLayout()?.markAsDirty();
    this.selectedLayout()?.markAsTouched();
    this.seatLayoutApplied.set(true);
    this.submitError.set(null);
  }

  // Utility Methods
  protected seatNumbers(layout: FormGroup<SeatLayoutForm>): number[] {
    const start = layout.controls.startSeatNumber.value;
    const end = layout.controls.endSeatNumber.value;

    if (end < start) {
      return [];
    }

    return Array.from({ length: end - start + 1 }, (_, index) => start + index);
  }

  protected getSeatCount(layout: FormGroup<SeatLayoutForm>): number {
    const start = layout.controls.startSeatNumber.value;
    const end = layout.controls.endSeatNumber.value;
    return Math.max(0, end - start + 1);
  }

  protected seatTypeClass(layout: FormGroup<SeatLayoutForm>): string {
    return `seat--${layout.controls.seatType.value.toLowerCase()}`;
  }

  protected seatTypeLabel(type: SeatType): string {
    return type.charAt(0) + type.slice(1).toLowerCase();
  }

  protected getSelectedSeatCount(): number {
    const layout = this.selectedLayout();
    if (!layout) {
      return 0;
    }
    return this.getSeatCount(layout);
  }

  private generateRowName(): string {
    const index = this.seatLayouts.length;
    if (index < 26) {
      return String.fromCharCode(65 + index);
    }
    return `R${index + 1}`;
  }

  // Form Actions
  protected resetForm(): void {
    if (this.mode() === 'edit') {
      // In edit mode, reload the original data
      this.loadScreenData();
    } else {
      // In add mode, reset to defaults
      this.form.controls.screenName.setValue('');
      this.form.controls.screenNumber.setValue(1);
      this.form.controls.screenType.setValue('REGULAR');
      this.initializeDefaultLayout();
      this.selectedLayoutIndex.set(0);
      this.form.markAsPristine();
      this.form.markAsUntouched();
    }

    this.submitError.set(null);
    this.submitSuccess.set(null);
  }

  protected async submit(): Promise<void> {
    this.submitError.set(null);
    this.submitSuccess.set(null);

    this.form.markAllAsTouched();

    if (this.form.invalid) {
      this.submitError.set('Please fix the highlighted fields before saving.');
      return;
    }

    this.isSubmitting.set(true);

    try {
      const payload: screenRequest = {
        ...this.form.getRawValue(),
        totalSeats: this.totalSeatCount(),
      };

      console.log('Screen payload:', payload);

      let response;
      if (this.mode() === 'edit' && this.screenId()) {
        response = await firstValueFrom(
          this.screenService.updateScreen(
            this.theaterId(),
            this.screenId()!,
            payload,
          ),
        );
        this.submitSuccess.set('Screen updated successfully.');
      } else {
        response = await firstValueFrom(
          this.screenService.registerScreen(payload),
        );
        this.submitSuccess.set('Screen created successfully.');
      }

      console.log('Screen saved successfully:', response);

      // Navigate back to list after short delay
      setTimeout(() => {
        this.router.navigate([
          '/theater',
          this.theaterId(),
          'screen',
          'list',
        ]);
      }, 1500);
    } catch (error) {
      console.error(error);
      const action = this.mode() === 'edit' ? 'update' : 'save';
      this.submitError.set(
        `Unable to ${action} the screen. Please try again.`,
      );
    } finally {
      this.isSubmitting.set(false);
    }
  }

  protected cancel(): void {
    this.router.navigate(['/theater', this.theaterId(), 'screen', 'list']);
  }

  // Form Factory
  private createSeatLayoutGroup(
    rowName: string,
    startSeatNumber: number,
    endSeatNumber: number,
    seatType: SeatType = 'REGULAR',
    priceMultiplier = 1,
  ): FormGroup<SeatLayoutForm> {
    return new FormGroup<SeatLayoutForm>(
      {
        rowName: new FormControl(rowName, {
          nonNullable: true,
          validators: [Validators.required, Validators.maxLength(10)],
        }),

        startSeatNumber: new FormControl(startSeatNumber, {
          nonNullable: true,
          validators: [Validators.required, Validators.min(1)],
        }),

        endSeatNumber: new FormControl(endSeatNumber, {
          nonNullable: true,
          validators: [Validators.required, Validators.min(1)],
        }),

        seatType: new FormControl<SeatType>(seatType, {
          nonNullable: true,
          validators: [Validators.required],
        }),

        priceMultiplier: new FormControl(priceMultiplier, {
          nonNullable: true,
          validators: [Validators.required, Validators.min(0.1)],
        }),
      },
      {
        validators: [this.seatRangeValidator],
      },
    );
  }
}