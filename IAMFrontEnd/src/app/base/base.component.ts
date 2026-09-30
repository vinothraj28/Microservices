// base.component.ts
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
} from '@angular/router';
import { AuthService } from '../core/services/auth/auth.service';

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

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly email = signal<string | null>(null);
  protected readonly roles = signal<string[]>([]);
  protected readonly mfaPending = signal(false);
  protected readonly hasToken = signal(false);

  ngOnInit() {
    const token = this.authService.getAccessToken();
    this.hasToken.set(!!token);
    this.mfaPending.set(this.authService.isMfaPending());

    if (!token) {
      return;
    }

    const claims = this.decodeJwtPayload(token);
    const claimRoles = claims['roles'] ?? claims['role'] ?? [];

    this.roles.set(
      Array.isArray(claimRoles) ? claimRoles : [String(claimRoles)],
    );
    this.email.set(
      (claims['email'] as string | undefined) ??
        (claims['sub'] as string | undefined) ??
        null,
    );
  }

  protected logout(): void {
    this.authService.clearSession();
    this.router.navigate(['/login']);
  }

  decodeJwtPayload(token: string): Record<string, unknown> {
    const parts = token.split('.');

    if (parts.length < 2) {
      return {};
    }

    try {
      const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const payload = atob(
        base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '='),
      );
      return JSON.parse(payload) as Record<string, unknown>;
    } catch {
      return {};
    }
  }

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
