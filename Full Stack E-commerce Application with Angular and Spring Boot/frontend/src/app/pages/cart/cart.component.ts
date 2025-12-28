import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CurrencyPipe, NgFor, NgIf, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  items = this.cartService.items;
  total = computed(() =>
    this.items().reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    )
  );

  constructor(private cartService: CartService) {}

  updateQuantity(productId: number, quantity: number): void {
    this.cartService.updateQuantity(productId, quantity);
  }

  remove(productId: number): void {
    this.cartService.remove(productId);
  }
}
