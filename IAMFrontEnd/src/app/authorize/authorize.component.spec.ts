import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorizeComponent } from './authorize.component';
import { AuthService } from '../core/services/auth/auth.service';

describe('AuthorizeComponent', () => {
  let component: AuthorizeComponent;
  let fixture: ComponentFixture<AuthorizeComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['authorize']);
    authServiceSpy.authorize.and.returnValue(Promise.resolve());

    await TestBed.configureTestingModule({
      imports: [AuthorizeComponent],
      providers: [{ provide: AuthService, useValue: authServiceSpy }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthorizeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start authorize flow on init', () => {
    expect(authServiceSpy.authorize).toHaveBeenCalled();
  });
});
