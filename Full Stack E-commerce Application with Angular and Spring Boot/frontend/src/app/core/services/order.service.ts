import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/app-config';
import { CreateOrderRequest, OrderResponse } from '../models/order';

@Injectable({ providedIn: 'root' })
export class OrderService {
  constructor(private http: HttpClient) {}

  createOrder(payload: CreateOrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${API_BASE_URL}/api/orders`, payload);
  }

  confirmPayment(orderId: number, paymentIntentId: string): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${API_BASE_URL}/api/orders/${orderId}/confirm`, {
      paymentIntentId
    });
  }

  listOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(`${API_BASE_URL}/api/orders`);
  }
}
