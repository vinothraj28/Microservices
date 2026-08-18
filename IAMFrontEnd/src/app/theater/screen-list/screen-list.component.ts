import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { inject } from '@angular/core';
import {
  ScreenService,
  screenResponse,
} from '../../core/services/screen/screen.service';
import { __await } from 'tslib';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-screen-list',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './screen-list.component.html',
  styleUrls: ['./screen-list.component.css'],
})
export class ScreenListComponent implements OnInit {
  protected theaterId: string | null = null;
  private route = inject(ActivatedRoute);
  private screenService = inject(ScreenService);
  protected screens: screenResponse[] = [];

  searchTerm = '';

  activeMenu: string | null = null;

  ngOnInit(): void {
    this.theaterId = this.route.snapshot.paramMap.get('theaterId');
    if (this.theaterId) {
      console.log('Theater ID:', this.theaterId);
      this.loadScreensForTheater(this.theaterId);
    } else {
      console.log('No Theater ID found in the route parameters.');
    }
  }

  protected async loadScreensForTheater(theaterId: string): Promise<void> {
    console.log(`Loading screens for theater ID: ${theaterId}`);
    try {
      const response = await firstValueFrom(
        this.screenService.listScreensForTheater(theaterId),
      );
      console.log('Screens loaded:', response);
      this.screens = response.body || [];
    } catch (error) {
      console.error('Error loading screens:', error);
    }
  }

  deleteScreenById(screenId: string): void {
    console.log(`Deleting screen with ID: ${screenId}`);
    // Implement the logic to delete the screen using the provided screenId
  }

  get filteredScreens(): screenResponse[] {
    const search = (this.searchTerm || '').trim().toLowerCase();

    if (!search) {
      return this.screens;
    }

    return this.screens.filter(
      (screen) =>
        screen.screenName.toLowerCase().includes(search) ||
        screen.screenType.toLowerCase().includes(search) ||
        screen.screenId.toLowerCase().includes(search),
    );
  }

  get totalCapacity(): number {
    return this.screens.reduce((total, screen) => total + screen.totalSeats, 0);
  }

  get regularScreens(): number {
    return this.screens.filter((screen) => screen.screenType === 'REGULAR')
      .length;
  }

  toggleMenu(screenId: string): void {
    this.activeMenu = this.activeMenu === screenId ? null : screenId;
  }

  addScreen(): void {
    this.activeMenu = null;

    // Navigate to your create screen page.
    //
    // Example:
    // this.router.navigate(['/screens/new']);

    console.log('Add screen');
  }

  editScreen(screen: screenResponse): void {
    this.activeMenu = null;
    

    // Navigate to edit screen.
    //
    // Example:
    // this.router.navigate(['/screens', screen.id, 'edit']);

    console.log('Edit screen:', screen);
  }

  deleteScreen(screen: screenResponse): void {
    this.activeMenu = null;

    const confirmed = window.confirm(
      `Are you sure you want to delete "${screen.screenName}"?`,
    );

    if (!confirmed) {
      return;
    }

    // Replace this with your API call.
    this.screens = this.screens.filter(
      (item) => item.screenId !== screen.screenId,
    );

    console.log('Deleted screen:', screen.screenId);
  }

  trackByScreenId(index: number, screen: screenResponse): string {
    return screen.screenId;
  }
}
