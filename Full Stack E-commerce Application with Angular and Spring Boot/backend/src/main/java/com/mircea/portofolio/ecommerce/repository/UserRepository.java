package com.mircea.portofolio.ecommerce.repository;

import com.mircea.portofolio.ecommerce.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
