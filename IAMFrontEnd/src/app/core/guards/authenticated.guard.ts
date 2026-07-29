import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth/auth.service';
import { firstValueFrom } from 'rxjs';

export const authenticatedGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getAccessToken()) {
    return true;
  }

  firstValueFrom(authService.refreshToken()).then((token) => {
    if (token && token.accessToken) {
      authService.storeAccessToken(token.accessToken);
    }
  });
  
  return router.createUrlTree(['/authorize']);
};
