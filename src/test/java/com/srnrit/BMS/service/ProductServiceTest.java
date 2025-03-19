package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.impl.ProductServiceImpl;
import com.srnrit.BMS.util.FileStorageProperties;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@Mock
	private FileStorageProperties fileStorageProperties;

	@InjectMocks
	private ProductServiceImpl productService;

	private ProductServiceImpl productServiceSpy;

	private ProductRequestDTO productRequestDTO;
	private Product product;

	@BeforeEach
	void setUp() {
		productServiceSpy = spy(productService);

		productRequestDTO = new ProductRequestDTO();
		productRequestDTO.setProductName("Organic Apple");
		productRequestDTO.setCategoryId("cat123");

		product = new Product();
		product.setProductId("prod123");
		product.setProductName("Organic Apple");
	}

	@Test
	void testStoreProduct_NullRequest() {
		MultipartFile mockFile = mock(MultipartFile.class);

		assertThrows(IllegalArgumentException.class, () -> productService.storeProduct(null, mockFile));
	}

	@Test
	void testGetProductByProductName_Success() {
		when(productDao.searchProductByName(anyString())).thenReturn(Optional.of(Collections.singletonList(product)));

		ProductResponseDTO response = productService.getProductByProductName("Organic Apple");

		assertNotNull(response);
		assertEquals("Organic Apple", response.getProductName());
	}

	@Test
	void testGetProductByProductName_NotFound() {
		when(productDao.searchProductByName(anyString())).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class,
				() -> productService.getProductByProductName("NonExistentProduct"));
	}

	@Test
	void testDeleteProductByProductId_Success() {
		// Mocking successful deletion
		when(productDao.deleteProductById("prod123")).thenReturn(Optional.of("Product deleted successfully"));

		// Call the service method
		String result = productService.deleteProductByProductId("prod123");

		// Assertions
		assertNotNull(result);
		assertEquals("Product deleted successfully", result);

		// Verify interactions
		verify(productDao, times(1)).deleteProductById("prod123");
	}

	@Test
	void testDeleteProductByProductId_DatabaseException() {
		// Simulating a database-level exception
		when(productDao.deleteProductById("prod123")).thenThrow(new RuntimeException("Database error"));

		// Assert the exception is propagated
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.deleteProductByProductId("prod123"));

		assertEquals("Database error", exception.getMessage());

		// Verify interaction
		verify(productDao, times(1)).deleteProductById("prod123");
	}

	@Test
	void testUpdateProductByProductId_Success() {
		// Mocking successful product update
		when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.of(product));

		// Call the service method
		ProductResponseDTO response = productService.updateProductByProductId(productRequestDTO, "prod123");

		// Assertions
		assertNotNull(response);
		assertEquals("Organic Apple", response.getProductName());

		// Verify interactions
		verify(productDao, times(1)).updateProduct(any(Product.class));
	}

	@Test
	void testUpdateProductByProductId_DatabaseException() {
		// Simulating a database-level exception
		when(productDao.updateProduct(any(Product.class))).thenThrow(new RuntimeException("Database error"));

		// Assert the exception is propagated
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.updateProductByProductId(productRequestDTO, "prod123"));

		assertEquals("Database error", exception.getMessage());

		// Verify interaction
		verify(productDao, times(1)).updateProduct(any(Product.class));
	}

	@Test
	void testGetAllProducts_Success() {
		// Mocking successful product fetch
		when(productDao.fetchAllProduct()).thenReturn(Optional.of(Collections.singletonList(product)));

		// Call the service method
		List<ProductResponseDTO> response = productService.getAllProducts();

		// Assertions
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());

		// Verify interaction
		verify(productDao, times(1)).fetchAllProduct();
	}

	@Test
	void testGetAllProducts_DatabaseException() {
		// Simulating database error
		when(productDao.fetchAllProduct()).thenThrow(new RuntimeException("Database error"));

		// Assert exception is propagated
		RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());

		assertEquals("Database error", exception.getMessage());

		// Verify interaction
		verify(productDao, times(1)).fetchAllProduct();
	}

	@Test
	void testFetchProductByAvailability_Success() {
		// Mocking product fetch by availability
		when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.of(Collections.singletonList(product)));

		// Call the service method
		List<ProductResponseDTO> response = productService.fetchProductByAvailability(true);

		// Assertions
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());

		// Verify interaction
		verify(productDao, times(1)).fetchProductByAvailability(true);
	}

	@Test
	void testFetchProductByAvailability_DatabaseException() {
		// Simulating database error
		when(productDao.fetchProductByAvailability(true)).thenThrow(new RuntimeException("Database error"));

		// Assert exception is propagated
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.fetchProductByAvailability(true));

		assertEquals("Database error", exception.getMessage());

		// Verify interaction
		verify(productDao, times(1)).fetchProductByAvailability(true);
	}
}
