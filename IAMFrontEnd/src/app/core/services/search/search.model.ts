import { ShowResponse } from '../show/show.models';

export interface SearchSuggestion {
  id: string;
  type: 'MOVIE' | 'THEATER';
  title: string;
  subtitle?: string;
  imageUrl?: string;
}

export interface SearchResult {
  show: ShowResponse;
}

// =========================================================
// SEARCH SHOWS REQUEST
// =========================================================

export interface SearchShowsRequest {
  movieId?: string;
  theaterId?: string;
  showTime?: string;
  showType?: ShowType;
  date?: string;
  city?: string;
  language?: string;
  genre?: string;
  page: number;
  size: number;
}

export enum ShowType {
  MORNING = 'MORNING',
  MATINEE = 'MATINEE',
  EVENING = 'EVENING',
  NIGHT = 'NIGHT',
}
