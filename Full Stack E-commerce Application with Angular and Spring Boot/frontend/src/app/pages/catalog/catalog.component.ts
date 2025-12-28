import { AsyncPipe, CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
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
  products$ = this.productService.getProducts();

  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {}

  addToCart(product: Product): void {
    this.cartService.add(product);
  }
}
