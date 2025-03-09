package com.srnrit.BMS.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.service.IProductService;

@RestController
@RequestMapping(value="/product")
public class ProductController {
	
	
	IProductService productService;
	
	public ProductController(IProductService productService) {
		super();
		this.productService = productService;
	}

	// Add Product by Category
	@PostMapping(value = "/addProductByCategory")
	public ResponseEntity<ProductResponseDTO> addProductByCategory(@RequestBody ProductRequestDTO productRequestDTO) {
	    ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO);
	    return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
	}

	// Fetch Products by Availability (Multiple Products)
	@GetMapping(value = "/fetchByAvailability")
	public ResponseEntity<List<ProductResponseDTO>> fetchProductByAvailability(@RequestParam Boolean inStock) {
	    List<ProductResponseDTO> productList = this.productService.fetchProductByAvailability(inStock);
	    return new ResponseEntity<>(productList, HttpStatus.OK);
	}
	
	
	// Delete Product by ID
    @DeleteMapping(value = "/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        String message = this.productService.deleteProductByProductId(productId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    // Update product by ID
    @PutMapping(value = "/update/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String productId, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO updatedProduct = this.productService.updateProductByProductId(productRequestDTO, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
	
    
    // Search Product by Name
    @GetMapping(value = "/searchByName")
    public ResponseEntity<ProductResponseDTO> searchProductByName(@RequestParam String productName) {
        ProductResponseDTO productResponseDTO = this.productService.getProductByProductName(productName);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }
    
    
    // Fetch All Products
    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> productList = this.productService.getAllProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
}
