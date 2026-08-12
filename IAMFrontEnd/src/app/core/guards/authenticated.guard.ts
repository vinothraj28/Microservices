import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../services/auth/auth.service';

export const authenticatedGuard: CanActivateFn = async (_route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const existingToken = authService.getAccessToken();
  if (existingToken) {
    return true;
  }

  try {
    const token = await firstValueFrom(authService.refreshToken());

    if (token?.accessToken) {
      authService.storeAccessToken(token.accessToken);
      return true;
    }
  } catch (error) {
    console.error('Error refreshing token:', error);
    // Refresh failed, handled by redirect below.
  }

  authService.clearSession();

  return router.createUrlTree(['/authorize']);

  // return router.createUrlTree(['/authorize'], {
  //   queryParams: { returnUrl: state.url },
  // });
};
