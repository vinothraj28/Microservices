import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  OnInit,
  inject,
  input,
  signal,
} from '@angular/core';

@Component({
  selector: 'app-magnet',
  template: '<ng-content />',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[style.transform]': 'transform()',
    '[style.transition]': 'transition()',
    '[style.will-change]': '"transform"',
    style: 'display: block;',
  },
})
export class MagnetComponent implements OnInit, OnDestroy {
  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly zone = inject(NgZone);

  readonly padding = input(150);
  readonly strength = input(3);
  readonly activeTransition = input('transform 0.3s ease-out');
  readonly inactiveTransition = input('transform 0.6s ease-in-out');

  protected readonly transform = signal('translate3d(0, 0, 0)');
  protected readonly transition = signal(this.inactiveTransition());

  private removeListener?: () => void;

  ngOnInit(): void {
    this.zone.runOutsideAngular(() => {
      const onMove = (event: MouseEvent) => this.track(event);
      window.addEventListener('mousemove', onMove, { passive: true });
      this.removeListener = () => window.removeEventListener('mousemove', onMove);
    });
  }

  ngOnDestroy(): void {
    this.removeListener?.();
  }

  private track(event: MouseEvent): void {
    const rect = this.host.nativeElement.getBoundingClientRect();
    const pad = this.padding();
    const inside =
      event.clientX >= rect.left - pad &&
      event.clientX <= rect.right + pad &&
      event.clientY >= rect.top - pad &&
      event.clientY <= rect.bottom + pad;

    if (!inside) {
      this.transition.set(this.inactiveTransition());
      this.transform.set('translate3d(0, 0, 0)');
      return;
    }

    const dx = (event.clientX - (rect.left + rect.width / 2)) / this.strength();
    const dy = (event.clientY - (rect.top + rect.height / 2)) / this.strength();
    this.transition.set(this.activeTransition());
    this.transform.set(`translate3d(${dx}px, ${dy}px, 0)`);
  }
}
