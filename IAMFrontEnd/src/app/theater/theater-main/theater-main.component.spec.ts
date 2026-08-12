import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TheaterMainComponent } from './theater-main.component';

describe('TheaterMainComponent', () => {
  let component: TheaterMainComponent;
  let fixture: ComponentFixture<TheaterMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TheaterMainComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TheaterMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
