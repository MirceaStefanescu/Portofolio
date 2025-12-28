package com.mircea.portofolio.ecommerce.repository;

import com.mircea.portofolio.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
