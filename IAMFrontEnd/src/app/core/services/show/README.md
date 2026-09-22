# Show Service Documentation

## Overview

The Show Service provides comprehensive functionality for managing movie shows/screenings in a theater booking system. It follows FAANG-level best practices with full TypeScript typing, error handling, and extensive test coverage.

## Architecture

```
src/app/core/services/show/
├── show.service.ts           # Main service implementation
├── show.service.spec.ts      # Comprehensive test suite
├── show.models.ts            # Type definitions and interfaces
└── README.md                 # This file
```

## Features

### Core Functionality
- ✅ Create new movie shows with validation
- ✅ Update existing shows (partial updates)
- ✅ Retrieve show details by ID
- ✅ List shows by movie with filters (date, city)
- ✅ List shows by theater with date filter
- ✅ Get seat availability with real-time status
- ✅ Comprehensive error handling (400, 404, 409)

### Advanced Features
- ✅ Date/time formatting utilities
- ✅ Seat price calculation based on type
- ✅ Seat grouping by status (available/locked/booked)
- ✅ Seat grouping by type (regular/premium/recliner/vip)
- ✅ Sold-out detection
- ✅ Today's shows convenience methods
- ✅ Type guards and constants

## API Endpoints

### 1. Create Show
```typescript
createShow(request: CreateShowRequest): Observable<ShowResponse>
```

**Example:**
```typescript
const request: CreateShowRequest = {
  movieId: '550e8400-e29b-41d4-a716-446655440000',
  screenId: '660e8400-e29b-41d4-a716-446655440001',
  showDateTime: '2026-09-15T18:30:00',
  basePrice: 250.00,
  showType: ShowType.EVENING
};

this.showService.createShow(request).subscribe({
  next: (show) => console.log('Show created:', show),
  error: (error) => this.handleError(error)
});
```

### 2. Update Show
```typescript
updateShow(showId: string, request: UpdateShowRequest): Observable<ShowResponse>
```

**Example:**
```typescript
const update: UpdateShowRequest = {
  showId: '770e8400-e29b-41d4-a716-446655440002',
  basePrice: 300.00,
  showType: ShowType.NIGHT
};

this.showService.updateShow(showId, update).subscribe({
  next: (show) => console.log('Show updated:', show),
  error: (error) => this.handleError(error)
});
```

### 3. Get Show by ID
```typescript
getShowById(showId: string): Observable<ShowResponse>
```

**Example:**
```typescript
this.showService.getShowById(showId).subscribe({
  next: (show) => this.displayShow(show),
  error: (error) => this.handleNotFound(error)
});
```

### 4. List Shows by Movie
```typescript
getShowsByMovie(movieId: string, date?: string, city?: string): Observable<ShowListResponse>
```

**Example:**
```typescript
// Get all shows for a movie
this.showService.getShowsByMovie(movieId).subscribe(shows => {
  console.log('All shows:', shows);
});

// Filter by date
this.showService.getShowsByMovie(movieId, '2026-09-15').subscribe(shows => {
  console.log('Shows on date:', shows);
});

// Filter by city
this.showService.getShowsByMovie(movieId, undefined, 'Mumbai').subscribe(shows => {
  console.log('Shows in city:', shows);
});

// Filter by both
this.showService.getShowsByMovie(movieId, '2026-09-15', 'Mumbai').subscribe(shows => {
  console.log('Shows on date in city:', shows);
});
```

### 5. List Shows by Theater
```typescript
getShowsByTheater(theaterId: string, date?: string): Observable<ShowListResponse>
```

**Example:**
```typescript
// Get all shows for a theater
this.showService.getShowsByTheater(theaterId).subscribe(shows => {
  console.log('Theater shows:', shows);
});

// Filter by date
this.showService.getShowsByTheater(theaterId, '2026-09-15').subscribe(shows => {
  console.log('Shows on date:', shows);
});
```

### 6. Get Available Seats
```typescript
getAvailableSeats(showId: string): Observable<AvailableSeatsResponse>
```

**Example:**
```typescript
this.showService.getAvailableSeats(showId).subscribe(response => {
  console.log('Total seats:', response.seats.length);
  console.log('Available:', response.totalAvailable);
  
  response.seats.forEach(seat => {
    console.log(`${seat.rowName}${seat.seatNumber}: ${seat.status} - ₹${seat.price}`);
  });
});
```

