import { ChangeDetectionStrategy, Component } from '@angular/core';

import { FadeInComponent } from '../fade-in/fade-in.component';
import { SERVICES } from '../portfolio.data';

@Component({
  selector: 'app-services-section',
  imports: [FadeInComponent],
  templateUrl: './services-section.component.html',
  styleUrl: './services-section.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ServicesSectionComponent {
  protected readonly services = SERVICES;
}
