# Show Service API Test Examples

## Prerequisites
- Movie Service running on port 8082 (gRPC: 9091)
- Gateway running on port 8080
- PostgreSQL database "MovieService" accessible
- At least one Movie and Screen/Theater created

## 1. Create a Show

```bash
curl -X POST http://localhost:8080/api/v1/shows \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": "123e4567-e89b-12d3-a456-426614174000",
    "screenId": "123e4567-e89b-12d3-a456-426614174001",
    "showDateTime": "2024-12-25T18:30:00",
    "basePrice": 250.00,
    "showType": "EVENING"
  }'
```

**Expected Response (201 Created):**
```json
{
  "showId": "generated-uuid",
  "movieId": "123e4567-e89b-12d3-a456-426614174000",
  "screenId": "123e4567-e89b-12d3-a456-426614174001",
  "theaterId": "theater-uuid",
  "movie": { /* complete movie details */ },
  "screen": { /* complete screen details */ },
  "showDateTime": "2024-12-25T18:30:00",
  "basePrice": 250.00,
  "showType": "EVENING",
  "availableSeats": 100,
  "createdAt": "2024-08-31T17:15:00",
  "updatedAt": "2024-08-31T17:15:00"
}
```

## 2. Get Show by ID

```bash
curl -X GET http://localhost:8080/api/v1/shows/{showId}
```

## 3. Update Show

```bash
curl -X PUT http://localhost:8080/api/v1/shows/{showId} \
  -H "Content-Type: application/json" \
  -d '{
    "showId": "{showId}",
    "basePrice": 300.00,
    "showType": "NIGHT"
  }'
```

## 4. List Shows by Movie

```bash
# All shows for a movie
curl -X GET http://localhost:8080/api/v1/shows/movie/{movieId}

# Shows for a specific date
curl -X GET "http://localhost:8080/api/v1/shows/movie/{movieId}?date=2024-12-25"

# Shows in a specific city on a date
curl -X GET "http://localhost:8080/api/v1/shows/movie/{movieId}?date=2024-12-25&city=Mumbai"
```

**Expected Response (200 OK):**
```json
{
  "shows": [
    {
      "showId": "uuid-1",
      "movieId": "movie-uuid",
      "showDateTime": "2024-12-25T10:00:00",
      "basePrice": 200.00,
      "showType": "MORNING",
      "availableSeats": 95
    },
    {
      "showId": "uuid-2",
      "movieId": "movie-uuid",
      "showDateTime": "2024-12-25T18:30:00",
      "basePrice": 250.00,
      "showType": "EVENING",
      "availableSeats": 100
    }
  ],
  "totalCount": 2
}
```

## 5. List Shows by Theater

```bash
# All shows for a theater
curl -X GET http://localhost:8080/api/v1/shows/theater/{theaterId}

# Shows for a specific date
curl -X GET "http://localhost:8080/api/v1/shows/theater/{theaterId}?date=2024-12-25"
```

## 6. Get Available Seats

```bash
curl -X GET http://localhost:8080/api/v1/shows/{showId}/seats
```

**Expected Response (200 OK):**
```json
{
  "showId": "show-uuid",
  "totalAvailable": 95,
  "seats": [
    {
      "seatId": "seat-uuid-1",
      "rowName": "A",
      "seatNumber": 1,
      "seatType": "REGULAR",
      "price": 250.00,
      "status": "AVAILABLE",
      "lockedUntil": null
    },
    {
      "seatId": "seat-uuid-2",
      "rowName": "A",
      "seatNumber": 2,
      "seatType": "REGULAR",
      "price": 250.00,
      "status": "BOOKED",
      "lockedUntil": null
    },
    {
      "seatId": "seat-uuid-3",
      "rowName": "A",
      "seatNumber": 3,
      "seatType": "PREMIUM",
      "price": 375.00,
      "status": "LOCKED",
      "lockedUntil": "2024-08-31T17:25:00"
    }
  ]
}
```

## Error Responses

### 400 Bad Request - Validation Failed
```json
{
  "timestamp": "2024-08-31T17:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Show date time must be in ISO-8601 format (yyyy-MM-ddTHH:mm:ss)",
  "path": "/api/v1/shows"
}
```

### 404 Not Found - Resource Missing
```json
{
  "timestamp": "2024-08-31T17:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Show with id abc123 not found",
  "path": "/api/v1/shows/abc123"
}
```

### 409 Conflict - Timing Overlap
```json
{
  "timestamp": "2024-08-31T17:15:00",
  "status": 409,
  "error": "Conflict",
  "message": "Show timing overlaps with existing show on screen",
  "path": "/api/v1/shows"
}
```

## Data Validation Rules

### CreateShowRequestDTO
- `movieId`: Required, UUID format
- `screenId`: Required, UUID format
- `showDateTime`: Required, ISO-8601 format (yyyy-MM-ddTHH:mm:ss)
- `basePrice`: Required, positive number >= 0.01
- `showType`: Required, one of: MORNING, MATINEE, EVENING, NIGHT

### UpdateShowRequestDTO
- `showId`: Required (from path parameter)
- All other fields: Optional (partial update)
- Same validation rules as create for provided fields

## Business Rules

1. **No Overlapping Shows**: A screen cannot have overlapping shows
   - Buffer time included: movie duration + 20 minutes cleaning time

2. **Seat Pricing**: Final seat price = basePrice × seatType.priceMultiplier
   - REGULAR: 1.0x
   - PREMIUM: 1.5x
   - RECLINER: 2.0x
   - VIP: 2.5x

3. **Seat Status**:
   - AVAILABLE: Can be booked
   - LOCKED: Temporarily reserved (10 minutes default)
   - BOOKED: Confirmed booking

4. **Show Types**:
   - MORNING: Early shows (typically 9 AM - 12 PM)
   - MATINEE: Afternoon shows (typically 12 PM - 4 PM)
   - EVENING: Evening shows (typically 4 PM - 8 PM)
   - NIGHT: Late shows (typically 8 PM onwards)

## Testing Workflow

1. **Setup Phase**:
   - Create a Movie
   - Create a Theater with Screens
   - Create Seats for the Screen

2. **Create Shows**:
   - Create morning show (10:00 AM)
   - Create evening show (6:30 PM)
   - Try creating overlapping show (should fail)

3. **Query Shows**:
   - List all shows for the movie
   - Filter by date
   - Filter by city

4. **Check Availability**:
   - Get available seats
   - Verify seat pricing calculation
   - Check seat status

5. **Update Show**:
   - Update base price
   - Update show type
   - Verify changes reflected in responses

## Performance Testing

```bash
# Test concurrent show creation (should handle timing conflicts)
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/v1/shows \
    -H "Content-Type: application/json" \
    -d "{
      \"movieId\": \"same-movie-id\",
      \"screenId\": \"same-screen-id\",
      \"showDateTime\": \"2024-12-25T18:30:00\",
      \"basePrice\": 250.00,
      \"showType\": \"EVENING\"
    }" &
done
wait
```

## Postman Collection

Import these examples into Postman:
- Set base URL: `http://localhost:8080/api/v1`
- Create environment variables for movieId, screenId, showId
- Use collection variables for authentication (if implemented)
