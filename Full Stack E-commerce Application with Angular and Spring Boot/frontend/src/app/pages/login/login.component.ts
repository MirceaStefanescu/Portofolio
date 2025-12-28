import { NgIf } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  status = signal<'idle' | 'loading' | 'error' | 'success'>('idle');
  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.status.set('loading');
    this.errorMessage.set(null);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.status.set('success');
        this.router.navigate(['/catalog']);
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('Unable to sign in. Check credentials and try again.');
      }
    });
  }
}
