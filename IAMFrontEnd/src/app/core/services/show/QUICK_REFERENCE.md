# Show Service Quick Reference

## 🚀 Quick Start

```typescript
import { ShowService, ShowType } from "@core/services/show";

export class MyComponent {
  private showService = inject(ShowService);
}
```

---

## 📚 Common Operations

### Create a Show

```typescript
this.showService
  .createShow({
    movieId: "uuid",
    screenId: "uuid",
    showDateTime: "2026-09-15T18:30:00",
    basePrice: 250,
    showType: ShowType.EVENING,
  })
  .subscribe();
```

### Get Shows for a Movie

```typescript
// All shows
this.showService.getShowsByMovie(movieId).subscribe();

// With date filter
this.showService.getShowsByMovie(movieId, "2026-09-15").subscribe();

// With city filter
this.showService.getShowsByMovie(movieId, undefined, "Mumbai").subscribe();

// With both filters
this.showService.getShowsByMovie(movieId, "2026-09-15", "Mumbai").subscribe();
```

### Get Shows for a Theater

```typescript
// All shows
this.showService.getShowsByTheater(theaterId).subscribe();

// Today's shows
this.showService.getTodayShowsByTheater(theaterId).subscribe();
```

### Get Seat Availability

```typescript
this.showService.getAvailableSeats(showId).subscribe((response) => {
  console.log("Total available:", response.totalAvailable);
  response.seats.forEach((seat) => {
    console.log(`${seat.rowName}${seat.seatNumber}: ₹${seat.price}`);
  });
});
```

### Update a Show

```typescript
this.showService
  .updateShow(showId, {
    showId: showId,
    basePrice: 300,
    showType: ShowType.NIGHT,
  })
  .subscribe();
```

---

## 🛠️ Utilities

### Date Formatting

```typescript
// Date to API format (ISO-8601)
const apiDate = this.showService.formatShowDateTime(new Date());
// → "2026-09-15T18:30:00"

// String to Date
const date = this.showService.parseShowDateTime("2026-09-15T18:30:00");

// Date for query params
const queryDate = this.showService.formatDateForQuery(new Date());
// → "2026-09-15"
```

### Seat Pricing

```typescript
const basePrice = 250;

this.showService.calculateSeatPrice(basePrice, SeatType.REGULAR); // 250
this.showService.calculateSeatPrice(basePrice, SeatType.PREMIUM); // 375
this.showService.calculateSeatPrice(basePrice, SeatType.RECLINER); // 500
this.showService.calculateSeatPrice(basePrice, SeatType.VIP); // 625
```

### Convenience Methods

```typescript
// Today's shows
this.showService.getTodayShowsByMovie(movieId, "Mumbai").subscribe();
this.showService.getTodayShowsByTheater(theaterId).subscribe();

// Check if sold out
this.showService.isShowSoldOut(showId).subscribe((isSoldOut) => {
  if (isSoldOut) console.log("Sold out!");
});

// Group seats by status
this.showService.getSeatsGroupedByStatus(showId).subscribe((seats) => {
  console.log("Available:", seats.available.length);
  console.log("Locked:", seats.locked.length);
  console.log("Booked:", seats.booked.length);
});

// Group seats by type
this.showService.getSeatsGroupedByType(showId).subscribe((seats) => {
  console.log("Regular:", seats.regular.length);
  console.log("Premium:", seats.premium.length);
});
```

---

## 📋 Enums

### ShowType

```typescript
ShowType.MORNING; // 6 AM - 12 PM
ShowType.MATINEE; // 12 PM - 5 PM
ShowType.EVENING; // 5 PM - 9 PM
ShowType.NIGHT; // 9 PM - 12 AM
```

### SeatType

```typescript
SeatType.REGULAR; // 1.0× base price
SeatType.PREMIUM; // 1.5× base price
SeatType.RECLINER; // 2.0× base price
SeatType.VIP; // 2.5× base price
```

### SeatStatus

```typescript
SeatStatus.AVAILABLE; // Can be booked
SeatStatus.LOCKED; // Temporarily held (10 min)
SeatStatus.BOOKED; // Permanently reserved
```

---

## ⚠️ Error Handling

```typescript
this.showService.createShow(request).subscribe({
  next: (show) => {
    // Success
  },
  error: (error) => {
    switch (error.status) {
      case 400:
        // Validation error
        console.error("Bad request:", error.error.message);
        break;
      case 404:
        // Not found
        console.error("Movie or Screen not found");
        break;
      case 409:
        // Conflict
        console.error("Show time conflict");
        break;
      default:
        console.error("Unknown error");
    }
  },
});
```

---

## 📦 Interfaces

### CreateShowRequest

```typescript
{
  movieId: string; // UUID
  screenId: string; // UUID
  showDateTime: string; // ISO-8601 (YYYY-MM-DDTHH:mm:ss)
  basePrice: number; // min: 0.01
  showType: ShowType; // MORNING | MATINEE | EVENING | NIGHT
}
```

### ShowResponse

```typescript
{
  showId: string;
  movieId: string;
  screenId: string;
  theaterId: string;
  movie: MovieResponse;
  screen: ScreenResponse;
  showDateTime: string;
  basePrice: number;
  showType: string;
  availableSeats: number;
  createdAt: string;
  updatedAt: string;
}
```

### SeatInfo

```typescript
{
  seatId: string;
  rowName: string;
  seatNumber: number;
  seatType: string;
  price: number;
  status: string;
  lockedUntil: string | null;
}
```

---

## 🎯 Best Practices

✅ Always handle errors in subscribe  
✅ Use typed interfaces, avoid `any`  
✅ Use utility methods for date formatting  
✅ Unsubscribe in ngOnDestroy if not using async pipe  
✅ Use convenience methods when available  
✅ Leverage type guards for runtime checks

---

## 🔗 Related Services

```typescript
import { MovieService } from "@core/services/movie";
import { TheaterService } from "@core/services/theater";
import { ScreenService } from "@core/services/screen";
import { ShowService } from "@core/services/show";
```

---

## 📖 Full Documentation

- **README.md** - Complete API documentation
- **USAGE_EXAMPLES.md** - Real-world component examples
- **IMPLEMENTATION_SUMMARY.md** - Architecture and design
- **show.models.ts** - All type definitions

---

## 🎨 Component Template Example

```typescript
@Component({
  selector: "app-show-list",
  template: `
    <div *ngFor="let show of shows$ | async">
      <h3>{{ show.movie.title }}</h3>
      <p>{{ formatTime(show.showDateTime) }}</p>
      <p>{{ show.showType }} - ₹{{ show.basePrice }}</p>
      <p>{{ show.availableSeats }} seats available</p>
      <button [disabled]="show.availableSeats === 0">Book Now</button>
    </div>
  `,
})
export class ShowListComponent {
  private showService = inject(ShowService);
  shows$ = this.showService.getShowsByMovie("movie-id");

  formatTime(dateTime: string): string {
    return new Date(dateTime).toLocaleString();
  }
}
```

---

## 🚨 Common Pitfalls

❌ Forgetting to subscribe to Observables  
❌ Using wrong date format (use ISO-8601)  
❌ Not handling errors  
❌ Not unsubscribing (memory leaks)  
❌ Hardcoding seat prices (use calculateSeatPrice)

---

## 💡 Pro Tips

1. Use `async` pipe to auto-unsubscribe
2. Use convenience methods for common tasks
3. Leverage RxJS operators (map, filter, catchError)
4. Cache frequently accessed data
5. Use loading states for better UX
6. Implement optimistic UI updates

---

**Need help?** Check the USAGE_EXAMPLES.md for complete component implementations!
