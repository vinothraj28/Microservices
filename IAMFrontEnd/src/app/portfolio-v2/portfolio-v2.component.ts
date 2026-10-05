import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { SkillsComponent } from './skills/skills.component';
import { ProjectsComponent } from './projects/projects.component';
import { ContactComponent } from './contact/contact.component';

interface Star {
  x: number;
  y: number;
  vx: number;
  vy: number;
  radius: number;
}

@Component({
  selector: 'app-portfolio-v2',
  imports: [
    RouterLink,
    RouterOutlet,
    AboutComponent,
    SkillsComponent,
    ProjectsComponent,
    ContactComponent,
  ],
  templateUrl: './portfolio-v2.component.html',
  styleUrl: './portfolio-v2.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PortfolioV2Component implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('constellationCanvas')
  private readonly canvas?: ElementRef<HTMLCanvasElement>;

  private animationFrame?: number;
  private context?: CanvasRenderingContext2D;
  private stars: Star[] = [];
  private pointer = { x: -500, y: -500 };
  private reduceMotion = false;

  ngOnInit(): void {
    document.documentElement.classList.add('portfolio-theme');
  }

  ngAfterViewInit(): void {
    this.reduceMotion = window.matchMedia(
      '(prefers-reduced-motion: reduce)',
    ).matches;
    this.resizeCanvas();

    if (!this.reduceMotion) {
      this.animate();
    }
  }

  ngOnDestroy(): void {
    document.documentElement.classList.remove('portfolio-theme');
    if (this.animationFrame) {
      cancelAnimationFrame(this.animationFrame);
    }
  }

  @HostListener('window:resize')
  onResize(): void {
    this.resizeCanvas();
  }

  onPointerMove(event: PointerEvent): void {
    const canvas = this.canvas?.nativeElement;
    if (!canvas) {
      return;
    }

    const bounds = canvas.getBoundingClientRect();
    console.log('Pointer move:', event.clientX, event.clientY);
    this.pointer = {
      x: event.clientX - bounds.left,
      y: event.clientY - bounds.top,
    };
    console.log(
      'Pointer position relative to canvas:',
      this.pointer.x,
      this.pointer.y,
    );
  }

  onPointerLeave(): void {
    this.pointer = { x: -500, y: -500 };
  }

  private resizeCanvas(): void {
    const canvas = this.canvas?.nativeElement;
    if (!canvas) {
      return;
    }

    const bounds = canvas.getBoundingClientRect();
    const pixelRatio = Math.min(window.devicePixelRatio || 1, 2);
    canvas.width = bounds.width * pixelRatio;
    canvas.height = bounds.height * pixelRatio;
    this.context = canvas.getContext('2d') ?? undefined;
    this.context?.scale(pixelRatio, pixelRatio);
    this.createStars(bounds.width, bounds.height);
    //this.draw(bounds.width, bounds.height);
  }

  private createStars(width: number, height: number): void {
    const count = width < 700 ? 34 : 68;
    this.stars = Array.from({ length: count }, () => ({
      x: Math.random() * width,
      y: Math.random() * height,
      vx: (Math.random() - 0.5) * 0.19,
      vy: (Math.random() - 0.5) * 0.19,
      radius: Math.random() * 1.15 + 0.45,
    }));
  }

  private animate(): void {
    const canvas = this.canvas?.nativeElement;
    if (!canvas) {
      return;
    }

    const bounds = canvas.getBoundingClientRect();
    this.stars.forEach((star) => {
      star.x += star.vx;
      star.y += star.vy;

      if (star.x < 0 || star.x > bounds.width) star.vx *= -1;
      if (star.y < 0 || star.y > bounds.height) star.vy *= -1;
    });
    this.draw(bounds.width, bounds.height);
    this.animationFrame = requestAnimationFrame(() => this.animate());
  }

  private draw(width: number, height: number): void {
    if (!this.context) {
      return;
    }

    const connectionDistance = width < 700 ? 105 : 145;
    this.context.clearRect(0, 0, width, height);

    this.stars.forEach((star, index) => {
      for (
        let targetIndex = index + 1;
        targetIndex < this.stars.length;
        targetIndex += 1
      ) {
        const target = this.stars[targetIndex];
        const distance = Math.hypot(star.x - target.x, star.y - target.y);
        if (distance < connectionDistance) {
          this.context!.strokeStyle = `rgba(75, 132, 255, ${(1 - distance / connectionDistance) * 0.26})`;
          this.context!.lineWidth = 0.55;
          this.context!.beginPath();
          this.context!.moveTo(star.x, star.y);
          this.context!.lineTo(target.x, target.y);
          this.context!.stroke();
        }
      }

      const pointerDistance = Math.hypot(
        star.x - this.pointer.x,
        star.y - this.pointer.y,
      );
      const emphasis = pointerDistance < 140 ? 1 - pointerDistance / 140 : 0;
      this.context!.fillStyle = `rgba(111, 158, 255, ${0.48 + emphasis * 0.45})`;
      this.context!.beginPath();
      this.context!.arc(
        star.x,
        star.y,
        star.radius + emphasis * 1.4,
        0,
        Math.PI * 2,
      );
      this.context!.fill();
    });
  }
}
