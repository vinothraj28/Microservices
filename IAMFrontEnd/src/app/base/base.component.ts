import { Component } from '@angular/core';
import { RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-base',
  imports: [RouterLink, RouterOutlet, RouterLinkActive],
  templateUrl: './base.component.html',
  styleUrl: './base.component.css'
})
export class BaseComponent {
  protected readonly currentYear = new Date().getFullYear();

}
