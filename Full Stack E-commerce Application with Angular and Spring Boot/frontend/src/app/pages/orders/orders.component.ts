import { AsyncPipe, CurrencyPipe, DatePipe, NgFor, NgIf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { OrderService } from '../../core/services/order.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [AsyncPipe, CurrencyPipe, DatePipe, NgFor, NgIf],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  private orderService = inject(OrderService);
  orders$ = this.orderService.listOrders();
}
