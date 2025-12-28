import { computed, effect, Injectable, signal } from '@angular/core';
import { CartItem } from '../models/cart-item';
import { Product } from '../models/product';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly storageKey = 'cart_items';
  private readonly itemsSignal = signal<CartItem[]>([]);
  readonly items = computed(() => this.itemsSignal());

  constructor() {
    const stored = localStorage.getItem(this.storageKey);
    if (stored) {
      try {
        const parsed = JSON.parse(stored) as CartItem[];
        this.itemsSignal.set(parsed);
      } catch {
        localStorage.removeItem(this.storageKey);
      }
    }

    effect(() => {
      localStorage.setItem(this.storageKey, JSON.stringify(this.itemsSignal()));
    });
  }

  add(product: Product): void {
    const items = this.itemsSignal();
    const existing = items.find((item) => item.product.id === product.id);
    if (existing) {
      this.itemsSignal.update((current) =>
        current.map((item) =>
          item.product.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
      return;
    }
    this.itemsSignal.update((current) => [...current, { product, quantity: 1 }]);
  }

  remove(productId: number): void {
    this.itemsSignal.update((current) =>
      current.filter((item) => item.product.id !== productId)
    );
  }

  updateQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) {
      this.remove(productId);
      return;
    }
    this.itemsSignal.update((current) =>
      current.map((item) =>
        item.product.id === productId ? { ...item, quantity } : item
      )
    );
  }

  clear(): void {
    this.itemsSignal.set([]);
  }
}
