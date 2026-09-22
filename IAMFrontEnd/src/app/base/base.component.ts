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
  isShowMenuOpen = false;
  isUserMenuOpen = false;

  toggleMovieMenu() {
    this.isMovieMenuOpen = !this.isMovieMenuOpen;
    this.isTheaterMenuOpen = false;
    this.isShowMenuOpen = false;
    this.isUserMenuOpen = false;
  }

  toggleTheaterMenu() {
    this.isTheaterMenuOpen = !this.isTheaterMenuOpen;
    this.isMovieMenuOpen = false;
    this.isShowMenuOpen = false;
    this.isUserMenuOpen = false;
  }

  toggleShowMenu() {
    this.isShowMenuOpen = !this.isShowMenuOpen;
    this.isMovieMenuOpen = false;
    this.isTheaterMenuOpen = false;
    this.isUserMenuOpen = false;
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }
}
