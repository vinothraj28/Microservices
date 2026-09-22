import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SeatSelectionComponent } from './seat-selection.component';
import { ShowService } from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('SeatSelectionComponent', () => {
  let component: SeatSelectionComponent;
  let fixture: ComponentFixture<SeatSelectionComponent>;
  let mockShowService: jasmine.SpyObj<ShowService>;
  let mockToastService: jasmine.SpyObj<ToastService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockActivatedRoute: any;

  beforeEach(async () => {
    mockShowService = jasmine.createSpyObj('ShowService', [
      'getShowById',
      'getAvailableSeats',
      'calculateSeatPrice',
      'formatShowDateTime',
      'getSeatsGroupedByStatus',
      'getSeatsGroupedByType',
    ]);
    mockToastService = jasmine.createSpyObj('ToastService', ['showToast']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockActivatedRoute = {
      params: of({}),
    };

    await TestBed.configureTestingModule({
      imports: [SeatSelectionComponent],
      providers: [
        { provide: ShowService, useValue: mockShowService },
        { provide: ToastService, useValue: mockToastService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SeatSelectionComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
