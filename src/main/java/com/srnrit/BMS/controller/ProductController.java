package com.srnrit.BMS.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
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

	@PostMapping(value = "/addProductByCategory/{categoryId}", consumes = {"multipart/form-data" },                                                             
			                                                              
	                                                           produces = {MediaType.APPLICATION_JSON_VALUE}
	            )
	public ResponseEntity<?> addProductByCategory( 
			@RequestPart("product") String productRequestJsonString,
			@PathVariable String categoryId, @RequestPart("file") MultipartFile file) throws JsonMappingException, JsonProcessingException
	{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ProductRequestDTO productRequestDTO = objectMapper.readValue(productRequestJsonString, ProductRequestDTO.class);
		
		
		// Validate manually (since Spring doesn't automatically validate @RequestPart)
        // If validation fails, return errors
        if (validateUserRequest(productRequestDTO) != null) 
          return ResponseEntity.badRequest().body(validateUserRequest(productRequestDTO));
		
		
		

		ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO,categoryId,file);

		return new ResponseEntity<ProductResponseDTO>(productResponseDTO, HttpStatus.CREATED);

	}

	// Fetch Products by Availability
	@GetMapping(value = "/fetchByAvailability")
	public ResponseEntity<?> fetchProductByAvailability(@RequestParam Optional<Boolean> inStock) {
	    try {
	        Boolean availability = inStock.orElse(true); // Default to true if not provided
	        List<ProductResponseDTO> productList = this.productService.fetchProductByAvailability(availability);
	        
	        if (productList.isEmpty()) {
	            return new ResponseEntity<>("No products available with the given availability status.", HttpStatus.OK);
	        }
	        return new ResponseEntity<>(productList, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("An unexpected error occurred while fetching products.",
	                HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Delete Product by ID with Validation
	@DeleteMapping(value = "/delete/{productId}")
	public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String productId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        productId = productId.trim(); // Trim spaces to avoid mismatch issues
	        String message = this.productService.deleteProductByProductId(productId);

	        logger.info("Product deleted successfully: {}", productId);

	        response.put("status", "success");
	        response.put("message", message);
	        response.put("productId", productId);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (ProductNotFoundException e) {
	        logger.warn("Product deletion failed - Not Found: {}", e.getMessage());

	        response.put("status", "error");
	        response.put("message", "Product not found for deletion.");
	        response.put("errorDetails", e.getMessage());

	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        logger.error("❌ Unexpected error while deleting product with ID {}: {}", productId, e.getMessage(), e);

	        response.put("status", "error");
	        response.put("message", "An unexpected error occurred while deleting the product.");
	        response.put("errorDetails", e.getMessage());

	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	// Update Product by ID with Validation
	@PutMapping(value = "/update/{productId}")
	public ResponseEntity<ProductResponseDTO> updateProduct(
	        @PathVariable String productId,
	        @Valid @RequestBody ProductRequestDTO productRequestDTO) {
	    
	    ProductResponseDTO updatedProduct = this.productService.updateProductByProductId(productRequestDTO, productId);
	    return ResponseEntity.ok(updatedProduct);
	}


	// Search Product by Name with Validation
	@GetMapping(value = "/searchByName")
	public ResponseEntity<?> searchProductByName(@RequestParam String productName) {
		try {
			ProductResponseDTO productResponseDTO = this.productService.getProductByProductName(productName);
			return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred while searching for the product.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch All Products with Validation
	@GetMapping(value = "/getAllProducts")
	public ResponseEntity<?> getAllProducts() {
		try {
			List<ProductResponseDTO> productList = this.productService.getAllProducts();
			if (productList.isEmpty()) {
				return new ResponseEntity<>("No products found in the database.", HttpStatus.OK);
			}
			return new ResponseEntity<>(productList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred while fetching all products.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Manual validation method (since @RequestPart doesn't auto-validate)
    private String validateUserRequest(@Valid ProductRequestDTO productRequestDTO) 
    {
    	return null;
    }
	
	
	
}