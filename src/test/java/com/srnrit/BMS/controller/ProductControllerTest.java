package com.srnrit.BMS.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.service.IProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

	@Mock
	private IProductService productService;

	@InjectMocks
	private ProductController productController;

	private ProductRequestDTO productRequestDTO;
	private ProductResponseDTO productResponseDTO;
	private MultipartFile file;

	@BeforeEach
	void setUp() {
		productRequestDTO = new ProductRequestDTO(); // Set necessary fields
		productResponseDTO = new ProductResponseDTO(); // Set necessary fields
	}

	@Test
	void testAddProductByCategory() throws Exception {
		// Mock MultipartFile
		file = mock(MultipartFile.class);

		// Initialize DTOs with sample data
		productRequestDTO = new ProductRequestDTO();
		// Set required fields in productRequestDTO

		productResponseDTO = new ProductResponseDTO();
		// Set required fields in productResponseDTO

		// Mock service behavior
		when(productService.storeProduct(any(ProductRequestDTO.class), anyString(), any(MultipartFile.class)))
				.thenReturn(productResponseDTO);

		// Convert DTO to JSON string
		String jsonString = new ObjectMapper().writeValueAsString(productRequestDTO);

		// Call the controller method
		ResponseEntity<?> response = productController.addProductByCategory(jsonString, "1", file);

		// Verify response
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(productResponseDTO, response.getBody());
	}

	@Test
	void testFetchProductByAvailability() {
		when(productService.fetchProductByAvailability(anyBoolean())).thenReturn(Arrays.asList(productResponseDTO));

		ResponseEntity<List<ProductResponseDTO>> response = productController
				.fetchProductByAvailability(Optional.of(true));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}

	@Test
	void testDeleteProduct() {
		when(productService.deleteProductByProductId(anyString())).thenReturn("Product deleted successfully");

		ResponseEntity<String> response = productController.deleteProduct("123");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Product deleted successfully", response.getBody());
	}

	@Test
	void testUpdateProduct() {
		when(productService.updateProductByProductId(any(ProductRequestDTO.class), anyString()))
				.thenReturn(productResponseDTO);

		ResponseEntity<ProductResponseDTO> response = productController.updateProduct("123", productRequestDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(productResponseDTO, response.getBody());
	}

	@Test
	void testSearchProductByName() {
		when(productService.getProductByProductName(anyString())).thenReturn(productResponseDTO);

		ResponseEntity<?> response = productController.searchProductByName("TestProduct");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(productResponseDTO, response.getBody());
	}

	@Test
	void testGetAllProducts() {
		when(productService.getAllProducts()).thenReturn(Arrays.asList(productResponseDTO));

		ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}

	@Test
	void testDeleteNonExistingProduct() {
		when(productService.deleteProductByProductId(anyString())).thenReturn("Product not found");

		ResponseEntity<String> response = productController.deleteProduct("999");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Product not found", response.getBody());
	}

	@Test
	void testUpdateProductWithInvalidData() {
		when(productService.updateProductByProductId(any(ProductRequestDTO.class), anyString()))
				.thenThrow(new IllegalArgumentException("Invalid product data"));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			productController.updateProduct("123", productRequestDTO);
		});

		assertEquals("Invalid product data", exception.getMessage());
	}

	@Test
	void testFetchProductsWhenNoneExist() {
		when(productService.getAllProducts()).thenReturn(Arrays.asList());

		ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
	}

	@Test
	void testAddProductByCategoryWithoutFile() throws Exception {
		when(productService.storeProduct(any(ProductRequestDTO.class), anyString(), isNull()))
				.thenReturn(productResponseDTO);

		String jsonString = new ObjectMapper().writeValueAsString(productRequestDTO);
		ResponseEntity<?> response = productController.addProductByCategory(jsonString, "1", null);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testUpdateProductWithNonExistingId() {
		when(productService.updateProductByProductId(any(ProductRequestDTO.class), anyString())).thenReturn(null);

		ResponseEntity<ProductResponseDTO> response = productController.updateProduct("999", productRequestDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void testSearchProductByNameWithSpecialCharacters() {
		when(productService.getProductByProductName(anyString())).thenReturn(productResponseDTO);

		ResponseEntity<?> response = productController.searchProductByName("@#!$%Product123");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(productResponseDTO, response.getBody());
	}

	@Test
	void testFetchProductByAvailabilityWithFalse() {
		when(productService.fetchProductByAvailability(false)).thenReturn(Arrays.asList(productResponseDTO));

		ResponseEntity<List<ProductResponseDTO>> response = productController
				.fetchProductByAvailability(Optional.of(false));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}

	@Test
	void testFetchProductByAvailabilityWithEmptyList() {
		when(productService.fetchProductByAvailability(true)).thenReturn(Arrays.asList());

		ResponseEntity<List<ProductResponseDTO>> response = productController
				.fetchProductByAvailability(Optional.of(true));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
	}

	@Test
	void testGetAllProductsWithEmptyList() {
		when(productService.getAllProducts()).thenReturn(Arrays.asList());

		ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
	}

	@Test
	void testGetAllProductsWithData() {
		when(productService.getAllProducts()).thenReturn(Arrays.asList(productResponseDTO));

		ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}

	@Test
	void testAddProductByCategoryWithNullFile() throws Exception {
		when(productService.storeProduct(any(ProductRequestDTO.class), anyString(), isNull()))
				.thenReturn(productResponseDTO);

		String jsonString = new ObjectMapper().writeValueAsString(productRequestDTO);
		ResponseEntity<?> response = productController.addProductByCategory(jsonString, "1", null);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void testSearchProductByNameWithEmptyString() {
		ResponseEntity<?> response = productController.searchProductByName("");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Product name must be provided!", response.getBody());
	}

	@Test
	void testGetAllProductsWithMultipleEntries() {
		when(productService.getAllProducts()).thenReturn(Arrays.asList(productResponseDTO, productResponseDTO));

		ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}
	
	

	
	@Test
	void testDeleteProductWithWhitespaceId() {
	    // Arrange: Mock service behavior for whitespace ID
	    when(productService.deleteProductByProductId(anyString()))
	        .thenReturn("Product ID cannot be null or empty");

	    // Act: Call the controller with a whitespace-only ID
	    ResponseEntity<String> response = productController.deleteProduct("   ");

	    // Assert: Verify response
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals("Product ID cannot be null or empty", response.getBody());
	}

	
	
	@Test
	void testSearchProductByNameWithNull() {
	    ResponseEntity<?> response = productController.searchProductByName(null);
	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	
	@Test
	void testFetchProductByAvailabilityWithNullParam() {
	    ResponseEntity<List<ProductResponseDTO>> response = productController.fetchProductByAvailability(Optional.empty());
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	@Test
	void testSearchProductByNameWithLongName() {
	    // Arrange: Define a very long product name
	    String longProductName = "A".repeat(500);

	    // Mock behavior: Simulate the service throwing an exception for long names
	    when(productService.getProductByProductName(anyString()))
	        .thenThrow(new IllegalArgumentException("Product name too long"));

	    // Act & Assert: Ensure the controller handles this properly
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	        productController.searchProductByName(longProductName);
	    });

	    assertEquals("Product name too long", exception.getMessage());
	}

	@Test
	void testFetchProductByAvailabilityWithNull() {
	    ResponseEntity<List<ProductResponseDTO>> response = productController.fetchProductByAvailability(Optional.empty());

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertTrue(response.getBody().isEmpty()); // Assuming the service handles null values gracefully
	}

	@Test
	void testSearchProductByNameWithNumbers() {
	    when(productService.getProductByProductName("12345")).thenReturn(productResponseDTO);

	    ResponseEntity<?> response = productController.searchProductByName("12345");

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(productResponseDTO, response.getBody());
	}
	
	@Test
	void testUpdateProductWithLargeRequestData() {
	    ProductRequestDTO largeRequest = new ProductRequestDTO();
	    // Populate `largeRequest` with very large data

	    when(productService.updateProductByProductId(any(ProductRequestDTO.class), anyString()))
	        .thenThrow(new IllegalArgumentException("Payload too large"));

	    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	        productController.updateProduct("123", largeRequest);
	    });

	    assertEquals("Payload too large", exception.getMessage());
	}

	
	@Test
	void testSearchProductByNameWithNullInput() {
	    ResponseEntity<?> response = productController.searchProductByName(null);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
