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
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private FileStorageProperties fileStorageProperties;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDTO productRequestDTO;
    private Product product;

    @BeforeEach
    void setUp() {
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

        // Mocking the static method of ProductImageFileNameGenerator
        try (MockedStatic<ProductImageFileNameGenerator> mockedStatic = mockStatic(ProductImageFileNameGenerator.class)) {
            mockedStatic.when(() -> ProductImageFileNameGenerator.getNewFileName(anyString()))
                        .thenReturn("image_123.png");

            ProductResponseDTO response = productService.storeProduct(productRequestDTO, mockFile);

            assertNotNull(response);
            assertEquals("Organic Apple", response.getProductName());

            verify(mockFile, times(1)).transferTo(any(File.class));
            verify(productDao, times(1)).saveProduct(any(Product.class), eq("cat123"));
            verify(fileStorageProperties, times(1)).getImageStoragePath();
        }
    }


    @Test
    void testSaveImage_Exception() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        // Mocking the MultipartFile behavior
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("image.png");

        // Mocking the image storage path
        when(fileStorageProperties.getImageStoragePath()).thenReturn("/mock/storage/path");

        // Simulate an IOException during file transfer
        doThrow(new IOException("File transfer failed")).when(mockFile).transferTo(any(File.class));

        // Mocking the static method to generate a unique filename
        try (MockedStatic<ProductImageFileNameGenerator> mockedStatic = mockStatic(ProductImageFileNameGenerator.class)) {
            mockedStatic.when(() -> ProductImageFileNameGenerator.getNewFileName(anyString()))
                        .thenReturn("image_123.png");

            // Assert that the RuntimeException is thrown with the correct message
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    productService.storeProduct(productRequestDTO, mockFile));

            assertTrue(exception.getMessage().contains("Error saving image file"));

            // Verify interactions
            verify(mockFile, times(1)).transferTo(any(File.class));
            verify(fileStorageProperties, times(1)).getImageStoragePath();
            mockedStatic.verify(() -> ProductImageFileNameGenerator.getNewFileName("image.png"), times(1));
        }
    }


    @Test
    void testStoreProduct_Failure() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        // Mock image-related behaviors
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("image.png");
        doNothing().when(mockFile).transferTo(any(File.class));

        when(fileStorageProperties.getImageStoragePath()).thenReturn("/mock/storage/path");
        when(productDao.saveProduct(any(Product.class), anyString())).thenReturn(Optional.empty());

        // Mocking the static method of ProductImageFileNameGenerator
        try (MockedStatic<ProductImageFileNameGenerator> mockedStatic = mockStatic(ProductImageFileNameGenerator.class)) {
            mockedStatic.when(() -> ProductImageFileNameGenerator.getNewFileName(anyString()))
                        .thenReturn("image_123.png");

            assertThrows(ProductNotCreatedException.class, () ->
                    productService.storeProduct(productRequestDTO, mockFile));

            // Verify interactions
            verify(mockFile, times(1)).transferTo(any(File.class));
            verify(productDao, times(1)).saveProduct(any(Product.class), eq("cat123"));
            verify(fileStorageProperties, times(1)).getImageStoragePath();
        }
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

        assertThrows(ProductNotFoundException.class, () ->
                productService.getProductByProductName("NonExistentProduct"));
    }

    @Test
    void testDeleteProductByProductId_Success() {
        when(productDao.deleteProductById(anyString())).thenReturn(Optional.of("Product deleted successfully"));

        String result = productService.deleteProductByProductId("prod123");

        assertEquals("Product deleted successfully", result);
    }

    @Test
    void testDeleteProductByProductId_NotFound() {
        when(productDao.deleteProductById(anyString())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProductByProductId("invalidId"));
    }

    @Test
    void testUpdateProductByProductId_Success() {
        when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.updateProductByProductId(productRequestDTO, "prod123");

        assertNotNull(response);
        assertEquals("Organic Apple", response.getProductName());
    }

    @Test
    void testUpdateProductByProductId_Failure() {
        when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProductByProductId(productRequestDTO, "prod123"));
    }

    @Test
    void testGetAllProducts_Success() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.of(Collections.singletonList(product)));

        List<ProductResponseDTO> response = productService.getAllProducts();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void testGetAllProducts_Empty() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }

    @Test
    void testFetchProductByAvailability_Success() {
        when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.of(Collections.singletonList(product)));

        List<ProductResponseDTO> response = productService.fetchProductByAvailability(true);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void testFetchProductByAvailability_Empty() {
        when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.fetchProductByAvailability(true));
    }
}
