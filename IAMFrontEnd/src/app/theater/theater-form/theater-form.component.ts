import {
  ChangeDetectionStrategy,
  Component,
  signal,
  OnInit,
  inject,
} from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import {
  TheaterResponse,
  TheaterService,
} from '../../core/services/theater/theater.service';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { ToastService } from '../../core/services/toast/toast.service';

type TheaterDetailsForm = {
  name: FormControl<string>;
  address: FormControl<string>;
  city: FormControl<string>;
  state: FormControl<string>;
  pincode: FormControl<string>;
};

type TheaterContactForm = {
  phone: FormControl<string>;
  email: FormControl<string>;
};

type TheaterLocationForm = {
  latitude: FormControl<number | null>;
  longitude: FormControl<number | null>;
};

type TheaterAmenitiesForm = {
  amenities: FormControl<string[]>;
};

type TheaterForm = {
  details: FormGroup<TheaterDetailsForm>;
  contact: FormGroup<TheaterContactForm>;
  location: FormGroup<TheaterLocationForm>;
  amenities: FormGroup<TheaterAmenitiesForm>;
};

@Component({
  selector: 'app-theater-form',
  standalone: true,
  imports: [ReactiveFormsModule, MatStepperModule, MatButtonModule],
  templateUrl: './theater-form.component.html',
  styleUrls: ['./theater-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TheaterFormComponent implements OnInit {
  protected readonly isEditMode = signal(false);
  protected readonly isSubmitting = signal(false);
  protected readonly submitError = signal<string | null>(null);

  private router = inject(Router);
  private toastService = inject(ToastService);

  async ngOnInit(): Promise<void> {
    const theaterId = this.route.snapshot.paramMap.get('theaterId');
    if (theaterId) {
      this.isEditMode.set(true);
      await this.loadTheaterData(theaterId);
    }
  }

  constructor(
    private theaterService: TheaterService,
    private route: ActivatedRoute,
  ) {}

  protected readonly theaterForm = new FormGroup<TheaterForm>({
    details: new FormGroup<TheaterDetailsForm>({
      name: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      address: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      city: new FormControl('', { nonNullable: true }),
      state: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(/^[A-Za-z\s]+$/)],
      }),
      pincode: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(/^\d{6}$/)],
      }),
    }),
    contact: new FormGroup<TheaterContactForm>({
      phone: new FormControl('', {
        nonNullable: true,
        validators: [Validators.pattern(/^\d{10}$/)],
      }),
      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.email],
      }),
    }),
    location: new FormGroup<TheaterLocationForm>({
      latitude: new FormControl<number | null>(null, {
        validators: [Validators.min(-90), Validators.max(90)],
      }),
      longitude: new FormControl<number | null>(null, {
        validators: [Validators.min(-180), Validators.max(180)],
      }),
    }),
    amenities: new FormGroup<TheaterAmenitiesForm>({
      amenities: new FormControl<string[]>([], { nonNullable: true }),
    }),
  });

  protected readonly amenitiesList = [
    'Parking',
    'Wheelchair Accessible',
    'Restrooms',
    'Concessions',
    'VIP Seating',
    '3D Screens',
    'IMAX Screens',
  ];

  protected nextStep(stepper: MatStepper, group: FormGroup): void {
    group.markAllAsTouched();
    if (group.invalid) return;
    stepper.next();
  }

  protected toggleAmenity(amenity: string): void {
    const current =
      this.theaterForm.controls.amenities.controls.amenities.value;
    const exists = current.includes(amenity);
    const next = exists
      ? current.filter((item) => item !== amenity)
      : [...current, amenity];
    this.theaterForm.controls.amenities.controls.amenities.setValue(next);
  }

  protected isAmenitySelected(amenity: string): boolean {
    return this.theaterForm.controls.amenities.controls.amenities.value.includes(
      amenity,
    );
  }

  private async loadTheaterData(theaterId: string): Promise<void> {
    this.isSubmitting.set(true);
    this.submitError.set(null);

    try {
      const theater: TheaterResponse = await firstValueFrom(
        this.theaterService.getTheaterById(theaterId),
      );

      this.theaterForm.controls.details.patchValue({
        name: theater.name,
        address: theater.address,
        city: theater.city,
        state: theater.state,
        pincode: theater.pincode,
      });

      this.theaterForm.controls.contact.patchValue({
        phone: theater.phone,
        email: theater.email,
      });

      this.theaterForm.controls.location.patchValue({
        latitude: theater.latitude,
        longitude: theater.longitude,
      });

      this.theaterForm.controls.amenities.patchValue({
        amenities: theater.amenities,
      });
    } catch (error) {
      console.error('Error loading theater data:', error);
      this.submitError.set('Failed to load theater data. Please try again.');
    } finally {
      this.isSubmitting.set(false);
    }
  }

  protected async submitForm(): Promise<void> {
    if (this.theaterForm.invalid) {
      this.theaterForm.markAllAsTouched();
      return;
    }

    const payload = this.theaterForm.getRawValue();
    console.log('Theater payload:', payload);

    const theaterRequest = {
      name: payload.details.name,
      address: payload.details.address,
      city: payload.details.city,
      state: payload.details.state,
      pincode: payload.details.pincode,
      phone: payload.contact.phone,
      email: payload.contact.email,
      latitude: payload.location.latitude,
      longitude: payload.location.longitude,
      amenities: payload.amenities.amenities,
    };

    let response: TheaterResponse | undefined;

    if (this.isEditMode()) {
      response = await firstValueFrom(
        this.theaterService.updateTheater(
          this.route.snapshot.paramMap.get('theaterId')!,
          theaterRequest,
        ),
      );
    } else {
      response = await firstValueFrom(
        this.theaterService.registerTheater(theaterRequest),
      );
    }

    if (response) {
      console.log('Theater registered successfully:', response);
      this.theaterForm.reset();
      this.toastService.setToast(
        this.isEditMode()
          ? 'Theater updated successfully!'
          : 'Theater registered successfully!',
        'success',
      );
      this.router.navigate(['base', 'theater']);
    } else {
      console.error('Failed to register theater');
      this.toastService.setToast(
        this.isEditMode()
          ? 'Failed to update theater. Please try again.'
          : 'Failed to register theater. Please try again.',
        'error',
      );
    }
  }
}
