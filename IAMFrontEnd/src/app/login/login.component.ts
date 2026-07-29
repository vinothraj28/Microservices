import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../core/services/auth/auth.service';

interface LoginForm {
  email: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly isSubmitting = signal(false);
  protected readonly submitted = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly authContext = signal<'user' | 'admin'>('user');

  protected readonly adminProvisioningMessage =
    'Admin accounts are provisioned by super-admins. Use your assigned credentials to sign in.';

  protected readonly form = new FormGroup<LoginForm>({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  constructor() {
    const routeContext = this.route.snapshot.data['authContext'];
    this.authContext.set(routeContext === 'admin' ? 'admin' : 'user');
  }

  protected get isAdminContext(): boolean {
    return this.authContext() === 'admin';
  }

  protected async submit(): Promise<void> {
    this.submitted.set(true);
    this.submitError.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    try {
      const formValue = this.form.getRawValue();

      const response = await firstValueFrom(
        this.authService.authenticate({
          email: formValue.email,
          password: formValue.password
        })
      );

      const accessToken = response?.body?.accessToken;
      if (accessToken) {
        this.authService.storeAccessToken(accessToken);
      }

      if (!response?.body?.mfaRequired) {
        this.authService.setMfaPending(false);
        this.router.navigate(['/mfa-setup'], {
          state: { email: formValue.email }
        });
        return;
      } else if (response?.body?.mfaRequired) {
        this.authService.setMfaPending(true);
        this.router.navigate(['/verify-mfa']);
        return;
      }
      // MFA already enabled and verified — proceed to dashboard
      this.router.navigate(['/dashboard']);
    } catch {
      this.submitError.set('Authentication failed. Please check your credentials and try again.');
    } finally {
      this.isSubmitting.set(false);
    }
  }
}
