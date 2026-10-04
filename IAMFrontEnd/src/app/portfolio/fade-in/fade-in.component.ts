import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ElementRef,
  inject,
  input,
  signal,
} from '@angular/core';

@Component({
  selector: 'app-fade-in',
  template: '<ng-content />',
  styleUrl: './fade-in.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.is-visible]': 'visible()',
    '[style.--fade-delay]': 'delay() + "s"',
    '[style.--fade-duration]': 'duration() + "s"',
    '[style.--fade-x]': 'x() + "px"',
    '[style.--fade-y]': 'y() + "px"',
  },
})
export class FadeInComponent implements AfterViewInit {
  private readonly host = inject(ElementRef<HTMLElement>);
  private readonly destroyRef = inject(DestroyRef);
  private observer?: IntersectionObserver;

  readonly delay = input(0);
  readonly duration = input(0.7);
  readonly x = input(0);
  readonly y = input(30);

  protected readonly visible = signal(false);

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          this.visible.set(true);
          this.observer?.disconnect();
        }
      },
      { rootMargin: '50px', threshold: 0 },
    );

    this.observer.observe(this.host.nativeElement);
    this.destroyRef.onDestroy(() => this.observer?.disconnect());
  }
}
