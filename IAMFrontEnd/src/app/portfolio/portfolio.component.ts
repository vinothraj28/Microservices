import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  inject,
} from '@angular/core';
import { Title } from '@angular/platform-browser';

import { AboutSectionComponent } from './about-section/about-section.component';
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { MarqueeSectionComponent } from './marquee-section/marquee-section.component';
import { ProjectsSectionComponent } from './projects-section/projects-section.component';
import { ServicesSectionComponent } from './services-section/services-section.component';

@Component({
  selector: 'app-portfolio',
  imports: [
    HeroSectionComponent,
    MarqueeSectionComponent,
    AboutSectionComponent,
    ServicesSectionComponent,
    ProjectsSectionComponent,
  ],
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PortfolioComponent implements OnInit, OnDestroy {
  private readonly title = inject(Title);

  ngOnInit(): void {
    this.title.setTitle('Jack -- 3D Creator');
    document.documentElement.classList.add('portfolio-theme');
    document.body.classList.add('portfolio-theme');
  }

  ngOnDestroy(): void {
    this.title.setTitle('IAMFrontEnd');
    document.documentElement.classList.remove('portfolio-theme');
    document.body.classList.remove('portfolio-theme');
  }
}
