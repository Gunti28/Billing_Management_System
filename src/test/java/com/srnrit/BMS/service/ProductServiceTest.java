package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.MockedStatic;
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
import com.srnrit.BMS.util.idgenerator.ProductImageFileNameGenerator;

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
		assertThrows(IllegalArgumentException.class, () -> productService.storeProduct(null));
	}

	@Test
	void testStoreProduct_WithImage_Success() throws Exception {
		// Prepare a non-empty MultipartFile mock.
		MultipartFile imageFile = mock(MultipartFile.class);
		when(imageFile.isEmpty()).thenReturn(false);
		when(imageFile.getOriginalFilename()).thenReturn("test.png");

		// Simulate fileStorageProperties returning a temporary directory.
		String tempDir = System.getProperty("java.io.tmpdir");
		when(fileStorageProperties.getImageStoragePath()).thenReturn(tempDir);

		// Use static mocking for ProductImageFileNameGenerator.
		String generatedFileName = "test_unique.png";
		try (MockedStatic<ProductImageFileNameGenerator> mockedStatic = mockStatic(
				ProductImageFileNameGenerator.class)) {
			mockedStatic.when(() -> ProductImageFileNameGenerator.getNewFileName(anyString()))
					.thenReturn(generatedFileName);

			// Avoid writing to disk: stub transferTo to do nothing.
			doAnswer(invocation -> {
				File fileArg = invocation.getArgument(0);
				// Optionally verify fileArg path if needed.
				return null;
			}).when(imageFile).transferTo(any(File.class));

			// Stub productDao.saveProduct to return the product.
			when(productDao.saveProduct(any(Product.class), anyString())).thenReturn(Optional.of(product));

			// Call the method under test.
			ProductResponseDTO response = productService.storeProduct(productRequestDTO);

			// Verify that the response is correct.
			assertNotNull(response);
			assertEquals("Organic Apple", response.getProductName());
			// Verify that transferTo was called once.
			verify(imageFile, times(1)).transferTo(any(File.class));
		}
	}

	@Test
	void testStoreProduct_ImageTransferFailure() throws Exception {
		// Prepare a non-empty MultipartFile mock that throws IOException.
		MultipartFile imageFile = mock(MultipartFile.class);
		when(imageFile.isEmpty()).thenReturn(false);
		when(imageFile.getOriginalFilename()).thenReturn("test.png");

		// Simulate fileStorageProperties returning a temporary directory.
		when(fileStorageProperties.getImageStoragePath()).thenReturn(System.getProperty("java.io.tmpdir"));

		// Use static mocking for ProductImageFileNameGenerator.
		String generatedFileName = "test_unique.png";
		try (MockedStatic<ProductImageFileNameGenerator> mockedStatic = mockStatic(
				ProductImageFileNameGenerator.class)) {
			mockedStatic.when(() -> ProductImageFileNameGenerator.getNewFileName(anyString()))
					.thenReturn(generatedFileName);

			// Stub transferTo to throw IOException.
			doThrow(new IOException("Simulated IO failure")).when(imageFile).transferTo(any(File.class));

			// When productDao.saveProduct is called, it won't be reached because saveImage
			// should fail.
			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				productService.storeProduct(productRequestDTO);
			});
			assertEquals("Error saving image file: Simulated IO failure", exception.getMessage());
		}
	}

	@Test
	void testStoreProduct_ProductNotCreated() {
		// Prepare a scenario where productDao.saveProduct returns Optional.empty()
		MultipartFile imageFile = mock(MultipartFile.class);
		when(imageFile.isEmpty()).thenReturn(true); // no image processing

		when(productDao.saveProduct(any(Product.class), anyString())).thenReturn(Optional.empty());

		// Expect a ProductNotCreatedException.
		Exception exception = assertThrows(ProductNotCreatedException.class,
				() -> productService.storeProduct(productRequestDTO));
		assertEquals("Failed to save product.", exception.getMessage());
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
		when(productDao.deleteProductById("prod123")).thenReturn(Optional.of("Product deleted successfully"));
		String result = productService.deleteProductByProductId("prod123");
		assertNotNull(result);
		assertEquals("Product deleted successfully", result);
		verify(productDao, times(1)).deleteProductById("prod123");
	}

	@Test
	void testDeleteProductByProductId_DatabaseException() {
		when(productDao.deleteProductById("prod123")).thenThrow(new RuntimeException("Database error"));
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.deleteProductByProductId("prod123"));
		assertEquals("Database error", exception.getMessage());
		verify(productDao, times(1)).deleteProductById("prod123");
	}

	@Test
	void testUpdateProductByProductId_Success() {
		when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.of(product));
		ProductResponseDTO response = productService.updateProductByProductId(productRequestDTO, "prod123");
		assertNotNull(response);
		assertEquals("Organic Apple", response.getProductName());
		verify(productDao, times(1)).updateProduct(any(Product.class));
	}

	@Test
	void testUpdateProductByProductId_NotFound() {
		when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.empty());
		Exception exception = assertThrows(ProductNotFoundException.class,
				() -> productService.updateProductByProductId(productRequestDTO, "prod123"));
		assertEquals("Failed to update product with ID: prod123", exception.getMessage());
	}

	@Test
	void testUpdateProductByProductId_DatabaseException() {
		when(productDao.updateProduct(any(Product.class))).thenThrow(new RuntimeException("Database error"));
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.updateProductByProductId(productRequestDTO, "prod123"));
		assertEquals("Database error", exception.getMessage());
		verify(productDao, times(1)).updateProduct(any(Product.class));
	}

	@Test
	void testGetAllProducts_Success() {
		when(productDao.fetchAllProduct()).thenReturn(Optional.of(Collections.singletonList(product)));
		List<ProductResponseDTO> response = productService.getAllProducts();
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
		verify(productDao, times(1)).fetchAllProduct();
	}

	@Test
	void testGetAllProducts_NoProducts() {
		// Simulate no products available
		when(productDao.fetchAllProduct()).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());
		assertEquals("No products available!", exception.getMessage());
	}

	@Test
	void testGetAllProducts_DatabaseException() {
		when(productDao.fetchAllProduct()).thenThrow(new RuntimeException("Database error"));
		RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());
		assertEquals("Database error", exception.getMessage());
		verify(productDao, times(1)).fetchAllProduct();
	}

	@Test
	void testFetchProductByAvailability_Success() {
		when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.of(Collections.singletonList(product)));
		List<ProductResponseDTO> response = productService.fetchProductByAvailability(true);
		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
		verify(productDao, times(1)).fetchProductByAvailability(true);
	}

	@Test
	void testFetchProductByAvailability_NoProducts() {
		when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.fetchProductByAvailability(true));
		assertEquals("No products found with specified availability!", exception.getMessage());
	}

	@Test
	void testFetchProductByAvailability_DatabaseException() {
		when(productDao.fetchProductByAvailability(true)).thenThrow(new RuntimeException("Database error"));
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> productService.fetchProductByAvailability(true));
		assertEquals("Database error", exception.getMessage());
		verify(productDao, times(1)).fetchProductByAvailability(true);
	}
}
