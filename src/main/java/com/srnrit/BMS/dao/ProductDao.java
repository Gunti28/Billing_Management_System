package com.srnrit.BMS.dao;

import java.util.List;
import java.util.Optional;

import com.srnrit.BMS.entity.Product;

public interface ProductDao {

	Optional<Product> saveProduct(Product product,String categoryId);

	Optional<Product> fetchProductByAvailability(Boolean inStock);

	Optional<Product> deleteProductById(String productId);

	Optional<Product> updateProduct(Product product);
	
	Optional<Product> searchProductByName(String productName);

	Optional<List<Product>> fetchAllProduct();
}
