import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';

@Component({
  selector: 'app-animated-text',
  templateUrl: './animated-text.component.html',
  styleUrl: './animated-text.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AnimatedTextComponent implements AfterViewInit, OnDestroy {
  private readonly zone = inject(NgZone);

  readonly text = input.required<string>();

  private readonly root = viewChild.required<ElementRef<HTMLParagraphElement>>('root');
  protected readonly chars = signal<string[]>([]);
  protected readonly opacities = signal<number[]>([]);

  private removeListener?: () => void;

  ngAfterViewInit(): void {
    const letters = Array.from(this.text());
    this.chars.set(letters);
    this.opacities.set(letters.map(() => 0.2));

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

  protected displayChar(char: string): string {
    return char === ' ' ? '\u00A0' : char;
  }

  private update(): void {
    const el = this.root().nativeElement;
    const rect = el.getBoundingClientRect();
    const vh = window.innerHeight;
    const start = vh * 0.8;
    const end = vh * 0.2;
    const distance = rect.height + start - end;
    const progress = clamp((start - rect.top) / distance, 0, 1);
    const n = this.chars().length || 1;

    this.opacities.set(
      this.chars().map((_, index) => {
        const charStart = index / n;
        const charEnd = (index + 1) / n;
        const local = clamp((progress - charStart) / (charEnd - charStart), 0, 1);
        return 0.2 + 0.8 * local;
      }),
    );
  }
}

function clamp(value: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, value));
}
