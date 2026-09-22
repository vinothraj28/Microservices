import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShowMainComponent } from './show-main.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('ShowMainComponent', () => {
  let component: ShowMainComponent;
  let fixture: ComponentFixture<ShowMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowMainComponent, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
