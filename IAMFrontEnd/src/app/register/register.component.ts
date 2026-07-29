import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../core/services/auth/auth.service';

interface RegisterForm {
  userName: FormControl<string>;
  dateOfBirth: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RegisterComponent {
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);

  protected readonly isSubmitting = signal(false);
  protected readonly submitted = signal(false);
  protected readonly submitError = signal<string | null>(null);
  protected readonly submitSuccess = signal(false);
  protected readonly authContext = signal<'user' | 'admin'>('user');

  protected readonly form = new FormGroup<RegisterForm>({
    userName: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    dateOfBirth: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(8)]
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
    this.submitSuccess.set(false);

    if (this.isAdminContext) {
      this.submitError.set('Admin registration is restricted. Ask a super-admin to provision your account.');
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);

    try {
      const formValue = this.form.getRawValue();

      await firstValueFrom(
        this.authService.register({
          userName: formValue.userName,
          emailAddress: formValue.email,
          dob: formValue.dateOfBirth,
          password: formValue.password
        })
      );

      this.submitSuccess.set(true);
      this.form.reset({
        userName: '',
        dateOfBirth: '',
        email: '',
        password: ''
      });
      this.submitted.set(false);
    } catch {
      this.submitError.set('Registration failed. Please try again.');
    } finally {
      this.isSubmitting.set(false);
    }
  }
}
