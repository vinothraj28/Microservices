import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ShowService } from './show.service';
import {
  CreateShowRequest,
  AvailableShowTimesRequest,
  AvailableShowTimesResponse,
  UpdateShowRequest,
  ShowResponse,
  ShowListResponse,
  AvailableSeatsResponse,
  ShowType,
  SeatType,
  SeatStatus,
} from './show.models';
import { APP_CONFIG } from '../../config/app-config.token';

describe('ShowService', () => {
  let service: ShowService;
  let httpMock: HttpTestingController;

  const mockAppConfig = {
    show: {
      createUrl: 'http://localhost:8080/api/v1/shows',
      updateUrl: 'http://localhost:8080/api/v1/shows',
      getByIdUrl: 'http://localhost:8080/api/v1/shows',
      getByMovieUrl: 'http://localhost:8080/api/v1/shows/movie',
      getByTheaterUrl: 'http://localhost:8080/api/v1/shows/theater',
      getSeatsUrl: 'http://localhost:8080/api/v1/shows',
    },
  };

  const mockShowResponse: ShowResponse = {
    showId: '770e8400-e29b-41d4-a716-446655440002',
    movieId: '550e8400-e29b-41d4-a716-446655440000',
    screenId: '660e8400-e29b-41d4-a716-446655440001',
    theaterId: '880e8400-e29b-41d4-a716-446655440003',
    movie: {
      movieId: '550e8400-e29b-41d4-a716-446655440000',
      title: 'Inception',
      description: 'A mind-bending thriller',
      duration: 148,
      releaseDate: '2010-07-16',
      language: 'ENGLISH',
      genre: 'SCI_FI',
      rating: 'PG_13',
      posterUrl: 'https://example.com/inception.jpg',
      trailerUrl: 'https://example.com/trailer.mp4',
      createdAt: '2026-08-01T10:00:00',
      updatedAt: '2026-08-01T10:00:00',
    },
    screen: {
      screenId: '660e8400-e29b-41d4-a716-446655440001',
      theaterId: '880e8400-e29b-41d4-a716-446655440003',
      name: 'SCREEN 1',
      totalSeats: 150,
      screenType: 'IMAX',
      soundSystem: 'DOLBY_ATMOS',
      createdAt: '2026-07-01T09:00:00',
      updatedAt: '2026-07-01T09:00:00',
    },
    showDateTime: '2026-09-15T18:30:00',
    basePrice: 250.0,
    showType: 'EVENING',
    availableSeats: 150,
    createdAt: '2026-08-31T19:28:00',
    updatedAt: '2026-08-31T19:28:00',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ShowService,
        { provide: APP_CONFIG, useValue: mockAppConfig },
      ],
    });
    service = TestBed.inject(ShowService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ==================== CREATE SHOW TESTS ====================

  describe('createShow', () => {
    it('should create a new show with valid data', () => {
      const request: CreateShowRequest = {
        movieId: '550e8400-e29b-41d4-a716-446655440000',
        screenId: '660e8400-e29b-41d4-a716-446655440001',
        showDateTime: '2026-09-15T18:30:00',
        basePrice: 250.0,
        showType: ShowType.EVENING,
      };

      service.createShow(request).subscribe((response) => {
        expect(response).toEqual(mockShowResponse);
        expect(response.showId).toBeTruthy();
        expect(response.availableSeats).toBe(150);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/v1/shows');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(request);
      req.flush(mockShowResponse);
    });

    it('should handle 400 Bad Request for invalid datetime format', () => {
      const request: CreateShowRequest = {
        movieId: '550e8400-e29b-41d4-a716-446655440000',
        screenId: '660e8400-e29b-41d4-a716-446655440001',
        showDateTime: 'invalid-date',
        basePrice: 250.0,
        showType: ShowType.EVENING,
      };

      service.createShow(request).subscribe({
        next: () => fail('should have failed with 400 error'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.error.message).toContain('ISO-8601 format');
        },
      });

      const req = httpMock.expectOne('http://localhost:8080/api/v1/shows');
      req.flush(
        {
          timestamp: '2026-08-31T19:28:00',
          status: 400,
          error: 'Bad Request',
          message:
            'Show date time must be in ISO-8601 format (yyyy-MM-ddTHH:mm:ss)',
          path: '/api/v1/shows',
        },
        { status: 400, statusText: 'Bad Request' },
      );
    });

    it('should handle 409 Conflict for overlapping shows', () => {
      const request: CreateShowRequest = {
        movieId: '550e8400-e29b-41d4-a716-446655440000',
        screenId: '660e8400-e29b-41d4-a716-446655440001',
        showDateTime: '2026-09-15T18:30:00',
        basePrice: 250.0,
        showType: ShowType.EVENING,
      };

      service.createShow(request).subscribe({
        next: () => fail('should have failed with 409 error'),
        error: (error) => {
          expect(error.status).toBe(409);
          expect(error.error.message).toContain('overlaps');
        },
      });

      const req = httpMock.expectOne('http://localhost:8080/api/v1/shows');
      req.flush(
        {
          timestamp: '2026-08-31T19:28:00',
          status: 409,
          error: 'Conflict',
          message: 'Show overlaps with existing show on this screen',
          path: '/api/v1/shows',
        },
        { status: 409, statusText: 'Conflict' },
      );
    });
  });

  describe('getAvailableShowTimes', () => {
    it('should request available slots with the selected screen and runtime', () => {
      const request: AvailableShowTimesRequest = {
        theaterId: '880e8400-e29b-41d4-a716-446655440003',
        screenId: '660e8400-e29b-41d4-a716-446655440001',
        movieRunTime: 148,
        requestedShowDateTime: '2026-09-15T00:00:00',
      };
      const availableSlots: AvailableShowTimesResponse = {
        theaterId: request.theaterId,
        screenId: request.screenId,
        requestedDate: '2026-09-15',
        slots: [
          {
            startTime: '2026-09-15T10:00:00',
            endTime: '2026-09-15T12:28:00',
          },
        ],
        totalCount: 1,
      };

      service.getAvailableShowTimes(request).subscribe((response) => {
        expect(response).toEqual(availableSlots);
      });

      const req = httpMock.expectOne(
        'http://localhost:8080/api/v1/shows/available-slots',
      );
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(request);
      req.flush(availableSlots);
    });
  });

  // ==================== UPDATE SHOW TESTS ====================

  describe('updateShow', () => {
    it('should update show with partial data', () => {
      const showId = '770e8400-e29b-41d4-a716-446655440002';
      const request: UpdateShowRequest = {
        showId,
        showDateTime: '2026-09-15T20:00:00',
        basePrice: 300.0,
        showType: ShowType.NIGHT,
      };

      const updatedResponse = { ...mockShowResponse, ...request };

      service.updateShow(showId, request).subscribe((response) => {
        expect(response.basePrice).toBe(300.0);
        expect(response.showType).toBe('NIGHT');
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/${showId}`,
      );
      expect(req.request.method).toBe('PUT');
      expect(req.request.body.showId).toBe(showId);
      req.flush(updatedResponse);
    });
  });

  // ==================== GET SHOW BY ID TESTS ====================

  describe('getShowById', () => {
    it('should retrieve a show by ID', () => {
      const showId = '770e8400-e29b-41d4-a716-446655440002';

      service.getShowById(showId).subscribe((response) => {
        expect(response).toEqual(mockShowResponse);
        expect(response.showId).toBe(showId);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/${showId}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockShowResponse);
    });

    it('should handle 404 Not Found', () => {
      const showId = 'non-existent-id';

      service.getShowById(showId).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
        },
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/${showId}`,
      );
      req.flush(
        {
          timestamp: '2026-08-31T19:28:00',
          status: 404,
          error: 'Not Found',
          message: `Show not found with ID: ${showId}`,
          path: `/api/v1/shows/${showId}`,
        },
        { status: 404, statusText: 'Not Found' },
      );
    });
  });

  // ==================== GET SHOWS BY MOVIE TESTS ====================

  describe('getShowsByMovie', () => {
    it('should get shows by movie without filters', () => {
      const movieId = '550e8400-e29b-41d4-a716-446655440000';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getShowsByMovie(movieId).subscribe((response) => {
        expect(response.shows.length).toBe(1);
        expect(response.totalCount).toBe(1);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/movie/${movieId}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should get shows by movie with date filter', () => {
      const movieId = '550e8400-e29b-41d4-a716-446655440000';
      const date = '2026-09-15';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getShowsByMovie(movieId, date).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/movie/${movieId}?date=${date}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should get shows by movie with date and city filters', () => {
      const movieId = '550e8400-e29b-41d4-a716-446655440000';
      const date = '2026-09-15';
      const city = 'Mumbai';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getShowsByMovie(movieId, date, city).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/movie/${movieId}?date=${date}&city=${city}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  // ==================== GET SHOWS BY THEATER TESTS ====================

  describe('getShowsByTheater', () => {
    it('should get shows by theater without date filter', () => {
      const theaterId = '880e8400-e29b-41d4-a716-446655440003';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getShowsByTheater(theaterId).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/theater/${theaterId}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should get shows by theater with date filter', () => {
      const theaterId = '880e8400-e29b-41d4-a716-446655440003';
      const date = '2026-09-15';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getShowsByTheater(theaterId, date).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/theater/${theaterId}?date=${date}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  // ==================== GET AVAILABLE SEATS TESTS ====================

  describe('getAvailableSeats', () => {
    it('should get available seats for a show', () => {
      const showId = '770e8400-e29b-41d4-a716-446655440002';
      const mockResponse: AvailableSeatsResponse = {
        showId,
        seats: [
          {
            seatId: '990e8400-e29b-41d4-a716-446655440005',
            rowName: 'A',
            seatNumber: 1,
            seatType: 'REGULAR',
            price: 250.0,
            status: 'AVAILABLE',
            lockedUntil: null,
          },
          {
            seatId: '990e8400-e29b-41d4-a716-446655440006',
            rowName: 'A',
            seatNumber: 2,
            seatType: 'REGULAR',
            price: 250.0,
            status: 'LOCKED',
            lockedUntil: '2026-08-31T19:38:00',
          },
          {
            seatId: '990e8400-e29b-41d4-a716-446655440007',
            rowName: 'B',
            seatNumber: 1,
            seatType: 'PREMIUM',
            price: 375.0,
            status: 'AVAILABLE',
            lockedUntil: null,
          },
        ],
        totalAvailable: 147,
      };

      service.getAvailableSeats(showId).subscribe((response) => {
        expect(response.seats.length).toBe(3);
        expect(response.totalAvailable).toBe(147);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/${showId}/seats`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  // ==================== UTILITY METHOD TESTS ====================

  describe('Utility Methods', () => {
    it('should format Date to ISO-8601 string', () => {
      const date = new Date('2026-09-15T18:30:00.000Z');
      const formatted = service.formatShowDateTime(date);
      expect(formatted).toBe('2026-09-15T18:30:00');
    });

    it('should parse ISO-8601 string to Date', () => {
      const dateStr = '2026-09-15T18:30:00';
      const parsed = service.parseShowDateTime(dateStr);
      expect(parsed instanceof Date).toBe(true);
      expect(parsed.getFullYear()).toBe(2026);
    });

    it('should format Date for query parameters', () => {
      const date = new Date('2026-09-15T18:30:00.000Z');
      const formatted = service.formatDateForQuery(date);
      expect(formatted).toBe('2026-09-15');
    });

    it('should calculate seat price correctly', () => {
      const basePrice = 100;
      expect(service.calculateSeatPrice(basePrice, SeatType.REGULAR)).toBe(100);
      expect(service.calculateSeatPrice(basePrice, SeatType.PREMIUM)).toBe(150);
      expect(service.calculateSeatPrice(basePrice, SeatType.RECLINER)).toBe(
        200,
      );
      expect(service.calculateSeatPrice(basePrice, SeatType.VIP)).toBe(250);
    });
  });

  // ==================== CONVENIENCE METHOD TESTS ====================

  describe('Convenience Methods', () => {
    it('should get today shows by movie', () => {
      const movieId = '550e8400-e29b-41d4-a716-446655440000';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getTodayShowsByMovie(movieId).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const today = new Date().toISOString().split('T')[0];
      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/movie/${movieId}?date=${today}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should get today shows by theater', () => {
      const theaterId = '880e8400-e29b-41d4-a716-446655440003';
      const mockResponse: ShowListResponse = {
        shows: [mockShowResponse],
        totalCount: 1,
      };

      service.getTodayShowsByTheater(theaterId).subscribe((response) => {
        expect(response.shows.length).toBe(1);
      });

      const today = new Date().toISOString().split('T')[0];
      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/theater/${theaterId}?date=${today}`,
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should check if show is sold out', () => {
      const showId = '770e8400-e29b-41d4-a716-446655440002';
      const soldOutShow = { ...mockShowResponse, availableSeats: 0 };

      service.isShowSoldOut(showId).subscribe((isSoldOut) => {
        expect(isSoldOut).toBe(true);
      });

      const req = httpMock.expectOne(
        `http://localhost:8080/api/v1/shows/${showId}`,
      );
      req.flush(soldOutShow);
    });
  });
});
