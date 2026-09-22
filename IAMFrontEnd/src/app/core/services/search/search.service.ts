import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';

import { APP_CONFIG } from '../../config/app-config.token';
import { Observable } from 'rxjs';
import { SearchResult, SearchSuggestion, SearchShowsRequest } from './search.model';
import { ShowListResponse } from '../show/show.models';
@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private http = inject(HttpClient);
  private appConfig = inject(APP_CONFIG);

  constructor() {}

  // searchShows(
  //   movieId: string,
  //   theaterId: string,
  //   showTime: string,
  //   page: number,
  //   size: number,
  //   date?: string,
  //   city?: string,
  // ): Observable<SearchResult> {
  //   // Params: movieId, theaterId, showTime, page, size, date (optional), city (optional)
  //   let params = new HttpParams()
  //     .set('movieId', movieId)
  //     .set('theaterId', theaterId)
  //     .set('showTime', showTime)
  //     .set('date', date ?? '')
  //     .set('city', city ?? '')
  //     .set('page', (page ?? 0).toString())
  //     .set('size', (size ?? 12).toString());
  //   return this.http.get<SearchResult>(this.appConfig.search.searchShowsUrl, {
  //     params,
  //   });
  // }

    searchShows(request: SearchShowsRequest): Observable<ShowListResponse> {
    let params = new HttpParams()
      .set('page', request.page.toString())
      .set('size', request.size.toString());
  
    for (const [key, value] of Object.entries(request)) {
      if (value !== undefined && value !== '' && key !== 'page' && key !== 'size') {
        params = params.set(key, String(value));
      }
    }
  
    return this.http.get<ShowListResponse>(
      this.appConfig.search.searchShowsUrl,
      { params },
    );
  }

  searchSuggestions(query: string): Observable<SearchSuggestion[]> {
    return this.http.get<SearchSuggestion[]>(
      `${this.appConfig.search.searchSuggestionsUrl}?q=${query}`,
    );
  }
}
