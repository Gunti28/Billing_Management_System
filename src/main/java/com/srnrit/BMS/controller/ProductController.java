package com.srnrit.BMS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.service.IProductService;

@RestController
@RequestMapping
public class ProductController {
	
	
	IProductService productService;
	
	public ProductController(IProductService productService) {
		super();
		this.productService = productService;
	}

	@PostMapping(value = "/addProductByCategory")
	public ResponseEntity<ProductResponseDTO> addProductByCategory(@RequestBody ProductRequestDTO productRequestDTO) {
		
		ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO);
		
		return new ResponseEntity<ProductResponseDTO>(productResponseDTO, HttpStatus.CREATED);
	}
	
	
}
