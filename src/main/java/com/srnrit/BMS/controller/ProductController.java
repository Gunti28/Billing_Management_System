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

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private final IProductService productService;
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    // Add Product by Category 
    @PostMapping(value = "/addProductByCategory")
    public ResponseEntity<?> addProductByCategory(@RequestBody ProductRequestDTO productRequestDTO) {
        try {
            if (productRequestDTO.getCategoryId() == null || productRequestDTO.getCategoryId().isEmpty()) {
                return new ResponseEntity<>("Category ID is required!", HttpStatus.BAD_REQUEST);
            }
            ProductResponseDTO productResponseDTO = this.productService.storeProduct(productRequestDTO);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while adding the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Fetch Products by Availability with Validation
    // http://localhost:8080/product/fetchByAvailability?inStock={true or false}
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

 // Delete Product by ID 
 // http://localhost:8080/product/delete/{product_id}
    @DeleteMapping(value = "/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        try {
            if (productId == null || productId.isEmpty()) {
                logger.error("Product deletion failed: Product ID is missing.");
                return new ResponseEntity<>("Error: Product ID is required!", HttpStatus.BAD_REQUEST);
            }
            
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

    // Update Product by ID with Validations
    @PutMapping(value = "/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            if (productId == null || productId.isEmpty()) {
                return new ResponseEntity<>("Product ID is required!", HttpStatus.BAD_REQUEST);
            }
            if (productRequestDTO == null) {
                return new ResponseEntity<>("Product data is required!", HttpStatus.BAD_REQUEST);
            }
            ProductResponseDTO updatedProduct = this.productService.updateProductByProductId(productRequestDTO, productId);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while updating the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search Product by Name with Validations
    @GetMapping(value = "/searchByName")
    public ResponseEntity<?> searchProductByName(@RequestParam String productName) {
        try {
            if (productName == null || productName.trim().isEmpty()) {
                return new ResponseEntity<>("Product name is required!", HttpStatus.BAD_REQUEST);
            }
            ProductResponseDTO productResponseDTO = this.productService.getProductByProductName(productName);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while searching for the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Fetch All Products with Error Handling
    // http://localhost:8080/product/getAllProducts
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
