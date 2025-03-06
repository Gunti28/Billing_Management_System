package com.srnrit.BMS.dao.impl;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.repository.ProductRepository;

@Repository
public class ProductDaoImpl implements ProductDao {
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public Optional<Product> saveProduct(Product product, String categoryId) {
		return Optional.empty();
	}

	@Override
	public Optional<Product> fetchProductByAvailability(Boolean inStock) {
		return Optional.empty();
	}

	@Override
	public Optional<Product> deleteProductById(String productId) {
		return Optional.empty();
	}

	@Override
	public Optional<Product> updateProduct(Product product) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Product> searchProductByName(String productName) {
		return Optional.empty();
	}

	@Override
	public Optional<List<Product>> fetchAllProduct() {
		return Optional.empty();
	}

}
