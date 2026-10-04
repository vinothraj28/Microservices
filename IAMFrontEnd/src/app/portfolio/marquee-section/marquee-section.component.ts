import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  computed,
  inject,
  signal,
  viewChild,
} from '@angular/core';

import { MARQUEE_IMAGES } from '../portfolio.data';

@Component({
  selector: 'app-marquee-section',
  templateUrl: './marquee-section.component.html',
  styleUrl: './marquee-section.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MarqueeSectionComponent implements AfterViewInit, OnDestroy {
  private readonly zone = inject(NgZone);
  private readonly section = viewChild.required<ElementRef<HTMLElement>>('section');

  protected readonly rowOne = [...MARQUEE_IMAGES.slice(0, 11), ...MARQUEE_IMAGES.slice(0, 11), ...MARQUEE_IMAGES.slice(0, 11)];
  protected readonly rowTwo = [...MARQUEE_IMAGES.slice(11), ...MARQUEE_IMAGES.slice(11), ...MARQUEE_IMAGES.slice(11)];

  protected readonly offset = signal(0);
  protected readonly rowOneTransform = computed(
    () => `translateX(${this.offset() - 200}px)`,
  );
  protected readonly rowTwoTransform = computed(
    () => `translateX(${-(this.offset() - 200)}px)`,
  );

  private removeListener?: () => void;

  ngAfterViewInit(): void {
    this.zone.runOutsideAngular(() => {
      const onScroll = () => this.update();
      window.addEventListener('scroll', onScroll, { passive: true });
      this.removeListener = () => window.removeEventListener('scroll', onScroll);
      this.update();
    });
  }

  ngOnDestroy(): void {
    this.removeListener?.();
  }

  private update(): void {
    const top = this.section().nativeElement.getBoundingClientRect().top + window.scrollY;
    this.offset.set((window.scrollY - top + window.innerHeight) * 0.3);
  }
}
