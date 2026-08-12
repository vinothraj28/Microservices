import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenFormComponent } from './screen-form.component';

describe('ScreenFormComponent', () => {
  let component: ScreenFormComponent;
  let fixture: ComponentFixture<ScreenFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScreenFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScreenFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
