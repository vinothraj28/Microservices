import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MovieServiceService,
  MovieRegisterResponse,
} from '../../core/services/movie/movie-service.service';
@Component({
  selector: 'app-movie-list',
  imports: [CommonModule],
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css'],
})
export class MovieListComponent implements OnInit {
  protected page: number = 0;
  protected size: number = 10;
  protected movies: MovieRegisterResponse[] = [];

  constructor(private movieService: MovieServiceService) {}

  ngOnInit(): void {
    this.loadMovies();
  }

  protected loadMovies(): void {
    this.movieService.getMovieList(this.page, this.size).subscribe(
      (movies: MovieRegisterResponse[]) => {
        console.log('Movies loaded:', movies);
        this.movies = movies;
      },
      (error) => {
        console.error('Error loading movies:', error);
      },
    );
  }
}
