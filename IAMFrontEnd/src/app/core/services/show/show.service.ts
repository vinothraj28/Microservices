import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app-config.token';
import {
  ShowType,
  SeatType,
  SeatStatus,
  CreateShowRequest,
  AvailableShowTimesRequest,
  AvailableShowTimesResponse,
  UpdateShowRequest,
  ShowResponse,
  ShowListResponse,
  SeatInfo,
  AvailableSeatsResponse,
  SEAT_PRICE_MULTIPLIERS,
} from './show.models';

// ==================== SERVICE ====================

@Injectable({
  providedIn: 'root',
})
export class ShowService {
  private readonly http = inject(HttpClient);
  private readonly appConfig = inject(APP_CONFIG);

  // ==================== UTILITY METHODS ====================

  /**
   * Convert JavaScript Date to API format (ISO-8601: YYYY-MM-DDTHH:mm:ss)
   * @param date JavaScript Date object
   * @returns Formatted date string
   */
  formatShowDateTime(showDateTime: string): string {
    const date = new Date(showDateTime);

    // 2. Custom configuration (US English)
    const usFormatter = new Intl.DateTimeFormat('en-US', {
      dateStyle: 'full',
      timeStyle: 'short',
    });
    console.log(usFormatter.format(date));
    // Output: "Tuesday, October 6, 2026 at 2:30 PM"

    // 3. Granular token selection
    const granularFormatter = new Intl.DateTimeFormat('en-GB', {
      year: 'numeric',
      month: 'long',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
    return granularFormatter.format(date);
  }

  /**
   * Convert API date string to JavaScript Date
   * @param dateStr ISO-8601 date string
   * @returns JavaScript Date object
   */
  parseShowDateTime(dateStr: string): Date {
    return new Date(dateStr);
  }

  /**
   * Format date for query parameters (YYYY-MM-DD)
   * @param date JavaScript Date object
   * @returns Formatted date string
   */
  formatDateForQuery(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  /**
   * Calculate seat price based on base price and seat type
   * @param basePrice Base price of the show
   * @param seatType Type of seat
   * @returns Calculated price
   */
  calculateSeatPrice(basePrice: number, seatType: SeatType): number {
    return basePrice * SEAT_PRICE_MULTIPLIERS[seatType];
  }

  // ==================== API METHODS ====================

  /**
   * Create a new movie show/screening
   * @param request Show creation request
   * @returns Observable of show response
   */
  createShow(request: CreateShowRequest): Observable<ShowResponse> {
    return this.http.post<ShowResponse>(this.appConfig.show.createUrl, request);
  }

  /**
   * Get available start times for a movie on the selected screen.
   */
  getAvailableShowTimes(
    request: AvailableShowTimesRequest,
  ): Observable<AvailableShowTimesResponse> {
    const url = `${this.appConfig.show.createUrl}/available-slots`;
    return this.http.post<AvailableShowTimesResponse>(url, request);
  }

  /**
   * Update an existing show (partial update)
   * @param showId Show ID
   * @param request Update request with partial data
   * @returns Observable of updated show response
   */
  updateShow(
    showId: string,
    request: UpdateShowRequest,
  ): Observable<ShowResponse> {
    const url = `${this.appConfig.show.updateUrl}/${showId}`;
    return this.http.put<ShowResponse>(url, { ...request, showId });
  }

  /**
   * Get a single show by ID
   * @param showId Show ID
   * @returns Observable of show response
   */
  getShowById(showId: string): Observable<ShowResponse> {
    const url = `${this.appConfig.show.getByIdUrl}/${showId}`;
    return this.http.get<ShowResponse>(url);
  }

  getShows(page: number, pageSize: number): Observable<ShowListResponse> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', pageSize.toString());

    const url = `${this.appConfig.show.getAllUrl}`;
    return this.http.get<ShowListResponse>(url, { params });
  }

  /**
   * Get all shows for a specific movie with optional filters
   * @param movieId Movie ID
   * @param date Optional date filter (YYYY-MM-DD)
   * @param city Optional city filter
   * @returns Observable of show list response
   */
  getShowsByMovie(
    movieId: string,
    date?: string,
    city?: string,
  ): Observable<ShowListResponse> {
    let params = new HttpParams();

    if (date) {
      params = params.set('date', date);
    }
    if (city) {
      params = params.set('city', city);
    }

    const url = `${this.appConfig.show.getByMovieUrl}/${movieId}`;
    return this.http.get<ShowListResponse>(url, { params });
  }

  /**
   * Get all shows for a specific theater with optional date filter
   * @param theaterId Theater ID
   * @param date Optional date filter (YYYY-MM-DD)
   * @returns Observable of show list response
   */
  getShowsByTheater(
    theaterId: string,
    date?: string,
  ): Observable<ShowListResponse> {
    let params = new HttpParams();

    if (date) {
      params = params.set('date', date);
    }

    const url = `${this.appConfig.show.getByTheaterUrl}/${theaterId}`;
    return this.http.get<ShowListResponse>(url, { params });
  }

  /**
   * Get all seats for a show with availability status and pricing
   * @param showId Show ID
   * @returns Observable of available seats response
   */
  getAvailableSeats(showId: string): Observable<AvailableSeatsResponse> {
    const url = `${this.appConfig.show.getSeatsUrl}/${showId}/seats`;
    return this.http.get<AvailableSeatsResponse>(url);
  }

  // ==================== CONVENIENCE METHODS ====================

  /**
   * Get shows by movie for today
   * @param movieId Movie ID
   * @param city Optional city filter
   * @returns Observable of show list response
   */
  getTodayShowsByMovie(
    movieId: string,
    city?: string,
  ): Observable<ShowListResponse> {
    const today = this.formatDateForQuery(new Date());
    return this.getShowsByMovie(movieId, today, city);
  }

  /**
   * Get shows by theater for today
   * @param theaterId Theater ID
   * @returns Observable of show list response
   */
  getTodayShowsByTheater(theaterId: string): Observable<ShowListResponse> {
    const today = this.formatDateForQuery(new Date());
    return this.getShowsByTheater(theaterId, today);
  }

  /**
   * Get available seats grouped by status
   * @param showId Show ID
   * @returns Observable with seats categorized by status
   */
  getSeatsGroupedByStatus(showId: string): Observable<{
    available: SeatInfo[];
    locked: SeatInfo[];
    booked: SeatInfo[];
    totalAvailable: number;
  }> {
    return new Observable((observer) => {
      this.getAvailableSeats(showId).subscribe({
        next: (response) => {
          const available = response.seats.filter(
            (s) => s.status === SeatStatus.AVAILABLE,
          );
          const locked = response.seats.filter(
            (s) => s.status === SeatStatus.LOCKED,
          );
          const booked = response.seats.filter(
            (s) => s.status === SeatStatus.BOOKED,
          );

          observer.next({
            available,
            locked,
            booked,
            totalAvailable: response.totalAvailable,
          });
          observer.complete();
        },
        error: (err) => observer.error(err),
      });
    });
  }

  /**
   * Get available seats grouped by seat type
   * @param showId Show ID
   * @returns Observable with seats categorized by type
   */
  getSeatsGroupedByType(showId: string): Observable<{
    regular: SeatInfo[];
    premium: SeatInfo[];
    recliner: SeatInfo[];
    vip: SeatInfo[];
  }> {
    return new Observable((observer) => {
      this.getAvailableSeats(showId).subscribe({
        next: (response) => {
          const regular = response.seats.filter(
            (s) => s.seatType === SeatType.REGULAR,
          );
          const premium = response.seats.filter(
            (s) => s.seatType === SeatType.PREMIUM,
          );
          const recliner = response.seats.filter(
            (s) => s.seatType === SeatType.RECLINER,
          );
          const vip = response.seats.filter((s) => s.seatType === SeatType.VIP);

          observer.next({ regular, premium, recliner, vip });
          observer.complete();
        },
        error: (err) => observer.error(err),
      });
    });
  }

  /**
   * Check if a show is sold out
   * @param showId Show ID
   * @returns Observable<boolean>
   */
  isShowSoldOut(showId: string): Observable<boolean> {
    return new Observable((observer) => {
      this.getShowById(showId).subscribe({
        next: (show) => {
          observer.next(show.availableSeats === 0);
          observer.complete();
        },
        error: (err) => observer.error(err),
      });
    });
  }
}
