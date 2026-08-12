import { Component, signal } from '@angular/core';
import { MovieFormComponent } from '../movie-form/movie-form.component';
import { MovieListComponent } from '../movie-list/movie-list.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-movie-main',
  imports: [
    MovieFormComponent,
    MovieListComponent,
    FormsModule,
    CommonModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './movie-main.component.html',
  styleUrls: ['./movie-main.component.css'],
})
export class MovieMainComponent {
  protected readonly selectComponent = signal<'form' | 'list'>('list');
}
