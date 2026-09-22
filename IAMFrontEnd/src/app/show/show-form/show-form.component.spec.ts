import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShowFormComponent } from './show-form.component';
import { ShowService } from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';
import { NavigationService } from '../../core/services/navigation/navigation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('ShowFormComponent', () => {
  let component: ShowFormComponent;
  let fixture: ComponentFixture<ShowFormComponent>;
  let mockShowService: jasmine.SpyObj<ShowService>;
  let mockToastService: jasmine.SpyObj<ToastService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockNavigationService: jasmine.SpyObj<NavigationService>;
  let mockActivatedRoute: any;

  beforeEach(async () => {
    mockShowService = jasmine.createSpyObj('ShowService', [
      'createShow',
      'updateShow',
      'getShowById',
    ]);
    mockToastService = jasmine.createSpyObj('ToastService', ['showToast']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockNavigationService = jasmine.createSpyObj('NavigationService', [
      'goBack',
    ]);
    mockActivatedRoute = {
      params: of({}),
    };

    await TestBed.configureTestingModule({
      imports: [ShowFormComponent],
      providers: [
        { provide: ShowService, useValue: mockShowService },
        { provide: ToastService, useValue: mockToastService },
        { provide: Router, useValue: mockRouter },
        { provide: NavigationService, useValue: mockNavigationService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with default values', () => {
    expect(component['showForm'].valid).toBeFalsy();
  });
});
