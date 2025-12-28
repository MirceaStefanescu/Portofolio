import { NgIf } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  status = signal<'idle' | 'loading' | 'error' | 'success'>('idle');
  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.status.set('loading');
    this.errorMessage.set(null);

    this.authService.register(this.form.getRawValue()).subscribe({
      next: () => {
        this.status.set('success');
        this.router.navigate(['/catalog']);
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('Unable to create account. Try a different email.');
      }
    });
  }
}
