import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieMainComponent } from './movie-main.component';

describe('MovieMainComponent', () => {
  let component: MovieMainComponent;
  let fixture: ComponentFixture<MovieMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieMainComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
