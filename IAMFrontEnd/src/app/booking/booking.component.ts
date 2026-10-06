import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
  signal,
} from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { BookingStateService } from '../core/services/booking/booking-state.service';
import {
  BookingService,
  CreateBookingRequest,
} from '../core/services/booking/booking.service';
import { AuthSessionService } from '../core/services/auth/auth-session.service';
import { ToastService } from '../core/services/toast/toast.service';

type BookingForm = {
  userId: FormControl<string>;
  showId: FormControl<string>;
  seatIds: FormControl<string[]>;
  email: FormControl<string>;
  phone: FormControl<string>;
};

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BookingComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly toastService = inject(ToastService);
  private readonly authSession = inject(AuthSessionService);
  private readonly bookingService = inject(BookingService);
  protected readonly bookingState = inject(BookingStateService);

  protected readonly isSubmitting = signal(false);
  protected readonly submitError = signal<string | null>(null);

  protected readonly bookingForm = new FormGroup<BookingForm>({
    userId: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    showId: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    seatIds: new FormControl<string[]>([], {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(1)],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    phone: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^\d{10}$/)],
    }),
  });

  ngOnInit(): void {
    if (!this.bookingState.hasSelection()) {
      this.toastService.setToast('Please select seats first', 'error');
      this.router.navigate(['/base/show/list']);
      return;
    }

    const claims = this.readClaims();
    this.bookingForm.patchValue({
      userId: String(
        claims['userId'] ?? claims['user_id'] ?? claims['sub'] ?? '',
      ),
      showId: this.bookingState.showId(),
      seatIds: this.bookingState.seatIds(),
      email: String(claims['email'] ?? ''),
    });
  }

  protected async submitForm(): Promise<void> {
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    const { userId, showId, seatIds, email, phone } =
      this.bookingForm.getRawValue();
    const request: CreateBookingRequest = {
      userId,
      showId,
      seatIds,
      email,
      ...(phone ? { phone } : {}),
    };

    this.isSubmitting.set(true);
    this.submitError.set(null);

    try {
      await firstValueFrom(this.bookingService.createBooking(request));
      this.bookingState.clear();
      this.toastService.setToast('Booking created successfully!', 'success');
      this.router.navigate(['/base/dashboard']);
    } catch (error) {
      console.error('Failed to create booking:', error);
      this.submitError.set('Failed to create booking. Please try again.');
      this.toastService.setToast(
        'Failed to create booking. Please try again.',
        'error',
      );
    } finally {
      this.isSubmitting.set(false);
    }
  }

  protected goBack(): void {
    this.router.navigate(['/base/show', this.bookingState.showId(), 'seats']);
  }

  private readClaims(): Record<string, unknown> {
    const token = this.authSession.getAccessToken();
    if (!token) {
      return {};
    }
    try {
      const payload = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(atob(payload)) as Record<string, unknown>;
    } catch {
      return {};
    }
  }
}
