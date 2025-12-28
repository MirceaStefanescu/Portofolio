package com.mircea.portofolio.ecommerce.controller;

import com.mircea.portofolio.ecommerce.dto.AuthRequest;
import com.mircea.portofolio.ecommerce.dto.AuthResponse;
import com.mircea.portofolio.ecommerce.dto.RegisterRequest;
import com.mircea.portofolio.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return authService.register(request);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody AuthRequest request) {
		return authService.login(request);
	}
}