## Utility Methods

### Date Formatting
```typescript
// Convert Date to API format (ISO-8601)
const apiDateTime = this.showService.formatShowDateTime(new Date());
// Returns: "2026-09-15T18:30:00"

// Parse API date string to Date object
const date = this.showService.parseShowDateTime('2026-09-15T18:30:00');

// Format date for query parameters
const queryDate = this.showService.formatDateForQuery(new Date());
// Returns: "2026-09-15"
```

### Seat Price Calculation
```typescript
const basePrice = 250.00;

// Calculate prices for different seat types
const regularPrice = this.showService.calculateSeatPrice(basePrice, SeatType.REGULAR);    // 250.00
const premiumPrice = this.showService.calculateSeatPrice(basePrice, SeatType.PREMIUM);    // 375.00
const reclinerPrice = this.showService.calculateSeatPrice(basePrice, SeatType.RECLINER);  // 500.00
const vipPrice = this.showService.calculateSeatPrice(basePrice, SeatType.VIP);            // 625.00
```

## Convenience Methods

### Today's Shows
```typescript
// Get today's shows for a movie
this.showService.getTodayShowsByMovie(movieId, 'Mumbai').subscribe(shows => {
  console.log('Today\'s shows:', shows);
});

// Get today's shows for a theater
this.showService.getTodayShowsByTheater(theaterId).subscribe(shows => {
  console.log('Today\'s shows:', shows);
});
```

### Seats Grouped by Status
```typescript
this.showService.getSeatsGroupedByStatus(showId).subscribe(seats => {
  console.log('Available seats:', seats.available.length);
  console.log('Locked seats:', seats.locked.length);
  console.log('Booked seats:', seats.booked.length);
  console.log('Total available:', seats.totalAvailable);
});
```

### Seats Grouped by Type
```typescript
this.showService.getSeatsGroupedByType(showId).subscribe(seats => {
  console.log('Regular seats:', seats.regular.length);
  console.log('Premium seats:', seats.premium.length);
  console.log('Recliner seats:', seats.recliner.length);
  console.log('VIP seats:', seats.vip.length);
});
```

### Sold Out Check
```typescript
this.showService.isShowSoldOut(showId).subscribe(isSoldOut => {
  if (isSoldOut) {
    console.log('Show is sold out');
  }
});
```

## Error Handling

The service handles all API errors gracefully:

```typescript
this.showService.createShow(request).subscribe({
  next: (response) => {
    // Success handling
    console.log('Show created:', response);
  },
  error: (error) => {
    switch (error.status) {
      case 400:
        // Validation error
        this.toastService.error('Invalid input: ' + error.error.message);
        break;
      case 404:
        // Movie or Screen not found
        this.toastService.error('Movie or Screen not found');
        break;
      case 409:
        // Show timing conflict
        this.toastService.error('Show overlaps with existing show');
        break;
      default:
        this.toastService.error('An error occurred');
    }
  }
});
```

## Enums

### ShowType
```typescript
enum ShowType {
  MORNING = 'MORNING',    // 6 AM - 12 PM
  MATINEE = 'MATINEE',    // 12 PM - 5 PM
  EVENING = 'EVENING',    // 5 PM - 9 PM
  NIGHT = 'NIGHT'         // 9 PM - 12 AM
}
```

### SeatType
```typescript
enum SeatType {
  REGULAR = 'REGULAR',    // 1.0× base price
  PREMIUM = 'PREMIUM',    // 1.5× base price
  RECLINER = 'RECLINER',  // 2.0× base price
  VIP = 'VIP'             // 2.5× base price
}
```

### SeatStatus
```typescript
enum SeatStatus {
  AVAILABLE = 'AVAILABLE',  // Can be selected
  LOCKED = 'LOCKED',        // Temporarily held (10 min)
  BOOKED = 'BOOKED'         // Permanently reserved
}
```

## Type Safety

All responses and requests are fully typed:

```typescript
import { 
  ShowService, 
  CreateShowRequest, 
  ShowResponse, 
  ShowType,
  SeatType,
  SeatStatus 
} from '@core/services/show/show.service';
```

