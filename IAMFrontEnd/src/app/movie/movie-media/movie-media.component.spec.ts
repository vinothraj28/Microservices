import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieMediaComponent } from './movie-media.component';

describe('MovieMediaComponent', () => {
  let component: MovieMediaComponent;
  let fixture: ComponentFixture<MovieMediaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieMediaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovieMediaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
