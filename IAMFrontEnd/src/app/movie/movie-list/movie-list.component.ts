import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MovieServiceService,
  MovieRegisterResponse,
  MovieListResponse,
} from '../../core/services/movie/movie-service.service';
import { RouterLink } from "@angular/router";
@Component({
  selector: 'app-movie-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css'],
})
export class MovieListComponent implements OnInit {
  protected page: number = 0;
  protected size: number = 10;
  protected isMovieLoading: boolean = true;
  protected movies: MovieRegisterResponse[] = [];
  protected image: { [key: string]: string } = {};
  protected totalPages = 0;
  protected totalCount = 0;
  protected hasNext = false;

  constructor(private movieService: MovieServiceService) {}

  ngOnInit(): void {
    this.loadMovies();
  }

  protected loadMovies(): void {
    this.isMovieLoading = true;
    this.movieService.getMovieList(this.page, this.size).subscribe(
      (movies: MovieListResponse) => {
        console.log('Movies loaded:', movies);
        this.movies = movies.movieResponseDTO;
        this.totalPages = movies.totalPages;
        this.totalCount = movies.totalCount;
        this.hasNext = movies.hasNext;
        movies.movieResponseDTO.forEach((movie) => {
          if (movie.imageId) {
            this.movieService
              .getImageById(movie.imageId)
              .subscribe((imageBlob) => {
                const imageUrl = URL.createObjectURL(imageBlob);
                this.image[movie.imageId] = imageUrl;
              });
          }
        });
        this.isMovieLoading = false;
      },
      (error) => {
        console.error('Error loading movies:', error);
        this.isMovieLoading = false;
      },
    );
  }

  protected prevPage(): void {
    if (this.page <= 0 || this.isMovieLoading) return;
    this.page--;
    this.loadMovies();
  }

  protected nextPage(): void {
    if (!this.hasNext || this.isMovieLoading) return;
    this.page++;
    this.loadMovies();
  }

  protected setPageSize(size: number): void {
    if (size === this.size || this.isMovieLoading) return;
    this.size = size;
    this.page = 0;
    this.loadMovies();
  }
}
