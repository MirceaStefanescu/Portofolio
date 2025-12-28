import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/app-config';
import { Product } from '../models/product';

@Injectable({ providedIn: 'root' })
export class ProductService {
  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${API_BASE_URL}/api/products`);
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${API_BASE_URL}/api/products/${id}`);
  }
}
