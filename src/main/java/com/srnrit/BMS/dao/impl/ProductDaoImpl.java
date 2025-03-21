package com.srnrit.BMS.dao.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;
import com.srnrit.BMS.repository.ProductRepository;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.idgenerator.ProductImageFileNameGenerator;

@Component
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	

	@Override
	public Optional<Product> saveProduct(Product product, String categoryId) {
		if (product == null || product.getProductName() == null || product.getProductName().trim().isEmpty()) {
			throw new IllegalArgumentException("Product name cannot be empty or null");
		}
		Boolean existsById = this.categoryRepository.existsById(categoryId);
		if (!existsById) {
			throw new CategoryNotFoundException("Category Not Found with Id:" + categoryId);
		}
		boolean productExists = this.productRepository.existsByProductNameIgnoreCase(product.getProductName());
		if (productExists) {
			throw new IllegalArgumentException("Product with name '" + product.getProductName() + "' already exists.");
		}
		Category category = this.categoryRepository.getReferenceById(categoryId);
		category.addProduct(product);
		product.setCategory(category);
		Product savedProduct = this.productRepository.save(product);
		return savedProduct != null ? Optional.of(savedProduct) : Optional.empty();
	}

	@Override
	public Optional<List<Product>> fetchProductByAvailability(Boolean availability) {
		if (availability == null) {
			throw new NullPointerException("Availability status cannot be null");
		}
		List<Product> products = this.productRepository.findByInStock(availability);
		return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}

	@Override
	public Optional<String> deleteProductById(String productId) {
		if (productId == null) {
			throw new NullPointerException("Product ID cannot be null");
		}
		Boolean existsById = this.productRepository.existsById(productId);
		if (existsById) {
			Product product = this.productRepository.getReferenceById(productId);

			// Remove product from all categories
			if (product.getCategory() != null) {
				product.getCategory().removeProduct(product);
			}

			this.productRepository.delete(product);
			return Optional.of("Product successfully deleted with Id:" + productId);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Product> updateProduct(Product product) {
		if (product == null || product.getProductId() == null) {
			throw new NullPointerException("Product or Product ID cannot be null");
		}

		boolean existsById = this.productRepository.existsById(product.getProductId());
		if (!existsById) {
			return Optional.empty();
		}

		Product oldProduct = this.productRepository.getReferenceById(product.getProductId());

		// Check if any actual updates are needed
		if (oldProduct.getProductName().equals(product.getProductName())
				&& oldProduct.getProductImage().equals(product.getProductImage())
				&& oldProduct.getProductQuantity() == product.getProductQuantity()
				&& oldProduct.getProductPrice() == product.getProductPrice()
				&& oldProduct.getInStock().equals(product.getInStock())) {
			return Optional.of(oldProduct); // Return existing product if no changes
		}

		oldProduct.setProductName(product.getProductName());
		oldProduct.setProductImage(product.getProductImage());
		oldProduct.setProductQuantity(product.getProductQuantity());
		oldProduct.setProductPrice(product.getProductPrice());
		oldProduct.setInStock(product.getInStock());

		Product updatedProduct = this.productRepository.save(oldProduct);
		return Optional.of(updatedProduct);
	}

	@Override
	public Optional<List<Product>> searchProductByName(String name) {
		if (name == null) {
			throw new NullPointerException("Product name cannot be null");
		}
		List<Product> products = this.productRepository.findByProductNameContainingIgnoreCase(name);
		return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}

	@Override
	public Optional<List<Product>> fetchAllProduct() {
		List<Product> products = this.productRepository.findAll();
		if (products != null && products.size() != 0) {
			return Optional.of(products);
		}
		return Optional.empty();
	}

	@Override
	public boolean storeImage(MultipartFile file, String newImageName) 
	{
		try 
		{
			if (file != null && (newImageName !=null && !newImageName.isBlank())) 
			{
				
				// now update image in local driver
				String targetDirectory = this.fileStorageProperties.getImageStoragePath();
				
				// create directories if not exist
				Path path = Paths.get(targetDirectory);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}
				
				// save the file with new image file name
				Path targetLocation = path.resolve(newImageName);
				

				// storing new image in the local driver
				long copied = Files.copy(file.getInputStream(), targetLocation);

				return copied > 0 ? true : false;
			} 
			else throw new RuntimeException("Invalid image file");
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage());
		}

	}

}
