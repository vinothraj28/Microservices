import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TheaterListComponent } from '../theater-list/theater-list.component';
import { TheaterFormComponent } from '../theater-form/theater-form.component';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-theater-main',
  imports: [
    TheaterFormComponent,
    TheaterListComponent,
    MatIconModule,
    CommonModule,
  ],
  templateUrl: './theater-main.component.html',
  styleUrls: ['./theater-main.component.css'],
})
export class TheaterMainComponent {
  protected readonly selectComponent = signal<'form' | 'list'>('list');
}
