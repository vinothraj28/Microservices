import { ChangeDetectionStrategy, Component } from '@angular/core';

import { AnimatedTextComponent } from '../animated-text/animated-text.component';
import { ContactButtonComponent } from '../contact-button/contact-button.component';
import { FadeInComponent } from '../fade-in/fade-in.component';
import { ABOUT_COPY, ABOUT_DECOR } from '../portfolio.data';

@Component({
  selector: 'app-about-section',
  imports: [FadeInComponent, AnimatedTextComponent, ContactButtonComponent],
  templateUrl: './about-section.component.html',
  styleUrl: './about-section.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AboutSectionComponent {
  protected readonly decor = ABOUT_DECOR;
  protected readonly copy = ABOUT_COPY;
}
