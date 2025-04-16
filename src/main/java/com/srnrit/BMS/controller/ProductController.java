package com.srnrit.BMS.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.service.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	private IProductService productService;

	public ProductController(IProductService productService) {
		this.productService = productService;
	}

	// Add Product by Category with Image Upload

	@PostMapping(value = "/addProductByCategory/{categoryId}", consumes = { "multipart/form-data" },

			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addProductByCategory(@RequestPart("product") String productRequestJsonString,
			@PathVariable String categoryId, @RequestPart("file") MultipartFile file)
			throws JsonMappingException, JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		ProductRequestDTO productRequestDTO = objectMapper.readValue(productRequestJsonString, ProductRequestDTO.class);

		// Validate manually (since Spring doesn't automatically validate @RequestPart)
		// If validation fails, return errors
		if (validateUserRequest(productRequestDTO) != null)
			return ResponseEntity.badRequest().body(validateUserRequest(productRequestDTO));

		ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO, categoryId, file);

		return new ResponseEntity<ProductResponseDTO>(productResponseDTO, HttpStatus.CREATED);

	}

	// Fetch Products by Availability
	@GetMapping(value = "/fetchByAvailability")
	public ResponseEntity<List<ProductResponseDTO>> fetchProductByAvailability(
			@RequestParam Optional<Boolean> inStock) {
		Boolean availability = inStock.orElse(true);
		List<ProductResponseDTO> productList = this.productService.fetchProductByAvailability(availability);
		return ResponseEntity.ok(productList);
	}

	// Delete Product by ID
	@DeleteMapping(value = "/delete/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
		String message = this.productService.deleteProductByProductId(productId.trim());
		logger.info("Product deleted successfully: {}", productId);
		return ResponseEntity.ok(message);
	}

	// Update Product by ID with Validation
	@PutMapping(value = "/update/{productId}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String productId,
			@Valid @RequestBody ProductRequestDTO productRequestDTO) {

		ProductResponseDTO updatedProduct = this.productService.updateProductByProductId(productRequestDTO, productId);
		return ResponseEntity.ok(updatedProduct);
	}

	@GetMapping(value = "/searchByName")
	public ResponseEntity<?> searchProductByName(@RequestParam String productName) {
		if (productName == null || productName.trim().isEmpty()) {
			return new ResponseEntity<>("Product name must be provided!", HttpStatus.BAD_REQUEST);
		}

		ProductResponseDTO productResponseDTO = this.productService.getProductByProductName(productName.trim());
		return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
	}

	// Fetch All Products
	@GetMapping(value = "/getAllProducts")
	public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
		List<ProductResponseDTO> productList = this.productService.getAllProducts();
		return ResponseEntity.ok(productList);
	}

	// Manual validation method (since @RequestPart doesn't auto-validate)
	private String validateUserRequest(@Valid ProductRequestDTO productRequestDTO) {
		return null;
	}

}