import { ChangeDetectionStrategy, Component } from '@angular/core';

import { ContactButtonComponent } from '../contact-button/contact-button.component';
import { FadeInComponent } from '../fade-in/fade-in.component';
import { MagnetComponent } from '../magnet/magnet.component';
import { HERO_PORTRAIT } from '../portfolio.data';

@Component({
  selector: 'app-hero-section',
  imports: [FadeInComponent, MagnetComponent, ContactButtonComponent],
  templateUrl: './hero-section.component.html',
  styleUrl: './hero-section.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HeroSectionComponent {
  protected readonly portrait = HERO_PORTRAIT;
}
