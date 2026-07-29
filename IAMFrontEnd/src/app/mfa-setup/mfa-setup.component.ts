import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../core/services/auth/auth.service';

interface MfaConfirmForm {
  code: FormControl<string>;
}

@Component({
  selector: 'app-mfa-setup',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './mfa-setup.component.html',
  styleUrl: './mfa-setup.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MfaSetupComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly email = signal<string>('');
  protected readonly qrCodeUrl = signal<string | null>(null);
  protected readonly isLoadingQr = signal(true);
  protected readonly qrError = signal<string | null>(null);

  protected readonly isSubmitting = signal(false);
  protected readonly submitted = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly successMessage = signal<string | null>(null);

  protected readonly form = new FormGroup<MfaConfirmForm>({
    code: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{6}$/)]
    })
  });

  ngOnInit(): void {
    const state = history.state as { email?: string } | undefined;
    const email = state?.email ?? '';

    if (!email) {
      this.router.navigate(['/login']);
      return;
    }

    this.email.set(email);
    this.loadQrCode(email);
  }

  private async loadQrCode(email: string): Promise<void> {
    const token = this.authService.getAccessToken();
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    try {
      const response = await firstValueFrom(
        this.authService.setupMfa({ emailAddress: email }, token)
      );
      this.qrCodeUrl.set(`data:image/png;base64,${response.qrCodeUrl}`);
    } catch {
      this.qrError.set('Failed to load QR code. Please try again.');
    } finally {
      this.isLoadingQr.set(false);
    }
  }

  protected async confirm(): Promise<void> {
    this.submitted.set(true);
    this.submitError.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const token = this.authService.getAccessToken();
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    this.isSubmitting.set(true);

    try {
      await firstValueFrom(
        this.authService.confirmMfa(
          { emailAddress: this.email(), code: this.form.getRawValue().code },
          token
        )
      );
      this.successMessage.set('MFA setup successful! You can now use your authenticator app to log in.');
      this.authService.setMfaPending(false);
      setTimeout(() => {
        this.router.navigate(['/dashboard']);
      }, 3000);
    } catch {
      this.submitError.set('Invalid verification code. Please try again.');
    } finally {
      this.isSubmitting.set(false);
    }
  }
}
