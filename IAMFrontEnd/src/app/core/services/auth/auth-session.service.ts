import { Injectable } from '@angular/core';

interface OauthSession {
  state: string;
  codeVerifier: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthSessionService {
  private static readonly ACCESS_TOKEN_KEY = 'access_token';
  private static readonly OAUTH_SESSION_KEY = 'oauth_session';
  private static readonly MFA_PENDING_KEY = 'mfa_pending';

  setAccessToken(token: string): void {
    sessionStorage.setItem(AuthSessionService.ACCESS_TOKEN_KEY, token);
  }

  getAccessToken(): string | null {
    return sessionStorage.getItem(AuthSessionService.ACCESS_TOKEN_KEY);
  }

  clearAccessToken(): void {
    sessionStorage.removeItem(AuthSessionService.ACCESS_TOKEN_KEY);
  }

  saveOauthSession(session: OauthSession): void {
    sessionStorage.setItem(AuthSessionService.OAUTH_SESSION_KEY, JSON.stringify(session));
  }

  readOauthSession(): OauthSession | null {
    const raw = sessionStorage.getItem(AuthSessionService.OAUTH_SESSION_KEY);

    if (!raw) {
      return null;
    }

    try {
      const parsed = JSON.parse(raw) as Partial<OauthSession>;

      if (!parsed.state || !parsed.codeVerifier) {
        return null;
      }

      return {
        state: parsed.state,
        codeVerifier: parsed.codeVerifier
      };
    } catch {
      return null;
    }
  }

  clearOauthSession(): void {
    sessionStorage.removeItem(AuthSessionService.OAUTH_SESSION_KEY);
  }

  setMfaPending(value: boolean): void {
    sessionStorage.setItem(AuthSessionService.MFA_PENDING_KEY, value ? 'true' : 'false');
  }

  isMfaPending(): boolean {
    return sessionStorage.getItem(AuthSessionService.MFA_PENDING_KEY) === 'true';
  }

  clearMfaPending(): void {
    sessionStorage.removeItem(AuthSessionService.MFA_PENDING_KEY);
  }

  clearAll(): void {
    this.clearAccessToken();
    this.clearOauthSession();
    this.clearMfaPending();
  }
}
