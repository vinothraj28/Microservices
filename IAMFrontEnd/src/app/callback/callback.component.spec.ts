import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { CallbackComponent } from './callback.component';
import { AuthService } from '../core/services/auth/auth.service';

describe('CallbackComponent', () => {
  let component: CallbackComponent;
  let fixture: ComponentFixture<CallbackComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', [
      'readOauthSession',
      'exchangeCodeForToken',
      'storeAccessToken',
      'clearOauthSession',
      'setMfaPending'
    ]);
    authServiceSpy.readOauthSession.and.returnValue({ state: 'state-123', codeVerifier: 'verifier-123' });
    authServiceSpy.exchangeCodeForToken.and.returnValue(of({ accessToken: 'token-123' }));
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [CallbackComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: convertToParamMap({ code: 'code-123', state: 'state-123' })
            }
          }
        },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CallbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
