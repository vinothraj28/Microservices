# Show Service Usage Examples

## Table of Contents

1. [Basic CRUD Operations](#basic-crud-operations)
2. [Movie Show Listing Component](#movie-show-listing-component)
3. [Theater Show Management](#theater-show-management)
4. [Seat Selection Component](#seat-selection-component)
5. [Show Creation Form](#show-creation-form)
6. [Real-world Integration Patterns](#real-world-integration-patterns)

---

## Basic CRUD Operations

### Create a Show

```typescript
import { Component, inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ShowService, ShowType, CreateShowRequest } from "@core/services/show";
import { ToastService } from "@core/services/toast/toast.service";

@Component({
  selector: "app-create-show",
  template: `
    <form [formGroup]="showForm" (ngSubmit)="onSubmit()">
      <input formControlName="movieId" placeholder="Movie ID" />
      <input formControlName="screenId" placeholder="Screen ID" />
      <input type="datetime-local" formControlName="showDateTime" />
      <input type="number" formControlName="basePrice" placeholder="Base Price" />
      <select formControlName="showType">
        <option *ngFor="let type of showTypes" [value]="type">{{ type }}</option>
      </select>
      <button type="submit" [disabled]="showForm.invalid">Create Show</button>
    </form>
  `,
})
export class CreateShowComponent {
  private showService = inject(ShowService);
  private toastService = inject(ToastService);
  private fb = inject(FormBuilder);

  showTypes = Object.values(ShowType);

  showForm: FormGroup = this.fb.group({
    movieId: ["", [Validators.required]],
    screenId: ["", [Validators.required]],
    showDateTime: ["", [Validators.required]],
    basePrice: ["", [Validators.required, Validators.min(0.01)]],
    showType: [ShowType.EVENING, [Validators.required]],
  });

  onSubmit(): void {
    if (this.showForm.valid) {
      const formValue = this.showForm.value;

      // Convert datetime-local to ISO format
      const dateTime = new Date(formValue.showDateTime);

      const request: CreateShowRequest = {
        movieId: formValue.movieId,
        screenId: formValue.screenId,
        showDateTime: this.showService.formatShowDateTime(dateTime),
        basePrice: parseFloat(formValue.basePrice),
        showType: formValue.showType,
      };

      this.showService.createShow(request).subscribe({
        next: (show) => {
          this.toastService.success(`Show created successfully: ${show.showId}`);
          this.showForm.reset();
        },
        error: (error) => this.handleError(error),
      });
    }
  }

  private handleError(error: any): void {
    switch (error.status) {
      case 400:
        this.toastService.error("Invalid input: " + error.error.message);
        break;
      case 404:
        this.toastService.error("Movie or Screen not found");
        break;
      case 409:
        this.toastService.error("Show timing conflicts with existing show");
        break;
      default:
        this.toastService.error("Failed to create show");
    }
  }
}
```

---

## Movie Show Listing Component

```typescript
import { Component, OnInit, inject } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ShowService, ShowResponse, ShowListResponse } from "@core/services/show";
import { Observable } from "rxjs";

@Component({
  selector: "app-movie-shows",
  template: `
    <div class="movie-shows">
      <h2>Available Shows</h2>

      <!-- Filters -->
      <div class="filters">
        <input type="date" [(ngModel)]="selectedDate" (change)="loadShows()" />
        <input type="text" [(ngModel)]="selectedCity" placeholder="City" (change)="loadShows()" />
        <button (click)="clearFilters()">Clear Filters</button>
      </div>

      <!-- Show List -->
      <div class="shows-grid" *ngIf="shows$ | async as showsResponse">
        <div *ngIf="showsResponse.shows.length === 0" class="no-shows">No shows available for the selected filters</div>

        <div class="show-card" *ngFor="let show of showsResponse.shows">
          <div class="show-info">
            <h3>{{ show.movie.title }}</h3>
            <p class="show-time">{{ formatShowTime(show.showDateTime) }}</p>
            <p class="show-type">{{ show.showType }}</p>
            <p class="theater">{{ show.screen.name }}</p>
            <p class="price">₹{{ show.basePrice }}</p>
            <p class="availability" [class.sold-out]="show.availableSeats === 0">{{ show.availableSeats }} seats available</p>
          </div>
          <button [disabled]="show.availableSeats === 0" (click)="selectShow(show)">
            {{ show.availableSeats === 0 ? "Sold Out" : "Book Now" }}
          </button>
        </div>

        <div class="total-count">Total Shows: {{ showsResponse.totalCount }}</div>
      </div>
    </div>
  `,
  styles: [
    `
      .movie-shows {
        padding: 20px;
      }

      .filters {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
      }

      .shows-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
      }

      .show-card {
        border: 1px solid #ddd;
        padding: 15px;
        border-radius: 8px;
        transition: transform 0.2s;
      }

      .show-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      }

      .sold-out {
        color: red;
        font-weight: bold;
      }
    `,
  ],
})
export class MovieShowsComponent implements OnInit {
  private showService = inject(ShowService);
  private route = inject(ActivatedRoute);

  movieId!: string;
  selectedDate?: string;
  selectedCity?: string;
  shows$!: Observable<ShowListResponse>;

  ngOnInit(): void {
    this.movieId = this.route.snapshot.paramMap.get("movieId")!;
    this.loadShows();
  }

  loadShows(): void {
    this.shows$ = this.showService.getShowsByMovie(this.movieId, this.selectedDate, this.selectedCity);
  }

  clearFilters(): void {
    this.selectedDate = undefined;
    this.selectedCity = undefined;
    this.loadShows();
  }

  formatShowTime(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toLocaleString("en-US", {
      weekday: "short",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  selectShow(show: ShowResponse): void {
    // Navigate to seat selection
    this.router.navigate(["/booking", show.showId]);
  }
}
```

---

## Theater Show Management

```typescript
import { Component, OnInit, inject } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ShowService, ShowResponse } from "@core/services/show";
import { forkJoin, Observable } from "rxjs";
import { map } from "rxjs/operators";

interface ShowsByDate {
  date: string;
  shows: ShowResponse[];
}

@Component({
  selector: "app-theater-shows",
  template: `
    <div class="theater-shows">
      <h2>Theater Schedule</h2>

      <!-- Date Selector -->
      <div class="date-selector">
        <button *ngFor="let date of next7Days" (click)="selectDate(date)" [class.active]="selectedDate === date">
          {{ formatDateDisplay(date) }}
        </button>
      </div>

      <!-- Shows by Screen -->
      <div class="shows-by-screen" *ngIf="shows$ | async as shows">
        <div *ngFor="let showGroup of groupShowsByScreen(shows.shows)" class="screen-group">
          <h3>{{ showGroup.screenName }}</h3>
          <div class="show-timeline">
            <div *ngFor="let show of showGroup.shows" class="show-item" [class.sold-out]="show.availableSeats === 0">
              <div class="movie-title">{{ show.movie.title }}</div>
              <div class="show-time">{{ getTimeOnly(show.showDateTime) }}</div>
              <div class="show-type-badge" [attr.data-type]="show.showType">
                {{ show.showType }}
              </div>
              <div class="availability">{{ show.availableSeats }}/{{ show.screen.totalSeats }}</div>
              <div class="actions">
                <button (click)="editShow(show)">Edit</button>
                <button (click)="viewSeats(show)">View Seats</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .date-selector {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
        overflow-x: auto;
      }

      .date-selector button {
        padding: 10px 20px;
        border: 1px solid #ddd;
        background: white;
        cursor: pointer;
        white-space: nowrap;
      }

      .date-selector button.active {
        background: #007bff;
        color: white;
      }

      .screen-group {
        margin-bottom: 30px;
        border: 1px solid #ddd;
        padding: 20px;
        border-radius: 8px;
      }

      .show-timeline {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
      }

      .show-item {
        border: 1px solid #ddd;
        padding: 15px;
        border-radius: 4px;
        min-width: 200px;
      }

      .show-type-badge[data-type="MORNING"] {
        background: #ffeaa7;
      }

      .show-type-badge[data-type="MATINEE"] {
        background: #74b9ff;
      }

      .show-type-badge[data-type="EVENING"] {
        background: #fd79a8;
      }

      .show-type-badge[data-type="NIGHT"] {
        background: #636e72;
        color: white;
      }
    `,
  ],
})
export class TheaterShowsComponent implements OnInit {
  private showService = inject(ShowService);
  private route = inject(ActivatedRoute);

  theaterId!: string;
  selectedDate!: string;
  next7Days: string[] = [];
  shows$!: Observable<any>;

  ngOnInit(): void {
    this.theaterId = this.route.snapshot.paramMap.get("theaterId")!;
    this.generateNext7Days();
    this.selectDate(this.next7Days[0]);
  }

  private generateNext7Days(): void {
    this.next7Days = Array.from({ length: 7 }, (_, i) => {
      const date = new Date();
      date.setDate(date.getDate() + i);
      return this.showService.formatDateForQuery(date);
    });
  }

  selectDate(date: string): void {
    this.selectedDate = date;
    this.shows$ = this.showService.getShowsByTheater(this.theaterId, date);
  }

  formatDateDisplay(date: string): string {
    const d = new Date(date);
    return d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
  }

  getTimeOnly(dateTime: string): string {
    return new Date(dateTime).toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  groupShowsByScreen(shows: ShowResponse[]): any[] {
    const grouped = shows.reduce((acc, show) => {
      const screenId = show.screenId;
      if (!acc[screenId]) {
        acc[screenId] = {
          screenName: show.screen.name,
          shows: [],
        };
      }
      acc[screenId].shows.push(show);
      return acc;
    }, {} as any);

    // Sort shows by time within each screen
    Object.values(grouped).forEach((group: any) => {
      group.shows.sort((a: ShowResponse, b: ShowResponse) => a.showDateTime.localeCompare(b.showDateTime));
    });

    return Object.values(grouped);
  }

  editShow(show: ShowResponse): void {
    // Navigate to edit form
  }

  viewSeats(show: ShowResponse): void {
    // Navigate to seat view
  }
}
```

---

## Seat Selection Component

```typescript
import { Component, OnInit, inject } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ShowService, SeatInfo, SeatStatus, SeatType } from "@core/services/show";
import { Observable } from "rxjs";

@Component({
  selector: "app-seat-selection",
  template: `
    <div class="seat-selection" *ngIf="seatsData$ | async as seatsData">
      <h2>Select Your Seats</h2>

      <!-- Show Info -->
      <div class="show-info">
        <p>{{ showInfo?.movie.title }}</p>
        <p>{{ showInfo?.screen.name }}</p>
        <p>{{ formatDateTime(showInfo?.showDateTime) }}</p>
      </div>

      <!-- Seat Legend -->
      <div class="seat-legend">
        <div class="legend-item">
          <span class="seat-box available"></span>
          <span>Available</span>
        </div>
        <div class="legend-item">
          <span class="seat-box selected"></span>
          <span>Selected</span>
        </div>
        <div class="legend-item">
          <span class="seat-box booked"></span>
          <span>Booked</span>
        </div>
        <div class="legend-item">
          <span class="seat-box locked"></span>
          <span>Locked</span>
        </div>
      </div>

      <!-- Seat Map -->
      <div class="seat-map">
        <div class="screen-indicator">SCREEN</div>

        <div *ngFor="let row of getSeatRows(seatsData.seats)" class="seat-row">
          <div class="row-label">{{ row.rowName }}</div>
          <div class="seats">
            <div *ngFor="let seat of row.seats" class="seat" [class.available]="seat.status === 'AVAILABLE'" [class.booked]="seat.status === 'BOOKED'" [class.locked]="seat.status === 'LOCKED'" [class.selected]="isSelected(seat)" [class.premium]="seat.seatType === 'PREMIUM'" [class.recliner]="seat.seatType === 'RECLINER'" [class.vip]="seat.seatType === 'VIP'" [attr.disabled]="seat.status !== 'AVAILABLE'" (click)="toggleSeat(seat)">
              <span class="seat-number">{{ seat.seatNumber }}</span>
              <span class="seat-price">₹{{ seat.price }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Booking Summary -->
      <div class="booking-summary" *ngIf="selectedSeats.length > 0">
        <h3>Booking Summary</h3>
        <div class="selected-seats-list">
          <div *ngFor="let seat of selectedSeats" class="selected-seat-item">
            {{ seat.rowName }}{{ seat.seatNumber }} - {{ seat.seatType }} - ₹{{ seat.price }}
            <button (click)="toggleSeat(seat)">×</button>
          </div>
        </div>
        <div class="total"><strong>Total Amount:</strong> ₹{{ getTotalAmount() }}</div>
        <button class="proceed-btn" (click)="proceedToPayment()">Proceed to Payment</button>
      </div>
    </div>
  `,
  styles: [
    `
      .seat-selection {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
      }

      .seat-legend {
        display: flex;
        gap: 20px;
        margin: 20px 0;
        justify-content: center;
      }

      .legend-item {
        display: flex;
        align-items: center;
        gap: 5px;
      }

      .seat-box {
        width: 20px;
        height: 20px;
        border: 1px solid #ddd;
        display: inline-block;
      }

      .seat-box.available {
        background: #4caf50;
      }
      .seat-box.selected {
        background: #ff9800;
      }
      .seat-box.booked {
        background: #f44336;
      }
      .seat-box.locked {
        background: #9e9e9e;
      }

      .screen-indicator {
        background: #333;
        color: white;
        text-align: center;
        padding: 10px;
        margin-bottom: 30px;
        border-radius: 4px;
      }

      .seat-row {
        display: flex;
        justify-content: center;
        margin: 10px 0;
        align-items: center;
      }

      .row-label {
        width: 30px;
        font-weight: bold;
      }

      .seats {
        display: flex;
        gap: 5px;
      }

      .seat {
        width: 40px;
        height: 40px;
        border: 1px solid #ddd;
        border-radius: 4px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        font-size: 10px;
      }

      .seat.available {
        background: #4caf50;
        color: white;
      }
      .seat.selected {
        background: #ff9800;
        color: white;
      }
      .seat.booked {
        background: #f44336;
        color: white;
        cursor: not-allowed;
      }
      .seat.locked {
        background: #9e9e9e;
        color: white;
        cursor: not-allowed;
      }

      .seat.premium {
        border: 2px solid gold;
      }
      .seat.recliner {
        border: 2px solid purple;
      }
      .seat.vip {
        border: 2px solid red;
      }

      .booking-summary {
        margin-top: 30px;
        border: 1px solid #ddd;
        padding: 20px;
        border-radius: 8px;
      }

      .proceed-btn {
        width: 100%;
        padding: 15px;
        background: #4caf50;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
      }
    `,
  ],
})
export class SeatSelectionComponent implements OnInit {
  private showService = inject(ShowService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  showId!: string;
  showInfo: any;
  seatsData$!: Observable<any>;
  selectedSeats: SeatInfo[] = [];

  ngOnInit(): void {
    this.showId = this.route.snapshot.paramMap.get("showId")!;
    this.loadShowAndSeats();
  }

  private loadShowAndSeats(): void {
    // Load show details
    this.showService.getShowById(this.showId).subscribe((show) => {
      this.showInfo = show;
    });

    // Load seats
    this.seatsData$ = this.showService.getAvailableSeats(this.showId);
  }

  getSeatRows(seats: SeatInfo[]): any[] {
    const rowMap = seats.reduce((acc, seat) => {
      if (!acc[seat.rowName]) {
        acc[seat.rowName] = {
          rowName: seat.rowName,
          seats: [],
        };
      }
      acc[seat.rowName].seats.push(seat);
      return acc;
    }, {} as any);

    // Sort seats within each row
    Object.values(rowMap).forEach((row: any) => {
      row.seats.sort((a: SeatInfo, b: SeatInfo) => a.seatNumber - b.seatNumber);
    });

    return Object.values(rowMap);
  }

  isSelected(seat: SeatInfo): boolean {
    return this.selectedSeats.some((s) => s.seatId === seat.seatId);
  }

  toggleSeat(seat: SeatInfo): void {
    if (seat.status !== "AVAILABLE") return;

    const index = this.selectedSeats.findIndex((s) => s.seatId === seat.seatId);
    if (index > -1) {
      this.selectedSeats.splice(index, 1);
    } else {
      if (this.selectedSeats.length < 10) {
        // Max 10 seats
        this.selectedSeats.push(seat);
      } else {
        alert("Maximum 10 seats can be selected");
      }
    }
  }

  getTotalAmount(): number {
    return this.selectedSeats.reduce((sum, seat) => sum + seat.price, 0);
  }

  formatDateTime(dateTime: string | undefined): string {
    if (!dateTime) return "";
    return new Date(dateTime).toLocaleString();
  }

  proceedToPayment(): void {
    // Navigate to payment with selected seats
    this.router.navigate(["/payment"], {
      state: {
        showId: this.showId,
        selectedSeats: this.selectedSeats,
        totalAmount: this.getTotalAmount(),
      },
    });
  }
}
```

---

## Real-world Integration Patterns

### Pattern 1: Loading State Management

```typescript
import { Component, OnInit } from "@angular/core";
import { ShowService } from "@core/services/show";
import { BehaviorSubject, finalize } from "rxjs";

@Component({
  selector: "app-show-list",
  template: `
    <div class="loading" *ngIf="loading$ | async">Loading...</div>
    <div class="shows" *ngIf="!(loading$ | async)">
      <!-- Show content -->
    </div>
  `,
})
export class ShowListComponent implements OnInit {
  loading$ = new BehaviorSubject<boolean>(false);

  constructor(private showService: ShowService) {}

  ngOnInit(): void {
    this.loading$.next(true);
    this.showService
      .getShowsByMovie("movie-id")
      .pipe(finalize(() => this.loading$.next(false)))
      .subscribe((shows) => {
        // Handle shows
      });
  }
}
```

### Pattern 2: Error Boundary

```typescript
import { Component } from "@angular/core";
import { ShowService } from "@core/services/show";
import { catchError, of } from "rxjs";

@Component({
  selector: "app-show-container",
  template: `
    <div *ngIf="error" class="error">{{ error }}</div>
    <div *ngIf="!error">
      <!-- Content -->
    </div>
  `,
})
export class ShowContainerComponent {
  error: string | null = null;

  constructor(private showService: ShowService) {}

  loadShows(movieId: string): void {
    this.showService
      .getShowsByMovie(movieId)
      .pipe(
        catchError((error) => {
          this.error = this.getErrorMessage(error);
          return of({ shows: [], totalCount: 0 });
        }),
      )
      .subscribe((shows) => {
        // Handle shows
      });
  }

  private getErrorMessage(error: any): string {
    if (error.status === 404) return "Shows not found";
    if (error.status === 500) return "Server error occurred";
    return "An unexpected error occurred";
  }
}
```

### Pattern 3: Reactive Search

```typescript
import { Component, OnInit } from "@angular/core";
import { FormControl } from "@angular/forms";
import { ShowService } from "@core/services/show";
import { debounceTime, distinctUntilChanged, switchMap } from "rxjs/operators";

@Component({
  selector: "app-show-search",
  template: `
    <input [formControl]="searchControl" placeholder="Search city..." />
    <div *ngFor="let show of searchResults$ | async">
      {{ show.movie.title }}
    </div>
  `,
})
export class ShowSearchComponent implements OnInit {
  searchControl = new FormControl("");
  searchResults$!: Observable<any>;

  constructor(
    private showService: ShowService,
    @Inject("MOVIE_ID") private movieId: string,
  ) {}

  ngOnInit(): void {
    this.searchResults$ = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((city) => (city ? this.showService.getShowsByMovie(this.movieId, undefined, city) : of({ shows: [], totalCount: 0 }))),
      map((response) => response.shows),
    );
  }
}
```

---

These examples demonstrate production-ready patterns following Angular best practices with proper TypeScript typing, error handling, and reactive programming patterns.