## Testing

The service includes comprehensive test coverage:

```bash
# Run tests
ng test --include='**/show.service.spec.ts'
```

Test coverage includes:
- ✅ All CRUD operations
- ✅ Query parameter handling
- ✅ Error scenarios (400, 404, 409)
- ✅ Utility method validation
- ✅ Convenience method functionality
- ✅ Date formatting
- ✅ Seat price calculations

## Configuration

Update `app-config.json` to configure API endpoints:

```json
{
  "show": {
    "createUrl": "http://localhost:8080/api/v1/shows",
    "updateUrl": "http://localhost:8080/api/v1/shows",
    "getByIdUrl": "http://localhost:8080/api/v1/shows",
    "getByMovieUrl": "http://localhost:8080/api/v1/shows/movie",
    "getByTheaterUrl": "http://localhost:8080/api/v1/shows/theater",
    "getSeatsUrl": "http://localhost:8080/api/v1/shows"
  }
}
```

## Best Practices

### 1. Always Use Type Safety
```typescript
// ✅ Good
const request: CreateShowRequest = { ... };
this.showService.createShow(request);

// ❌ Bad
const request = { ... };
this.showService.createShow(request as any);
```

### 2. Handle Errors Gracefully
```typescript
// ✅ Good
this.showService.getShowById(id).subscribe({
  next: (show) => this.handleShow(show),
  error: (error) => this.handleError(error)
});

// ❌ Bad
this.showService.getShowById(id).subscribe(show => {
  // No error handling
});
```

### 3. Use Date Utilities
```typescript
// ✅ Good
const dateTime = this.showService.formatShowDateTime(new Date());

// ❌ Bad
const dateTime = new Date().toISOString(); // Wrong format
```

### 4. Leverage Convenience Methods
```typescript
// ✅ Good
this.showService.getTodayShowsByMovie(movieId);

// ❌ Bad
const today = new Date().toISOString().split('T')[0];
this.showService.getShowsByMovie(movieId, today);
```

## Integration Example

Complete component example:

```typescript
import { Component, OnInit, inject } from '@angular/core';
import { ShowService, ShowType, CreateShowRequest } from '@core/services/show/show.service';
import { ToastService } from '@core/services/toast/toast.service';

@Component({
  selector: 'app-show-form',
  templateUrl: './show-form.component.html'
})
export class ShowFormComponent implements OnInit {
  private showService = inject(ShowService);
  private toastService = inject(ToastService);
  
  showTypes = Object.values(ShowType);
  
  createShow(formData: any) {
    const request: CreateShowRequest = {
      movieId: formData.movieId,
      screenId: formData.screenId,
      showDateTime: this.showService.formatShowDateTime(formData.dateTime),
      basePrice: formData.basePrice,
      showType: formData.showType
    };
    
    this.showService.createShow(request).subscribe({
      next: (show) => {
        this.toastService.success('Show created successfully');
        this.router.navigate(['/shows', show.showId]);
      },
      error: (error) => {
        if (error.status === 409) {
          this.toastService.error('Show timing conflicts with existing show');
        } else {
          this.toastService.error('Failed to create show');
        }
      }
    });
  }
}
```

## Performance Considerations

1. **Caching**: Consider implementing caching for frequently accessed shows
2. **Pagination**: For large show lists, implement pagination on the backend
3. **Debouncing**: Use debounce for search/filter inputs
4. **Lazy Loading**: Load seat maps only when needed
5. **Optimistic Updates**: Update UI immediately, rollback on error

## Future Enhancements

- [ ] WebSocket integration for real-time seat updates
- [ ] Show search with autocomplete
- [ ] Bulk show creation
- [ ] Show templates
- [ ] Analytics integration
- [ ] Price surge management
- [ ] Show cancellation workflow

## Support

For issues or questions:
1. Check the test file for usage examples
2. Review the models file for all available types
3. Consult the API documentation
4. Contact the backend team for API-related issues

## Version History

- **v1.0.0** (2026-08-31): Initial release
  - Complete CRUD operations
  - Advanced filtering
  - Seat management
  - Comprehensive testing
