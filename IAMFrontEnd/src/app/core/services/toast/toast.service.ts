import { computed, Injectable, signal } from '@angular/core';

export interface Toast {
  message: string;
  type: 'success' | 'error';
  isVisible: boolean;
  confirmText?: string;
  cancelText?: string;
  onConfirm?: () => void;
  onCancel?: () => void;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  protected _toast = signal<Toast | null>(null);

  readonly toast = this._toast.asReadonly();

  private timeoutId: ReturnType<typeof setTimeout> | null = null;

  setToast(message: string, type: 'success' | 'error') {
    this._toast.set({ message, type, isVisible: true });
    this.setToastTimeout();
  }

  askForConfirmation(
    message: string,
    onConfirm: () => void,
    onCancel: () => void = () => {},
    confirmText = 'Confirm',
    cancelText = 'Cancel',
  ) {
    this.clearToast();
    this._toast.set({
      message,
      type: 'error',
      isVisible: true,
      confirmText,
      cancelText,
      onConfirm,
      onCancel,
    });
  }

  private setToastTimeout() {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
    this.timeoutId = setTimeout(() => {
      this.clearToast();
    }, 3000);
  }

  clearToast() {
    this._toast.set(null);
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
    }
  }

  confirm() {
    const currentToast = this._toast();
    this.clearToast();
    currentToast?.onConfirm?.();
  }

  cancel() {
    const currentToast = this._toast();
    this.clearToast();
    currentToast?.onCancel?.();
  }
}
