import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortfolioV2Component } from './portfolio-v2.component';

describe('PortfolioV2Component', () => {
  let component: PortfolioV2Component;
  let fixture: ComponentFixture<PortfolioV2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioV2Component]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortfolioV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
