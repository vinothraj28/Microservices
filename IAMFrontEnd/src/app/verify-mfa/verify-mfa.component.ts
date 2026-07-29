import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../core/services/auth/auth.service';


interface MfaVerifyForm {
  code: FormControl<string>;
}

@Component({
  selector: 'verify-mfa',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './verify-mfa.component.html',
  styleUrls: ['./verify-mfa.component.css']
})
export class VerifyMfaComponent  {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly isSubmitting = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly successMessage = signal<string | null>(null);

  protected readonly form = new FormGroup<MfaVerifyForm>({
    code: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{6}$/)]
    })
  });

  protected async confirm(): Promise<void> {
    this.submitError.set(null);
    this.successMessage.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    try {
      const response = await firstValueFrom(
        this.authService.verifyMfa({
          mfaCode: this.form.getRawValue().code
        })
      );
      if (response.status === 200) {
        const redirectUrl = response.body?.redirectUrl;

        if (redirectUrl && this.authService.isAllowedPostLoginRedirect(redirectUrl)) {
          this.authService.setMfaPending(false);
          window.location.assign(redirectUrl);
          return;
        }

        //this.successMessage.set('MFA verification successful. Redirecting to dashboard.');
        //this.authService.setMfaPending(false);
        //this.router.navigate(['/dashboard']);
      } else if (response.status === 302) {
        const redirectUrl = response.headers.get('Location');

        if (redirectUrl && this.authService.isAllowedPostLoginRedirect(redirectUrl)) {
          this.authService.setMfaPending(false);
          window.location.assign(redirectUrl);
          return;
        }

        this.submitError.set('MFA verified, but redirect URL was blocked for safety.');
      } else {
        this.submitError.set('An error occurred while verifying MFA.');
      }
    } catch (error) {
      if (error instanceof HttpErrorResponse) {
        switch (error.status) {
          case 400:
            this.submitError.set('Invalid MFA code. Please try again.');
            break;
          case 401:
            this.authService.clearSession();
            this.submitError.set('Unauthorized. Please log in again.');
            this.router.navigate(['/login']);
            break;
          default:
            this.submitError.set('An error occurred while verifying MFA.');
            break;
        }
      } else {
        this.submitError.set('An unexpected error occurred.');
      }
    } finally {
      this.isSubmitting.set(false);
    }
  }
}
