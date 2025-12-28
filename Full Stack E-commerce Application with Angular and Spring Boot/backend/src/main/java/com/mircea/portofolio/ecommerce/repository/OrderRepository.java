package com.mircea.portofolio.ecommerce.repository;

import com.mircea.portofolio.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
