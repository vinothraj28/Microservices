/**
 * Show Service Models and Types
 * Comprehensive type definitions for the Show Service API
 */

// ==================== ENUMS ====================

/**
 * Show Time Types
 * Represents different time slots for movie shows
 */
export enum ShowType {
  MORNING = 'MORNING', // 6 AM - 12 PM
  MATINEE = 'MATINEE', // 12 PM - 5 PM
  EVENING = 'EVENING', // 5 PM - 9 PM
  NIGHT = 'NIGHT', // 9 PM - 12 AM
}

/**
 * Seat Types
 * Different categories of seats with varying price multipliers
 */
export enum SeatType {
  REGULAR = 'REGULAR', // 1.0× base price
  PREMIUM = 'PREMIUM', // 1.5× base price
  RECLINER = 'RECLINER', // 2.0× base price
  VIP = 'VIP', // 2.5× base price
}

/**
 * Seat Status
 * Current availability status of a seat
 */
export enum SeatStatus {
  AVAILABLE = 'AVAILABLE', // Can be selected for booking
  LOCKED = 'LOCKED', // Temporarily held during checkout (10 min)
  BOOKED = 'BOOKED', // Permanently reserved
}

/**
 * Screen Types
 * Different types of cinema screens
 */
export enum ScreenType {
  STANDARD = 'STANDARD',
  IMAX = 'IMAX',
  DOLBY = 'DOLBY',
  THREE_D = '3D',
  FOUR_DX = '4DX',
}

/**
 * Sound Systems
 * Audio technology available in screens
 */
export enum SoundSystem {
  STANDARD = 'STANDARD',
  DOLBY_ATMOS = 'DOLBY_ATMOS',
  DTS_X = 'DTS_X',
  DOLBY_DIGITAL = 'DOLBY_DIGITAL',
}

// ==================== INTERFACES ====================

/**
 * Movie Response (nested in Show endpoints)
 */
export interface MovieResponse {
  movieId: string;
  title: string;
  description: string;
  durationMinutes: number;
  releaseDate: string;
  language: string;
  genre: string;
  rating: string;
  posterUrl: string;
  trailerUrl: string;
  createdAt: string;
  updatedAt: string;
  imageId: string;
}

/**
 * Screen Response (nested in Show endpoints)
 */
export interface ScreenResponse {
  screenId: string;
  theaterId: string;
  screenName: string;
  totalSeats: number;
  screenType: string;
  soundSystem: string;
  createdAt: string;
  updatedAt: string;
}

/**
 * Create Show Request
 * Payload for creating a new show
 */
export interface CreateShowRequest {
  theaterId: string; // UUID format
  movieId: string; // UUID format
  screenId: string; // UUID format
  showDateTime: string; // ISO-8601 format (YYYY-MM-DDTHH:mm:ss)
  basePrice: number; // Must be positive (min: 0.01)
  showType: ShowType; // MORNING | MATINEE | EVENING | NIGHT
}

/**
 * Request used to find non-conflicting show start times for a screen.
 */
export interface AvailableShowTimesRequest {
  theaterId: string;
  screenId: string;
  movieRunTime: number;
  requestedShowDateTime: string;
}

export interface AvailableShowTimeSlot {
  startTime: string;
  endTime: string;
}

export interface AvailableShowTimesResponse {
  theaterId: string;
  screenId: string;
  requestedDate: string;
  slots: AvailableShowTimeSlot[];
  totalCount: number;
}

/**
 * Update Show Request
 * Payload for updating an existing show (partial update)
 */
export interface UpdateShowRequest {
  showId: string; // Required in both URL and body
  showDateTime?: string; // Optional ISO-8601 format
  basePrice?: number; // Optional, must be positive if provided
  showType?: ShowType; // Optional show type
}

/**
 * Show Response
 * Complete show information with nested movie and screen details
 */
export interface ShowResponse {
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
  theaterName: string;
}

/**
 * Show List Response
 * Response for endpoints returning multiple shows
 */
export interface ShowListResponse {
  shows: ShowResponse[];
  totalCount: number;
  totalPages: number;
  hasNext: boolean;
}

/**
 * Seat Information
 * Detailed seat information with availability and pricing
 */
export interface SeatInfo {
  seatId: string;
  rowName: string;
  seatNumber: number;
  seatType: string;
  price: number; // Calculated: basePrice × seatType.priceMultiplier
  status: string; // AVAILABLE | LOCKED | BOOKED
  lockedUntil: string | null; // ISO datetime when lock expires (only for LOCKED)
}

/**
 * Available Seats Response
 * Response for seat availability endpoint
 */
