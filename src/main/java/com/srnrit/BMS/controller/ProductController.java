package com.srnrit.BMS.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
}
