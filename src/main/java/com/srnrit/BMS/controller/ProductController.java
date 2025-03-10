package com.srnrit.BMS.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    // Add Product by Category with Validation
    @PostMapping(value = "/addProductByCategory")
    public ResponseEntity<?> addProductByCategory(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while adding product: {}", e.getMessage(), e);
            return new ResponseEntity<>("An unexpected error occurred while adding the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Fetch Products by Availability
    @GetMapping(value = "/fetchByAvailability")
    public ResponseEntity<?> fetchProductByAvailability(@RequestParam Boolean inStock) {
        try {
            List<ProductResponseDTO> productList = this.productService.fetchProductByAvailability(inStock);
            if (productList.isEmpty()) {
                return new ResponseEntity<>("No products available with the given availability status.", HttpStatus.OK);
            }
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while fetching products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Product by ID with Validation
    @DeleteMapping(value = "/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        try {
            String message = this.productService.deleteProductByProductId(productId);
            logger.info("Product deleted successfully: {}", productId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            logger.error("Product deletion failed: {}", e.getMessage());
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting product with ID {}: {}", productId, e.getMessage(), e);
            return new ResponseEntity<>("An unexpected error occurred while deleting the product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  Update Product by ID with Validation
    @PutMapping(value = "/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO updatedProduct = this.productService.updateProductByProductId(productRequestDTO, productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while updating the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            return new ResponseEntity<>("An unexpected error occurred while searching for the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  Fetch All Products with Validation
    @GetMapping(value = "/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponseDTO> productList = this.productService.getAllProducts();
            if (productList.isEmpty()) {
                return new ResponseEntity<>("No products found in the database.", HttpStatus.OK);
            }
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while fetching all products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}