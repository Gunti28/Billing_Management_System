package com.srnrit.BMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnrit.BMS.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
	
	List<Product> findByInStock(Boolean inStock);

	List<Product> findByProductNameContainingIgnoreCase(String productName);
	boolean existsByProductNameIgnoreCase(String productName);
}
