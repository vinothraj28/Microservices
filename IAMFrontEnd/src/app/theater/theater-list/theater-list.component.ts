import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {
  TheaterResponse,
  TheaterService,
} from '../../core/services/theater/theater.service';

@Component({
  selector: 'app-theater-list',
  imports: [CommonModule, RouterModule],
  templateUrl: './theater-list.component.html',
  styleUrls: ['./theater-list.component.css'],
})
export class TheaterListComponent {
  protected page: number = 0;
  protected city: string = 'haarlem';
  protected size: number = 10;
  protected isMovieLoading: boolean = true;
  protected theaters: TheaterResponse[] = [];
  protected image: { [key: string]: string } = {};
  protected totalPages = 0;
  protected totalCount = 0;
  protected hasNext = false;

  constructor(private theaterService: TheaterService) {}

  ngOnInit(): void {
    this.loadTheaters();
  }

  protected loadTheaters(): void {
    this.isMovieLoading = true;
    this.theaterService
      .getTheaterList(this.page, this.size, this.city)
      .subscribe(
        (theaters: {
          theaters: TheaterResponse[];
          totalPages: number;
          totalCount: number;
          hasNext: boolean;
        }) => {
          console.log('Theaters loaded:', theaters);
          this.theaters = theaters.theaters;
          this.totalPages = theaters.totalPages;
          this.totalCount = theaters.totalCount;
          this.hasNext = theaters.hasNext;

          this.isMovieLoading = false;
        },
        (error) => {
          console.error('Error loading theaters:', error);
          this.isMovieLoading = false;
        },
      );
  }

  protected deleteTheaterById(theaterId: string): void {
    if (this.isMovieLoading) return;
    this.theaterService.deleteTheater(theaterId).subscribe({
      next: () => {
        console.log('Theater deleted successfully');
        this.loadTheaters();
      },
      error: (error: any) => {
        console.error('Error deleting theater:', error);
      },
    });
  }

  protected prevPage(): void {
    if (this.page <= 0 || this.isMovieLoading) return;
    this.page--;
    this.loadTheaters();
  }

  protected nextPage(): void {
    if (!this.hasNext || this.isMovieLoading) return;
    this.page++;
    this.loadTheaters();
  }

  protected setPageSize(size: number): void {
    if (size === this.size || this.isMovieLoading) return;
    this.size = size;
    this.page = 0;
    this.loadTheaters();
  }

  protected get uniqueCities(): number {
    const cities = this.theaters.map((theater) => theater.city);
    const uniqueCities = new Set(cities);
    return uniqueCities.size;
  }

}
