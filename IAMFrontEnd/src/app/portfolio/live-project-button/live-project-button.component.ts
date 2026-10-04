import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-live-project-button',
  templateUrl: './live-project-button.component.html',
  styleUrl: './live-project-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LiveProjectButtonComponent {}
