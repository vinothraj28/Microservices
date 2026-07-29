import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { DashboardComponent } from './dashboard.component';
import { AuthService } from '../core/services/auth/auth.service';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', [
      'getAccessToken',
      'isMfaPending',
      'clearSession'
    ]);
    authServiceSpy.getAccessToken.and.returnValue(null);
    authServiceSpy.isMfaPending.and.returnValue(false);

    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [provideRouter([]), { provide: AuthService, useValue: authServiceSpy }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
