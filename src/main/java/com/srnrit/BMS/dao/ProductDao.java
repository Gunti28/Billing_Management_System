package com.srnrit.BMS.dao;

import java.util.List;
import java.util.Optional;

import com.srnrit.BMS.entity.Product;

public interface ProductDao {

	Optional<Product> saveProduct(Product product);

	Optional<Product> fetchProductById(String productId);

	Optional<Product> deleteProductById(String productId);

	Optional<Product> updateProductById(Product newProduct, String oldProductId);

	Optional<List<Product>> fetchAllProduct();
}
