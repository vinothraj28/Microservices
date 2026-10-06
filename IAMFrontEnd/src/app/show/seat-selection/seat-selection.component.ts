import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  ShowService,
  SeatInfo,
  SeatType,
  SeatStatus,
  ShowResponse,
} from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';

@Component({
  selector: 'app-seat-selection',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.css'],
})
export class SeatSelectionComponent implements OnInit {
  // Dependencies
  protected readonly showService = inject(ShowService);
  protected readonly toastService = inject(ToastService);
  protected readonly route = inject(ActivatedRoute);
  protected readonly router = inject(Router);

  // State
  protected readonly showId = signal<string>('');
  protected readonly show = signal<ShowResponse | null>(null);
  protected readonly seats = signal<SeatInfo[]>([]);
  protected readonly selectedSeats = signal<Set<string>>(new Set());
  protected readonly isLoading = signal(false);
  protected readonly error = signal<string | null>(null);

  // Enum references for template
  protected readonly SeatType = SeatType;
  protected readonly SeatStatus = SeatStatus;
  protected readonly Array = Array;

  // Computed values
  protected get selectedSeatDetails(): SeatInfo[] {
    const selectedSeatIds = this.selectedSeats();
    console.log('Selected seat IDs:', selectedSeatIds);
    console.log('All seats:', this.seats());
    console.log(
      'Selected seat details:',
      this.seats().filter((seat) => selectedSeatIds.has(seat.seatId)),
    );
    return this.seats().filter((seat) => selectedSeatIds.has(seat.seatId));
  }

  protected get totalPrice(): number {
    const basePrice = this.show()?.basePrice || 0;

    return this.selectedSeatDetails.reduce(
      (total, seat) =>
        total +
        this.showService.calculateSeatPrice(
          basePrice,
          seat.seatType as SeatType,
        ),
      0,
    );
  }

  protected get seatsGroupedByStatus() {
    const seats = this.seats();
    return {
      available: seats.filter((s) => s.status === SeatStatus.AVAILABLE),
      locked: seats.filter((s) => s.status === SeatStatus.LOCKED),
      booked: seats.filter((s) => s.status === SeatStatus.BOOKED),
      totalAvailable: seats.filter((s) => s.status === SeatStatus.AVAILABLE)
        .length,
    };
  }

  protected get seatsGroupedByType() {
    const seats = this.seats();
    return {
      regular: seats.filter((s) => s.seatType === SeatType.REGULAR),
      premium: seats.filter((s) => s.seatType === SeatType.PREMIUM),
      recliner: seats.filter((s) => s.seatType === SeatType.RECLINER),
      vip: seats.filter((s) => s.seatType === SeatType.VIP),
    };
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const id = params['id'];
      if (id) {
        this.showId.set(id);
        this.loadShowAndSeats(id);
      }
    });
  }

  protected loadShowAndSeats(showId: string): void {
    this.isLoading.set(true);
    this.error.set(null);

    // Load show details
    this.showService.getShowById(showId).subscribe({
      next: (show) => {
        this.show.set(show);

        // Load seats
        this.showService.getAvailableSeats(showId).subscribe({
          next: (response) => {
            this.seats.set(response.seats);
            this.isLoading.set(false);
          },
          error: (err) => {
            this.error.set('Failed to load seats');
            this.isLoading.set(false);
            console.error('Error loading seats:', err);
          },
        });
      },
      error: (err) => {
        this.error.set('Failed to load show details');
        this.isLoading.set(false);
        console.error('Error loading show:', err);
      },
    });
  }

  protected toggleSeatSelection(seat: SeatInfo): void {
    if (seat.status !== SeatStatus.AVAILABLE) {
      this.toastService.setToast('This seat is not available', 'error');
      return;
    }

    const selectedSeats = new Set(this.selectedSeats());
    const seatId = seat.seatId;

    if (selectedSeats.has(seatId)) {
      selectedSeats.delete(seatId);
    } else {
      if (selectedSeats.size >= 10) {
        this.toastService.setToast('You can select maximum 10 seats', 'error');
        return;
      }
      selectedSeats.add(seatId);
    }

    this.selectedSeats.set(selectedSeats);
  }

  protected isSeatSelected(seat: SeatInfo): boolean {
    return this.selectedSeats().has(seat.seatId);
  }

  protected getSeatPrice(seat: SeatInfo): number {
    const basePrice = this.show()?.basePrice || 0;
    return this.showService.calculateSeatPrice(
      basePrice,
      seat.seatType as SeatType,
    );
  }

  protected getSeatTypeLabel(type: SeatType): string {
    const labels: Record<SeatType, string> = {
      [SeatType.REGULAR]: 'Regular',
      [SeatType.PREMIUM]: 'Premium',
      [SeatType.RECLINER]: 'Recliner',
      [SeatType.VIP]: 'VIP',
    };
    return labels[type];
  }

  protected getSeatTypeMultiplier(type: SeatType): string {
    const multipliers: Record<SeatType, string> = {
      [SeatType.REGULAR]: '1.0x',
      [SeatType.PREMIUM]: '1.5x',
      [SeatType.RECLINER]: '2.0x',
      [SeatType.VIP]: '2.5x',
    };
    return multipliers[type];
  }

  protected clearSelection(): void {
    this.selectedSeats.set(new Set());
  }

  protected proceedToBooking(): void {
    if (this.selectedSeats().size === 0) {
      this.toastService.setToast('Please select at least one seat', 'error');
      return;
    }

    // In a real app, you would navigate to booking/payment page
    this.toastService.setToast(
      `Proceeding to book ${this.selectedSeats().size} seats for ₹${this.totalPrice}`,
      'success',
    );
  }

  protected goBack(): void {
    this.router.navigate(['/base/show/list']);
  }

  protected formatShowTime(showDateTime: string): string {
    return this.showService.formatShowDateTime(new Date(showDateTime));
  }

  // Group seats by rows for grid layout
  protected get seatsGroupedByRow(): Map<string, SeatInfo[]> {
    const grouped = new Map<string, SeatInfo[]>();

    this.seats().forEach((seat) => {
      // Use rowName from the seat
      const row = seat.rowName;
      if (!grouped.has(row)) {
        grouped.set(row, []);
      }
      grouped.get(row)!.push(seat);
    });

    // Sort seats in each row by seat number
    grouped.forEach((seats, row) => {
      seats.sort((a, b) => a.seatNumber - b.seatNumber);
    });

    return grouped;
  }

  protected get rows(): string[] {
    return Array.from(this.seatsGroupedByRow.keys()).sort();
  }
}
