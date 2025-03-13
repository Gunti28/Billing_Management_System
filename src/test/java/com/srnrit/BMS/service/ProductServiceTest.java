package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
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
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.impl.ProductServiceImpl;
import com.srnrit.BMS.util.FileStorageProperties;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest_StoreProduct {

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
	void testStoreProduct_Success() throws IOException {
		MultipartFile mockFile = mock(MultipartFile.class);

		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getOriginalFilename()).thenReturn("image.png");
		doNothing().when(mockFile).transferTo(any(File.class));

		when(fileStorageProperties.getImageStoragePath()).thenReturn("/mock/storage/path");
		when(productDao.saveProduct(any(Product.class), anyString())).thenReturn(Optional.of(product));

		ProductResponseDTO response = productServiceSpy.storeProduct(productRequestDTO, mockFile);

		assertNotNull(response);
		assertEquals("Organic Apple", response.getProductName());

		verify(mockFile, times(1)).transferTo(any(File.class));
		verify(productDao, times(1)).saveProduct(any(Product.class), eq("cat123"));
		verify(fileStorageProperties, times(1)).getImageStoragePath();
	}

	@Test
	void testStoreProduct_NullRequest() {
		MultipartFile mockFile = mock(MultipartFile.class);

		assertThrows(IllegalArgumentException.class, () -> productService.storeProduct(null, mockFile));
	}

	@Test
	void testStoreProduct_NullFile() {
		assertThrows(IllegalArgumentException.class, () -> productService.storeProduct(productRequestDTO, null));
	}

	@Test
	void testStoreProduct_EmptyFile() {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.isEmpty()).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> productService.storeProduct(productRequestDTO, mockFile));
	}

	@Test
	void testStoreProduct_FileTransferFailure() throws IOException {
		MultipartFile mockFile = mock(MultipartFile.class);

		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getOriginalFilename()).thenReturn("image.png");
		doThrow(new IOException("File transfer failed")).when(mockFile).transferTo(any(File.class));

		when(fileStorageProperties.getImageStoragePath()).thenReturn("/mock/storage/path");

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productServiceSpy.storeProduct(productRequestDTO, mockFile));

		assertTrue(exception.getMessage().contains("Error saving image file"));

		verify(mockFile, times(1)).transferTo(any(File.class));
	}

	@Test
	void testStoreProduct_SaveFailure() throws IOException {
		MultipartFile mockFile = mock(MultipartFile.class);

		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getOriginalFilename()).thenReturn("image.png");
		doNothing().when(mockFile).transferTo(any(File.class));

		when(fileStorageProperties.getImageStoragePath()).thenReturn("/mock/storage/path");
		when(productDao.saveProduct(any(Product.class), anyString())).thenReturn(Optional.empty());

		assertThrows(ProductNotCreatedException.class,
				() -> productServiceSpy.storeProduct(productRequestDTO, mockFile));

		verify(mockFile, times(1)).transferTo(any(File.class));
		verify(productDao, times(1)).saveProduct(any(Product.class), eq("cat123"));
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
	void testGetProductByProductName_NullInput() {
		assertThrows(IllegalArgumentException.class, () -> productService.getProductByProductName(null));
	}

	@Test
	void testGetProductByProductName_EmptyInput() {
		assertThrows(IllegalArgumentException.class, () -> productService.getProductByProductName(""));
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
	void testDeleteProductByProductId_NotFound() {
		// Mocking when product does not exist
		when(productDao.deleteProductById("invalidId")).thenReturn(Optional.empty());

		// Assert exception is thrown
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
				() -> productService.deleteProductByProductId("invalidId"));

		assertEquals("Product not found with id: invalidId", exception.getMessage());

		// Verify interactions
		verify(productDao, times(1)).deleteProductById("invalidId");
	}

	@Test
	void testDeleteProductByProductId_NullId() {
		// Assert exception for null input
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.deleteProductByProductId(null));

		assertEquals("Product ID cannot be null", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).deleteProductById(anyString());
	}

	@Test
	void testDeleteProductByProductId_EmptyId() {
		// Assert exception for empty input
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.deleteProductByProductId(""));

		assertEquals("Product ID cannot be empty", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).deleteProductById(anyString());
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
	void testDeleteProductByProductId_WhitespaceId() {
		// Assert exception for whitespace input
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.deleteProductByProductId("   "));

		assertEquals("Product ID cannot be empty", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).deleteProductById(anyString());
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
	void testUpdateProductByProductId_NotFound() {
		// Mocking product not found scenario
		when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.empty());

		// Assert exception is thrown
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
				() -> productService.updateProductByProductId(productRequestDTO, "prod123"));

		assertEquals("Product not found with id: prod123", exception.getMessage());

		// Verify interactions
		verify(productDao, times(1)).updateProduct(any(Product.class));
	}

	@Test
	void testUpdateProductByProductId_NullId() {
		// Assert exception for null product ID
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.updateProductByProductId(productRequestDTO, null));

		assertEquals("Product ID cannot be null", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).updateProduct(any(Product.class));
	}

	@Test
	void testUpdateProductByProductId_EmptyId() {
		// Assert exception for empty product ID
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.updateProductByProductId(productRequestDTO, ""));

		assertEquals("Product ID cannot be empty", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).updateProduct(any(Product.class));
	}

	@Test
	void testUpdateProductByProductId_NullRequest() {
		// Assert exception for null request object
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.updateProductByProductId(null, "prod123"));

		assertEquals("Product request cannot be null", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).updateProduct(any(Product.class));
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
	void testUpdateProductByProductId_WhitespaceId() {
		// Assert exception for whitespace product ID
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> productService.updateProductByProductId(productRequestDTO, "   "));

		assertEquals("Product ID cannot be empty", exception.getMessage());

		// Verify no interaction with DAO
		verify(productDao, never()).updateProduct(any(Product.class));
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
	void testGetAllProducts_Empty() {
		// Mocking empty product list
		when(productDao.fetchAllProduct()).thenReturn(Optional.empty());

		// Assert exception is thrown
		RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());

		assertEquals("No products available", exception.getMessage());

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
	void testFetchProductByAvailability_Empty() {
		// Mocking empty product list
		when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.empty());

		// Assert exception is thrown
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.fetchProductByAvailability(true));

		assertEquals("No products available", exception.getMessage());

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
