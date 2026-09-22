import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowListV2Component } from './show-list-v2.component';

describe('ShowListV2Component', () => {
  let component: ShowListV2Component;
  let fixture: ComponentFixture<ShowListV2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowListV2Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowListV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
