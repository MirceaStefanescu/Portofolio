import { AsyncPipe, CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { CartService } from '../../core/services/cart.service';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../core/models/product';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [AsyncPipe, CurrencyPipe, NgFor, NgIf],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css'
})
export class CatalogComponent {
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  products$ = this.productService.getProducts();

  addToCart(product: Product): void {
    this.cartService.add(product);
  }
}
