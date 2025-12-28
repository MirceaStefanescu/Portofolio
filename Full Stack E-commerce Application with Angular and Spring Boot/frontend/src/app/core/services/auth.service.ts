import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { API_BASE_URL } from '../config/app-config';
import { AuthRequest, AuthResponse, RegisterRequest } from '../models/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'auth_token';

  constructor(private http: HttpClient) {}

  register(payload: RegisterRequest) {
    return this.http
      .post<AuthResponse>(`${API_BASE_URL}/api/auth/register`, payload)
      .pipe(tap((response) => this.persistToken(response.token)));
  }

  login(payload: AuthRequest) {
    return this.http
      .post<AuthResponse>(`${API_BASE_URL}/api/auth/login`, payload)
      .pipe(tap((response) => this.persistToken(response.token)));
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private persistToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }
}
