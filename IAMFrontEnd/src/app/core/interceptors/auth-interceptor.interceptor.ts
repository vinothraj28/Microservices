// import { HttpInterceptorFn } from '@angular/common/http';
// import { AuthService } from '../services/auth/auth.service';
// import { inject } from '@angular/core';
// import { Router } from '@angular/router';

// export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
//   const authService = inject(AuthService);
//   const token = authService.getAccessToken();

//   if (token) {
//     req = req.clone({
//       setHeaders: {
//         Authorization: `Bearer ${token}`,
//       },
//     });
//   } else {
//     console.log(
//       'No access token found. Request will be sent without Authorization header.',
//     );
//   }

//   return next(req);
// };

import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, from, switchMap, throwError } from 'rxjs';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../services/auth/auth.service';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const accessToken = authService.getAccessToken();
  const requestWithToken = accessToken
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
    : req;

  const isRefreshCall = /refresh/i.test(req.url);

  return next(requestWithToken).pipe(
    catchError((error: unknown) => {
      if (
        !(error instanceof HttpErrorResponse) ||
        error.status !== 401 ||
        isRefreshCall
      ) {
        return throwError(() => error);
      }

      return from(firstValueFrom(authService.refreshToken())).pipe(
        switchMap((tokenResponse) => {
          const newToken = tokenResponse?.accessToken;

          if (!newToken) {
            authService.clearSession();
            void router.navigate(['/authorize']);
            return throwError(() => error);
          }

          authService.storeAccessToken(newToken);

          const retryRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`,
            },
          });

          return next(retryRequest);
        }),
        catchError((refreshError) => {
          authService.clearSession();
          void router.navigate(['/login']);
          return throwError(() => refreshError);
        }),
      );
    }),
  );
};
