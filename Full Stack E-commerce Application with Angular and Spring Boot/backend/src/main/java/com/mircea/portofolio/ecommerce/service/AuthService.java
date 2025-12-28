package com.mircea.portofolio.ecommerce.service;

import com.mircea.portofolio.ecommerce.dto.AuthRequest;
import com.mircea.portofolio.ecommerce.dto.AuthResponse;
import com.mircea.portofolio.ecommerce.dto.RegisterRequest;
import com.mircea.portofolio.ecommerce.model.User;
import com.mircea.portofolio.ecommerce.repository.UserRepository;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest request) {
		validateRegister(request);
		String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
		if (userRepository.findByEmail(normalizedEmail).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}
		User user = new User(normalizedEmail, passwordEncoder.encode(request.password()), request.fullName());
		userRepository.save(user);
		return buildResponse(user);
	}

	public AuthResponse login(AuthRequest request) {
		validateRequest(request.email(), request.password());
		String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
		User user = userRepository.findByEmail(normalizedEmail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
		return buildResponse(user);
	}

	private void validateRequest(String email, String password) {
		if (email == null || email.isBlank() || password == null || password.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
		}
	}

	private void validateRegister(RegisterRequest request) {
		validateRequest(request.email(), request.password());
		if (request.fullName() == null || request.fullName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required");
		}
	}

	private AuthResponse buildResponse(User user) {
		JwtToken token = jwtService.generateToken(user.getEmail());
		return new AuthResponse(token.token(), token.expiresAt(), user.getEmail());
	}
}
