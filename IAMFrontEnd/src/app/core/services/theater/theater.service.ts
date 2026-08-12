import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app-config.token';

export interface TheaterRequest {
  name: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
  phone: string;
  email: string;
  latitude: number | null;
  longitude: number | null;
  amenities: string[];
}

export interface TheaterResponse {
  theaterId: string;
  name: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
  phone: string;
  email: string;
  latitude: number | null;
  longitude: number | null;
  amenities: string[];
}

@Injectable({
  providedIn: 'root',
})
export class TheaterService {
  private http = inject(HttpClient);
  private appConfig = inject(APP_CONFIG);

  registerTheater(theater: TheaterRequest): Observable<TheaterResponse> {
    return this.http.post<TheaterResponse>(
      this.appConfig.theater.registerUrl,
      theater,
    );
  }

  getTheaterList(
    page: number,
    size: number,
    city: string = 'haarlem',
  ): Observable<{
    theaters: TheaterResponse[];
    totalPages: number;
    totalCount: number;
    hasNext: boolean;
  }> {
    const url = `${this.appConfig.theater.getAllTheatersUrl}?page=${page}&size=${size}&city=${city}`;
    return this.http.get<{
      theaters: TheaterResponse[];
      totalPages: number;
      totalCount: number;
      hasNext: boolean;
    }>(url);
  }

  getTheaterById(theaterId: string): Observable<TheaterResponse> {
    const url = `${this.appConfig.theater.getTheaterByIdUrl}/${theaterId}`;
    return this.http.get<TheaterResponse>(url);
  }

  updateTheater(
    theaterId: string,
    theater: TheaterRequest,
  ): Observable<TheaterResponse> {
    const url = `${this.appConfig.theater.updateUrl}/${theaterId}`;
    return this.http.put<TheaterResponse>(url, theater);
  }

  deleteTheater(theaterId: string): Observable<void> {
    const url = `${this.appConfig.theater.deleteUrl}/${theaterId}`;
    return this.http.delete<void>(url);
  }
}
