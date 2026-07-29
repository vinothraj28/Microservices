import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/services/auth/auth.service';

@Component({
  selector: 'app-callback',
  standalone: true,
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {

    const code = this.route.snapshot.queryParamMap.get('code');
    const state = this.route.snapshot.queryParamMap.get('state');
    const authError = this.route.snapshot.queryParamMap.get('error');

    if (authError) {
      console.error('Authorization failed:', authError);
      this.authService.clearOauthSession();
      this.router.navigate(['/login']);
      return;
    }

    if (!code || !state) {
      console.error('No authorization code found.');
      this.authService.clearOauthSession();
      this.router.navigate(['/login']);
      return;
    }

    const oauthSession = this.authService.readOauthSession();

    if (!oauthSession || oauthSession.state !== state) {
      console.error('Invalid state.');
      this.authService.clearOauthSession();
      this.router.navigate(['/login']);
      return;
    }

    this.authService.exchangeCodeForToken(code, oauthSession.codeVerifier)
      .subscribe({
        next: (response) => {
          this.authService.storeAccessToken(response.accessToken);
          this.authService.clearOauthSession();
          this.authService.setMfaPending(false);

          // Refresh token is stored in an HttpOnly cookie by the backend.

          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Token exchange failed', err);
          this.authService.clearOauthSession();
          this.router.navigate(['/login']);
        }
      });

  }

}