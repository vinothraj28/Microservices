import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { AuthService } from '../core/services/auth/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly email = signal<string | null>(null);
  protected readonly roles = signal<string[]>([]);
  protected readonly mfaPending = signal(false);
  protected readonly hasToken = signal(false);

  ngOnInit(): void {
    const token = this.authService.getAccessToken();
    this.hasToken.set(Boolean(token));
    this.mfaPending.set(this.authService.isMfaPending());

    if (!token) {
      return;
    }

    const claims = this.decodeJwtPayload(token);
    const claimRoles = claims['roles'] ?? claims['role'] ?? [];

    this.roles.set(Array.isArray(claimRoles) ? claimRoles : [String(claimRoles)]);
    this.email.set((claims['email'] as string | undefined) ?? (claims['sub'] as string | undefined) ?? null);
  }

  protected logout(): void {
    this.authService.clearSession();
    this.router.navigate(['/login']);
  }

  private decodeJwtPayload(token: string): Record<string, unknown> {
    const parts = token.split('.');

    if (parts.length < 2) {
      return {};
    }

    try {
      const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const payload = atob(base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '='));
      return JSON.parse(payload) as Record<string, unknown>;
    } catch {
      return {};
    }
  }

}
