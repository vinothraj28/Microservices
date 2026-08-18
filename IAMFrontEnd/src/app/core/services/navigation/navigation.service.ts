import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(
    private router: Router,
    private location: Location
  ) { }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  goBack(): void {
    this.location.back();
  }

  goBackWithFallback(fallbackPath: string = '/'): void {
    if (window.history.length > 1) {
      this.location.back();
    } else {
      this.router.navigate([fallbackPath]);
    }
  }

}
