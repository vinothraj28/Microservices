import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShowListComponent } from './show-list.component';
import { ShowService } from '../../core/services/show';
import { ToastService } from '../../core/services/toast/toast.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MovieServiceService } from '../../core/services/movie/movie-service.service';
import { TheaterService } from '../../core/services/theater/theater.service';

describe('ShowListComponent', () => {
  let component: ShowListComponent;
  let fixture: ComponentFixture<ShowListComponent>;
  let mockShowService: jasmine.SpyObj<ShowService>;
  let mockToastService: jasmine.SpyObj<ToastService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockMovieService: jasmine.SpyObj<MovieServiceService>;
  let mockTheaterService: jasmine.SpyObj<TheaterService>;
  let mockActivatedRoute: any;

  beforeEach(async () => {
    mockShowService = jasmine.createSpyObj('ShowService', [
      'getShowsByMovie',
      'getShowsByTheater',
      'deleteShowById',
      'formatShowDateTime',
    ]);
    mockToastService = jasmine.createSpyObj('ToastService', [
      'showToast',
      'askForConfirmation',
    ]);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockMovieService = jasmine.createSpyObj('MovieServiceService', [
      'getMovieList',
    ]);
    mockTheaterService = jasmine.createSpyObj('TheaterService', [
      'getTheaterList',
    ]);
    mockMovieService.getMovieList.and.returnValue(
      of({
        movieResponseDTO: [],
        totalPages: 0,
        totalCount: 0,
        page: 0,
        size: 100,
        hasNext: false,
      }),
    );
    mockTheaterService.getTheaterList.and.returnValue(
      of({
        theaters: [],
        totalPages: 0,
        totalCount: 0,
        hasNext: false,
      }),
    );
    mockActivatedRoute = {
      queryParams: of({}),
    };

    await TestBed.configureTestingModule({
      imports: [ShowListComponent],
      providers: [
        { provide: ShowService, useValue: mockShowService },
        { provide: ToastService, useValue: mockToastService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MovieServiceService, useValue: mockMovieService },
        { provide: TheaterService, useValue: mockTheaterService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
