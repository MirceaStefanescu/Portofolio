import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component, computed, OnDestroy, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DEFAULT_CURRENCY, STRIPE_PUBLISHABLE_KEY } from '../../core/config/app-config';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';

declare const Stripe: (key: string) => any;

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, RouterLink],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit, OnDestroy {
  status = signal<'idle' | 'loading' | 'ready' | 'processing' | 'success' | 'error'>('idle');
  errorMessage = signal<string | null>(null);
  fatalError = signal(false);

  items = this.cartService.items;
  total = computed(() =>
    this.items().reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    )
  );

  private stripe: any;
  private card: any;
  private clientSecret: string | null = null;
  private orderId: number | null = null;

  constructor(
    private cartService: CartService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    if (!this.items().length) {
      this.status.set('error');
      this.fatalError.set(true);
      this.errorMessage.set('Your cart is empty.');
      return;
    }

    if (!STRIPE_PUBLISHABLE_KEY || STRIPE_PUBLISHABLE_KEY === 'pk_test_replace_me') {
      this.status.set('error');
      this.fatalError.set(true);
      this.errorMessage.set('Stripe publishable key is not configured.');
      return;
    }

    if (typeof Stripe === 'undefined') {
      this.status.set('error');
      this.fatalError.set(true);
      this.errorMessage.set('Stripe.js failed to load.');
      return;
    }

    this.status.set('loading');

    const payload = {
      items: this.items().map((item) => ({
        productId: item.product.id,
        quantity: item.quantity
      })),
      currency: DEFAULT_CURRENCY
    };

    this.orderService.createOrder(payload).subscribe({
      next: (order) => {
        this.orderId = order.id;
        this.clientSecret = order.clientSecret ?? null;
        if (!this.clientSecret) {
          this.status.set('error');
          this.fatalError.set(true);
          this.errorMessage.set('Stripe client secret was not returned.');
          return;
        }
        this.initStripe();
        this.status.set('ready');
      },
      error: (error) => {
        this.status.set('error');
        this.fatalError.set(true);
        this.errorMessage.set(error?.error?.message ?? 'Unable to start checkout.');
      }
    });
  }

  async pay(): Promise<void> {
    if (!this.stripe || !this.card || !this.clientSecret || !this.orderId) {
      return;
    }

    this.status.set('processing');
    this.errorMessage.set(null);

    const result = await this.stripe.confirmCardPayment(this.clientSecret, {
      payment_method: {
        card: this.card
      }
    });

    if (result.error) {
      this.status.set('error');
      this.errorMessage.set(result.error.message ?? 'Payment failed.');
      return;
    }

    const paymentIntent = result.paymentIntent;
    if (!paymentIntent) {
      this.status.set('error');
      this.errorMessage.set('Payment intent was not returned.');
      return;
    }

    this.orderService.confirmPayment(this.orderId, paymentIntent.id).subscribe({
      next: () => {
        this.status.set('success');
        this.cartService.clear();
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('Payment succeeded, but the order could not be updated.');
      }
    });
  }

  ngOnDestroy(): void {
    if (this.card) {
      this.card.destroy();
    }
  }

  private initStripe(): void {
    this.stripe = Stripe(STRIPE_PUBLISHABLE_KEY);
    const elements = this.stripe.elements();
    this.card = elements.create('card');
    this.card.mount('#card-element');
  }
}
