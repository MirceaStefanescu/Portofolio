import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable, catchError, of, tap } from "rxjs";
import { environment } from "../environments/environment";
import { UserProfile } from "./models";

@Injectable({
  providedIn: "root"
})
export class AuthService {
  private readonly userSubject = new BehaviorSubject<UserProfile | null>(null);
  readonly user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadProfile(): Observable<UserProfile | null> {
    return this.http
      .get<UserProfile>(`${environment.apiBaseUrl}/api/me`, { withCredentials: true })
      .pipe(
        tap((profile) => this.userSubject.next(profile)),
        catchError(() => {
          this.userSubject.next(null);
          return of(null);
        })
      );
  }

  login(): void {
    window.location.href = `${environment.apiBaseUrl}/oauth2/authorization/github`;
  }

  logout(): void {
    window.location.href = `${environment.apiBaseUrl}/logout`;
  }
}
