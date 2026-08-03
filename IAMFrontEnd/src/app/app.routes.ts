import { Routes } from '@angular/router';

import { authenticatedGuard } from './core/guards/authenticated.guard';
import { mfaPendingGuard } from './core/guards/mfa-pending.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: 'login',
    data: { authContext: 'user' },
    loadComponent: () =>
      import('./login/login.component').then(
        (component) => component.LoginComponent,
      ),
  },
  {
    path: 'admin/login',
    data: { authContext: 'admin' },
    loadComponent: () =>
      import('./login/login.component').then(
        (component) => component.LoginComponent,
      ),
  },
  {
    path: 'register',
    data: { authContext: 'user' },
    loadComponent: () =>
      import('./register/register.component').then(
        (component) => component.RegisterComponent,
      ),
  },
  {
    path: 'admin/register',
    data: { authContext: 'admin' },
    loadComponent: () =>
      import('./register/register.component').then(
        (component) => component.RegisterComponent,
      ),
  },
  {
    path: 'mfa-setup',
    canActivate: [authenticatedGuard],
    loadComponent: () =>
      import('./mfa-setup/mfa-setup.component').then(
        (component) => component.MfaSetupComponent,
      ),
  },
  {
    path: 'dashboard',
    redirectTo: 'base/dashboard',
  },
  {
    path: 'verify-mfa',
    canActivate: [mfaPendingGuard],
    loadComponent: () =>
      import('./verify-mfa/verify-mfa.component').then(
        (component) => component.VerifyMfaComponent,
      ),
  },
  {
    path: 'authorize',
    loadComponent: () =>
      import('./authorize/authorize.component').then(
        (component) => component.AuthorizeComponent,
      ),
  },
  {
    path: 'callback',
    loadComponent: () =>
      import('./callback/callback.component').then(
        (component) => component.CallbackComponent,
      ),
  },
  {
    path: 'base',
    canActivate: [authenticatedGuard],
    loadComponent: () =>
      import('./base/base.component').then(
        (component) => component.BaseComponent,
      ),
    children: [
      {
        path: 'dashboard',
        canActivate: [authenticatedGuard],
        loadComponent: () =>
          import('./dashboard/dashboard.component').then(
            (component) => component.DashboardComponent,
          ),
      },
      {
        path: 'movie',
        canActivate: [authenticatedGuard],
        children: [
          {
            path: '',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./movie/movie-main/movie-main.component').then(
                (component) => component.MovieMainComponent,
              ),
          },
          {
            path: ':movieId/edit',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./movie/movie-form/movie-form.component').then(
                (component) => component.MovieFormComponent,
              ),
          },
          {
            path: 'add',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./movie/movie-form/movie-form.component').then(
                (component) => component.MovieFormComponent,
              ),
          },
          {
            path: 'list',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./movie/movie-list/movie-list.component').then(
                (component) => component.MovieListComponent,
              ),
          },
        ],
      },
    ],
  },
];
