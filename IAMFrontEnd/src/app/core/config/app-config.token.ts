import { InjectionToken } from '@angular/core';

export interface AppConfig {
  auth: {
    registerUrl: string;
    authenticateUrl: string;
    verifyMfaUrl: string;
    authorizeBaseUrl: string;
    tokenUrl: string;
    clientId: string;
    redirectUri: string;
    scope: string;
    postLoginRedirectAllowList: string[];
    refreshTokenUrl: string;
  };
  mfa: {
    setupUrl: string;
    confirmUrl: string;
  };
  movie: {
    registerUrl: string;
    updateUrl: string;
    deleteUrl: string;
    getMovieByIdUrl: string;
    getAllMoviesUrl: string;
  };
}

export const APP_CONFIG = new InjectionToken<AppConfig>('app.config');
