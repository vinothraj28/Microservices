import { Component, signal, input, output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../core/services/toast/toast.service';

@Component({
  selector: 'app-toast',
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
})
export class ToastComponent {
  protected toastService = inject(ToastService);

  protected toast = this.toastService.toast;

  closeToast() {
    this.toastService.clearToast();
  }
  
  confirmAction() {
    this.toastService.confirm();
  }

  cancelAction() {
    this.toastService.cancel();
  }
}
