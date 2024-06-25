package com.shop.mob.product.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);
}
