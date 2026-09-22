import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-show-main',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './show-main.component.html',
  styleUrls: ['./show-main.component.css'],
})
export class ShowMainComponent {}
