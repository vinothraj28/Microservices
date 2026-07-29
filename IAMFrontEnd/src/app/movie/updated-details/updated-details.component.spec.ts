import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatedDetailsComponent } from './updated-details.component';

describe('UpdatedDetailsComponent', () => {
  let component: UpdatedDetailsComponent;
  let fixture: ComponentFixture<UpdatedDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatedDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatedDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
