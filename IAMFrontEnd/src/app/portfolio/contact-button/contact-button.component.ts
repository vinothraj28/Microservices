import { ChangeDetectionStrategy, Component, input } from '@angular/core';

@Component({
  selector: 'app-contact-button',
  templateUrl: './contact-button.component.html',
  styleUrl: './contact-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ContactButtonComponent {
  readonly href = input('#contact');
}
