import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { APP_CONFIG } from '../../config/app-config.token';
import { AuthSessionService } from './auth-session.service';

export interface RegisterRequest {
  userName: string;
  emailAddress: string;
  dob: string;
  password: string;
}

export interface AuthenticateRequest {
  email: string;
  password: string;
}

export interface AuthenticateResponse {
  accessToken?: string;
  mfaRequired?: boolean;
}

export interface MfaSetupRequest {
  emailAddress: string;
}

export interface MfaSetupResponse {
  qrCodeUrl: string; // base64-encoded image bytes
}

export interface MfaConfirmRequest {
  emailAddress: string;
  code: string;
}

export interface MfaVerifyRequest {
  mfaCode: string;
}

export interface MfaVerifyResponse {
  redirectUrl: string;
}

export interface OAuth2TokenRequest {
  grantType: string;
  code: string;
  redirectUri: string;
  codeVerifier: string;
  clientId: string;
  // clientSecret?: string; // only if your client is confidential
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly httpClient = inject(HttpClient);
  private readonly appConfig = inject(APP_CONFIG);
  private readonly authSession = inject(AuthSessionService);

  register(request: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(this.appConfig.auth.registerUrl, request);
  }

  authenticate(request: AuthenticateRequest): Observable<HttpResponse<AuthenticateResponse>> {
    return this.httpClient.post<AuthenticateResponse>(this.appConfig.auth.authenticateUrl,
      request, { observe: 'response' as const , withCredentials: true});
  }

  setupMfa(request: MfaSetupRequest, token: string): Observable<MfaSetupResponse> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.httpClient.post<MfaSetupResponse>(this.appConfig.mfa.setupUrl, request, { headers });
  }

  confirmMfa(request: MfaConfirmRequest, token: string): Observable<void> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.httpClient.post<void>(this.appConfig.mfa.confirmUrl, request, { headers });
  }

  verifyMfa(request: MfaVerifyRequest): Observable<HttpResponse<MfaVerifyResponse>> {
    return this.httpClient.post<MfaVerifyResponse>(this.appConfig.auth.verifyMfaUrl, request, {
      observe: 'response' as const, withCredentials: true});
  }

  refreshToken(): Observable<{ accessToken: string }> {
    return this.httpClient.post<{ accessToken: string }>(this.appConfig.auth.refreshTokenUrl, null, {
      withCredentials: true
    });
  }

  async authorize(): Promise<void> {
    const state = this.generateRandomString(32);
    const codeVerifier = this.generateRandomString(96);
    const codeChallenge = await this.createCodeChallenge(codeVerifier);

    this.authSession.saveOauthSession({ state, codeVerifier });

    const params = new URLSearchParams({
      response_type: 'code',
      client_id: this.appConfig.auth.clientId,
      redirect_uri: this.appConfig.auth.redirectUri,
      scope: this.appConfig.auth.scope,
      state,
      code_challenge: codeChallenge,
      code_challenge_method: 'S256'
    });

    window.location.assign(`${this.appConfig.auth.authorizeBaseUrl}?${params.toString()}`);
  }

  exchangeCodeForToken(code: string, codeVerifier: string): Observable<{ accessToken: string }> {
    const request: OAuth2TokenRequest = {
      grantType: 'authorization_code',
      code: code,
      redirectUri: this.appConfig.auth.redirectUri,
      codeVerifier: codeVerifier,
      clientId: this.appConfig.auth.clientId
    };

    return this.httpClient.post<{ accessToken: string }>(this.appConfig.auth.tokenUrl, request, {
      withCredentials: true
    });
  }

  getAccessToken(): string | null {
    return this.authSession.getAccessToken();
  }

  storeAccessToken(token: string): void {
    this.authSession.setAccessToken(token);
  }

  clearSession(): void {
    this.authSession.clearAll();
  }

  setMfaPending(value: boolean): void {
    this.authSession.setMfaPending(value);
  }

  isMfaPending(): boolean {
    return this.authSession.isMfaPending();
  }

  readOauthSession(): { state: string; codeVerifier: string } | null {
    return this.authSession.readOauthSession();
  }

  clearOauthSession(): void {
    this.authSession.clearOauthSession();
  }

  isAllowedPostLoginRedirect(redirectUrl: string): boolean {
    try {
      const parsedUrl = new URL(redirectUrl, window.location.origin);
      return this.appConfig.auth.postLoginRedirectAllowList.some((allowedBase) => {
        const allowedUrl = new URL(allowedBase);
        return parsedUrl.origin === allowedUrl.origin &&
         parsedUrl.pathname.startsWith(allowedUrl.pathname);
      });
    } catch {
      return false;
    }
  }

  private generateRandomString(byteLength: number): string {
    const randomBytes = new Uint8Array(byteLength);
    crypto.getRandomValues(randomBytes);
    return this.base64UrlEncode(randomBytes);
  }

  private async createCodeChallenge(codeVerifier: string): Promise<string> {
    const encoded = new TextEncoder().encode(codeVerifier);
    const hashed = await crypto.subtle.digest('SHA-256', encoded);
    return this.base64UrlEncode(new Uint8Array(hashed));
  }

  private base64UrlEncode(input: Uint8Array): string {
    const binary = Array.from(input)
      .map((value) => String.fromCharCode(value))
      .join('');

    return btoa(binary)
      .replace(/\+/g, '-')
      .replace(/\//g, '_')
      .replace(/=+$/g, '');
  }
}
