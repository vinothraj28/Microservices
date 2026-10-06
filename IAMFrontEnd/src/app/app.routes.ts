import { Routes } from '@angular/router';

import { authenticatedGuard } from './core/guards/authenticated.guard';
import { mfaPendingGuard } from './core/guards/mfa-pending.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'portfolio',
  },
  {
    path: 'portfolio',
    loadComponent: () =>
      import('./portfolio-v2/portfolio-v2.component').then(
        (component) => component.PortfolioV2Component,
      ),
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
        path: 'theater',
        canActivate: [authenticatedGuard],
        children: [
          {
            path: '',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/theater-list/theater-list.component').then(
                (component) => component.TheaterListComponent,
              ),
          },
          {
            path: ':theaterId/edit',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/theater-form/theater-form.component').then(
                (component) => component.TheaterFormComponent,
              ),
          },
          {
            path: ':theaterId/screen/list',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/screen-list/screen-list.component').then(
                (component) => component.ScreenListComponent,
              ),
          },
          {
            path: ':theaterId/screen/add',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/screen-form/screen-form.component').then(
                (component) => component.ScreenFormComponent,
              ),
          },
          {
            path: ':theaterId/screen/edit/:screenId', // NEW: Edit route
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/screen-form/screen-form.component').then(
                (component) => component.ScreenFormComponent,
              ),
          },
          {
            path: 'add',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/theater-form/theater-form.component').then(
                (component) => component.TheaterFormComponent,
              ),
          },
          {
            path: 'list',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./theater/theater-list/theater-list.component').then(
                (component) => component.TheaterListComponent,
              ),
          },
        ],
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
      {
        path: 'show',
        canActivate: [authenticatedGuard],
        children: [
          {
            path: 'setup',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./show/show-list/show-list.component').then(
                (component) => component.ShowListComponent,
              ),
          },
          {
            path: 'list',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./show/show-list-v2/show-list-v2.component').then(
                (component) => component.ShowListComponentV2,
              ),
          },
          {
            path: 'create',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./show/show-form/show-form.component').then(
                (component) => component.ShowFormComponent,
              ),
          },
          {
            path: ':id/edit',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./show/show-form/show-form.component').then(
                (component) => component.ShowFormComponent,
              ),
          },
          {
            path: ':id/seats',
            canActivate: [authenticatedGuard],
            loadComponent: () =>
              import('./show/seat-selection/seat-selection.component').then(
                (component) => component.SeatSelectionComponent,
              ),
          },
        ],
      },
      {
        path: 'booking',
        canActivate: [authenticatedGuard],
        loadComponent: () =>
          import('./booking/booking.component').then(
            (component) => component.BookingComponent,
          ),
      },
      {
        path: 'booking/confirm',
        redirectTo: 'booking',
      },
    ],
  },
];
