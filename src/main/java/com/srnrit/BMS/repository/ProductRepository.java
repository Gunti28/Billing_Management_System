package com.srnrit.BMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnrit.BMS.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

}
