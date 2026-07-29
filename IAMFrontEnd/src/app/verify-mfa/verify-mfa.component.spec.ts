import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { VerifyMfaComponent } from './verify-mfa.component';
import { AuthService } from '../core/services/auth/auth.service';

describe('VerifyMfaComponent', () => {
  let component: VerifyMfaComponent;
  let fixture: ComponentFixture<VerifyMfaComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', [
      'verifyMfa',
      'isAllowedPostLoginRedirect',
      'setMfaPending',
      'clearSession'
    ]);
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [VerifyMfaComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyMfaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
