import { Injectable } from '@angular/core';
import { APP_CONFIG } from '../../config/app-config.token';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface MovieRegisterRequest {
  title: string;
  description: string;
  language: string;
  releaseDate: string;
  genre: string;
  durationMinutes: number;
  posterUrl: string;
  trailerUrl: string;
  rating: string;
  cast: string[];
  crew: string[];
}

export interface MovieUpdateRequest {
  movieId: string;
  title: string;
  description: string;
  language: string;
  releaseDate: string;
  genre: string;
  durationMinutes: number;
  posterUrl: string;
  trailerUrl: string;
  rating: string;
  cast: string[];
  crew: string[];
}

export interface MovieRegisterResponse {
  movieId: string;
  title: string;
  description: string;
  language: string;
  releaseDate: string;
  genre: string;
  durationMinutes: number;
  posterUrl: string;
  trailerUrl: string;
  rating: string;
  cast: string[];
  crew: string[];
  imageId: string;
}

export interface MovieListResponse {
  movieResponseDTO: MovieRegisterResponse[];
  totalPages: number;
  totalCount: number;
  page: number;
  size: number;
  hasNext: boolean;
}

export interface PosterUploadResponse {
  url: string;
}

@Injectable({
  providedIn: 'root',
})
export class MovieServiceService {
  private readonly httpClient = inject(HttpClient);
  private readonly appConfig = inject(APP_CONFIG);

  // registerMovie(movieData: MovieRegisterRequest):Observable<MovieRegisterResponse> {
  //   const url = this.appConfig.movie.registerUrl;
  //   return this.httpClient.post<MovieRegisterResponse>(url, movieData);
  // }

  registerMovie(movieData: MovieRegisterRequest, image?: File) {
    const formData = new FormData();

    formData.append(
      'movieRequestDTO',
      new Blob([JSON.stringify(movieData)], {
        type: 'application/json',
      }),
    );

    if (image) {
      formData.append('movieImage', image);
    }

    return this.httpClient.post<MovieRegisterResponse>(
      this.appConfig.movie.registerUrl,
      formData,
    );
  }

  updateMovie(movieData: MovieUpdateRequest, image?: File) {
    const formData = new FormData();

    formData.append(
      'movieUpdateRequestDTO',
      new Blob([JSON.stringify(movieData)], { type: 'application/json' }),
    );

    if (image) {
      formData.append('movieImage', image);
    }

    return this.httpClient.put<MovieRegisterResponse>(
      this.appConfig.movie.updateUrl,
      formData,
    );
  }

  getMovieList(page: number, pageSize: number): Observable<MovieListResponse> {
    const url = `${this.appConfig.movie.getAllMoviesUrl}?page=${page}&size=${pageSize}`;
    return this.httpClient.get<MovieListResponse>(url);
  }

  getMovieById(movieId: string): Observable<MovieRegisterResponse> {
    const url = `${this.appConfig.movie.getMovieByIdUrl}${movieId}`;
    return this.httpClient.get<MovieRegisterResponse>(url);
  }

  getImageById(imageId: string): Observable<Blob> {
    const url = `${this.appConfig.movie.getImageById}/${imageId}`;
    return this.httpClient.get(url, { responseType: 'blob' });
  }
}
