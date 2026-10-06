import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app-config.token';
import { ShowResponse } from '../show/show.models';
import { SeatInfo } from '../show/show.models';

export interface CreateBookingRequest {
  userId: string;
  showId: string;
  seatIds: string[];
  email: string;
  phone?: string;
  lockId: string | undefined;
}

export interface CreateBookingResponse {
  bookingId?: string;
  userId: string;
  showId: string;
  show: ShowResponse;
  seats: SeatInfo[];
  totalAmount: number;
  status: string;
  email: string;
  phone?: string;
  lockId?: string;
  expiresAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface LockSeatRequest {
  showId: string;
  seatIds: string[];
  userId: string;
}

export interface LockSeatResponse {
  success: boolean;
  lock_id: string;
  message: string;
  locked_until: string; // ISO datetime
  locked_seat_ids: string[];
}

@Injectable({ providedIn: 'root' })
export class BookingService {
  private readonly http = inject(HttpClient);
  private readonly appConfig = inject(APP_CONFIG);

  lockSeat(request: LockSeatRequest): Observable<LockSeatResponse> {
    return this.http.post<LockSeatResponse>(
      this.appConfig.seat.lockUrl,
      request,
    );
  }

  createBooking(
    request: CreateBookingRequest,
  ): Observable<CreateBookingResponse> {
    return this.http.post<CreateBookingResponse>(
      this.appConfig.booking.createUrl,
      request,
    );
  }
}
