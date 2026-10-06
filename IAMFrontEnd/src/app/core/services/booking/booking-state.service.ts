import { Injectable, computed, signal } from '@angular/core';
import { SeatInfo } from '../show/show.models';

export interface BookingSelection {
  showId: string;
  showName: string;
  seats: SeatInfo[];
  totalPrice: number;
}

const STORAGE_KEY = 'booking_selection';

/** Holds the show and seat selection shared between seat selection and booking pages. */
@Injectable({ providedIn: 'root' })
export class BookingStateService {
  private readonly _selection = signal<BookingSelection | null>(this.restore());

  readonly selection = this._selection.asReadonly();
  readonly showId = computed(() => this._selection()?.showId ?? '');
  readonly seats = computed(() => this._selection()?.seats ?? []);
  readonly seatIds = computed(() => this.seats().map((s) => s.seatId));
  readonly totalPrice = computed(() => this._selection()?.totalPrice ?? 0);
  readonly showName = computed(() => this._selection()?.showName ?? '');
  readonly hasSelection = computed(
    () => !!this.showId() && this.seats().length > 0,
  );

  setSelection(selection: BookingSelection): void {
    this._selection.set(selection);
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(selection));
  }

  clear(): void {
    this._selection.set(null);
    sessionStorage.removeItem(STORAGE_KEY);
  }

  private restore(): BookingSelection | null {
    const raw = sessionStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    try {
      const parsed = JSON.parse(raw) as BookingSelection;
      return parsed?.showId && Array.isArray(parsed.seats) ? parsed : null;
    } catch {
      return null;
    }
  }
}