export interface AvailableSeatsResponse {
  showId: string;
  seats: SeatInfo[];
  totalAvailable: number;
}

/**
 * Seats Grouped by Status
 * Helper interface for organizing seats by availability
 */
export interface SeatsGroupedByStatus {
  available: SeatInfo[];
  locked: SeatInfo[];
  booked: SeatInfo[];
  totalAvailable: number;
}

/**
 * Seats Grouped by Type
 * Helper interface for organizing seats by type
 */
export interface SeatsGroupedByType {
  regular: SeatInfo[];
  premium: SeatInfo[];
  recliner: SeatInfo[];
  vip: SeatInfo[];
}

/**
 * Error Response
 * Standard error response format
 */
export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

/**
 * Show Filter Options
 * Optional filters for listing shows
 */
export interface ShowFilterOptions {
  date?: string; // YYYY-MM-DD format
  city?: string; // City name for location-based filtering
}

/**
 * Show Statistics
 * Calculated metrics for a show
 */
export interface ShowStatistics {
  totalSeats: number;
  availableSeats: number;
  bookedSeats: number;
  lockedSeats: number;
  occupancyPercentage: number;
  revenueProjection: number;
  actualRevenue: number;
}

/**
 * Show Time Slot
 * Helper interface for time slot management
 */
export interface ShowTimeSlot {
  startTime: Date;
  endTime: Date;
  showType: ShowType;
  isAvailable: boolean;
}

// ==================== TYPE GUARDS ====================

/**
 * Type guard to check if a value is a valid ShowType
 */
export function isShowType(value: string): value is ShowType {
  return Object.values(ShowType).includes(value as ShowType);
}

/**
 * Type guard to check if a value is a valid SeatType
 */
export function isSeatType(value: string): value is SeatType {
  return Object.values(SeatType).includes(value as SeatType);
}

/**
 * Type guard to check if a value is a valid SeatStatus
 */
export function isSeatStatus(value: string): value is SeatStatus {
  return Object.values(SeatStatus).includes(value as SeatStatus);
}

// ==================== CONSTANTS ====================

/**
 * Seat Type Price Multipliers
 * Used to calculate final seat price from base price
 */
export const SEAT_PRICE_MULTIPLIERS: Record<SeatType, number> = {
  [SeatType.REGULAR]: 1.0,
  [SeatType.PREMIUM]: 1.5,
  [SeatType.RECLINER]: 2.0,
  [SeatType.VIP]: 2.5,
};

/**
 * Show Type Time Ranges
 * Typical time ranges for different show types
 */
export const SHOW_TYPE_TIME_RANGES: Record<
  ShowType,
  { start: number; end: number }
> = {
  [ShowType.MORNING]: { start: 6, end: 12 },
  [ShowType.MATINEE]: { start: 12, end: 17 },
  [ShowType.EVENING]: { start: 17, end: 21 },
  [ShowType.NIGHT]: { start: 21, end: 24 },
};

/**
 * Seat Lock Duration
 * Time in minutes that a seat remains locked during checkout
 */
export const SEAT_LOCK_DURATION_MINUTES = 10;

/**
 * Date Format Constants
 */
export const DATE_FORMATS = {
  ISO_8601_DATETIME: 'YYYY-MM-DDTHH:mm:ss',
  ISO_8601_DATE: 'YYYY-MM-DD',
  DISPLAY_DATE: 'DD MMM YYYY',
  DISPLAY_TIME: 'hh:mm A',
  DISPLAY_DATETIME: 'DD MMM YYYY, hh:mm A',
};

// ==================== HELPER TYPES ====================

/**
 * Partial Show Update
 * Type-safe partial update of show properties
 */
export type PartialShowUpdate = Partial<
  Pick<ShowResponse, 'showDateTime' | 'basePrice' | 'showType'>
>;

/**
 * Show Creation DTO
 * Data Transfer Object for show creation
 */
export type ShowCreationDTO = Omit<CreateShowRequest, 'showDateTime'> & {
  showDateTime: Date;
};

/**
 * Seat Selection
 * Helper type for seat selection in booking flow
 */
export interface SeatSelection {
  seatId: string;
  rowName: string;
  seatNumber: number;
  price: number;
  seatType: SeatType;
}

/**
 * Booking Summary
 * Summary of selected seats for booking
 */
export interface BookingSummary {
  showId: string;
  selectedSeats: SeatSelection[];
  totalSeats: number;
  totalAmount: number;
  showDateTime: Date;
  movieTitle: string;
  theaterName: string;
  screenName: string;
}
