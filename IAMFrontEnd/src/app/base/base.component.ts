// base.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-base',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.css'],
})
export class BaseComponent {
  isMovieMenuOpen = false;
  isTheaterMenuOpen = false;
  isUserMenuOpen = false;

  toggleMovieMenu() {
    this.isMovieMenuOpen = !this.isMovieMenuOpen;
    this.isTheaterMenuOpen = false;
  }

  toggleTheaterMenu() {
    this.isTheaterMenuOpen = !this.isTheaterMenuOpen;
    this.isMovieMenuOpen = false;
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }
}