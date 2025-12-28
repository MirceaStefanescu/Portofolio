import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/app-config';

export interface PaymentIntentRequest {
  amount: number;
  currency: string;
}

export interface PaymentIntentResponse {
  clientSecret: string;
  paymentIntentId: string;
}

@Injectable({ providedIn: 'root' })
export class CheckoutService {
  constructor(private http: HttpClient) {}

  createPaymentIntent(payload: PaymentIntentRequest): Observable<PaymentIntentResponse> {
    return this.http.post<PaymentIntentResponse>(
      `${API_BASE_URL}/api/checkout/payment-intent`,
      payload
    );
  }
}
