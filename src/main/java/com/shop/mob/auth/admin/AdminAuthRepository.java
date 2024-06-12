package com.shop.mob.auth.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAuthRepository extends  JpaRepository<Admin, Long>{
    Optional<Admin> findAdminByEmail(String email);
}
