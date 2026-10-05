import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../../config/app-config.token';

export interface screenRequest {
  theaterId: string;
  screenName: string;
  screenNumber: number;
  totalRows: number;
  totalSeats: number;
  screenType: string;
  seatLayout: SeatLayoutRequestDTO[];
}

export interface screenResponse {
  screenId: string;
  theaterId: string;
  screenName: string;
  screenNumber: number;
  totalSeats: number;
  screenType: string;
  seatLayout: SeatLayoutRequestDTO[];
  createdAt: Date;
  updatedAt: Date;
}

export interface SeatLayoutRequestDTO {
  rowName: string;
  startSeatNumber: number;
  endSeatNumber: number;
  seatType: string;
  priceMultiplier: number;
}

export interface SeatLayoutResponseDTO {
  rowName: string;
  startSeatNumber: number;
  endSeatNumber: number;
  seatType: string;
  priceMultiplier: number;
}

@Injectable({
  providedIn: 'root',
})
export class ScreenService {
  private httpClient: HttpClient = inject(HttpClient);
  private appConfig = inject(APP_CONFIG);

  registerScreen(
    screenData: screenRequest,
  ): Observable<HttpResponse<screenResponse>> {
    // Implement the logic to register the screen using the provided screenData
    console.log('Registering screen with data:', screenData);
    return this.httpClient.post<screenResponse>(
      this.appConfig.theater.screen.registerUrl,
      screenData,
      { observe: 'response' },
    );
  }

  listScreensForTheater(
    theaterId: string,
  ): Observable<HttpResponse<screenResponse[]>> {
    // Implement the logic to list screens for the given theaterId
    console.log('Listing screens for theater ID:', theaterId);
    return this.httpClient.get<screenResponse[]>(
      `${this.appConfig.theater.screen.listUrl}${theaterId}/screens`,
      { observe: 'response' },
    );
  }
  // Add to screen.service.ts

  getScreenById(screenId: string): Observable<HttpResponse<screenResponse>> {
    return this.httpClient.get<screenResponse>(
      `${this.appConfig.theater.screen.getScreenByIdUrl}/${screenId}`,
      { observe: 'response' },
    );
  }

  getSeatLayoutByScreenId(
    screenId: string,
  ): Observable<SeatLayoutResponseDTO[]> {
    const url = `${this.appConfig.theater.screen.getSeatLayoutByScreenIdUrl}/${screenId}/seat-layout`;
    return this.httpClient.get<SeatLayoutResponseDTO[]>(url);
  }

  updateScreen(
    theaterId: string,
    screenId: string,
    screenData: screenRequest,
  ): Observable<HttpResponse<screenResponse>> {
    return this.httpClient.put<screenResponse>(
      `${this.appConfig.theater.screen.listUrl}${theaterId}/screens/${screenId}`,
      screenData,
      { observe: 'response' },
    );
  }
}
