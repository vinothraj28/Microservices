import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  inject,
  signal,
  viewChildren,
} from '@angular/core';

import { LiveProjectButtonComponent } from '../live-project-button/live-project-button.component';
import { PROJECTS } from '../portfolio.data';

@Component({
  selector: 'app-projects-section',
  imports: [LiveProjectButtonComponent],
  templateUrl: './projects-section.component.html',
  styleUrl: './projects-section.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProjectsSectionComponent implements AfterViewInit, OnDestroy {
  private readonly zone = inject(NgZone);
  private readonly slots = viewChildren<ElementRef<HTMLElement>>('slot');

  protected readonly projects = PROJECTS;
  protected readonly scales = signal(PROJECTS.map(() => 1));

  private removeListener?: () => void;

  protected cardTop(index: number): string {
    return `calc(var(--sticky-top) + ${index * 28}px)`;
  }

  ngAfterViewInit(): void {
    this.zone.runOutsideAngular(() => {
      const onScroll = () => this.updateScales();
      window.addEventListener('scroll', onScroll, { passive: true });
      this.removeListener = () => window.removeEventListener('scroll', onScroll);
      this.updateScales();
    });
  }

  ngOnDestroy(): void {
    this.removeListener?.();
  }

  private updateScales(): void {
    const total = this.projects.length;
    const next = this.slots().map((slot, index) => {
      const targetScale = 1 - (total - 1 - index) * 0.03;
      const rect = slot.nativeElement.getBoundingClientRect();
      const progress = clamp(-rect.top / Math.max(rect.height, 1), 0, 1);
      return 1 - progress * (1 - targetScale);
    });
    this.scales.set(next);
  }
}

function clamp(value: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, value));
}
