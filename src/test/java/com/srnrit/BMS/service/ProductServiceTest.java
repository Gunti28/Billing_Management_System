package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.srnrit.BMS.exception.productexceptions.UnsupportedFileTypeException;
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

    private Product product;
    private ProductRequestDTO productRequestDTO;
    private ProductResponseDTO productResponseDTO;

    @BeforeEach
    void setUp() {
        product = new Product("Apple", 10, 20.5, true);
        product.setProductId("PID12345");

        productRequestDTO = new ProductRequestDTO("Apple", 10, 20.5, true);
        
        productResponseDTO = new ProductResponseDTO("PID12345", "Apple", "image.jpg", 10, 20.5, true);
    }

    @Test
    void testGetProductByProductName_Success() {
        when(productDao.searchProductByName("Apple")).thenReturn(Optional.of(List.of(product)));

        ProductResponseDTO response = productService.getProductByProductName("Apple");

        assertNotNull(response);
        assertEquals("Apple", response.getProductName());
        verify(productDao, times(1)).searchProductByName("Apple");
    }

    @Test
    void testGetProductByProductName_NotFound() {
        when(productDao.searchProductByName("Mango")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductByProductName("Mango"));
        verify(productDao, times(1)).searchProductByName("Mango");
    }

    @Test
    void testDeleteProductByProductId_Success() {
        when(productDao.deleteProductById("PID12345")).thenReturn(Optional.of("Deleted"));

        String result = productService.deleteProductByProductId("PID12345");
        assertEquals("Deleted", result);
        verify(productDao, times(1)).deleteProductById("PID12345");
    }

    @Test
    void testDeleteProductByProductId_NotFound() {
        when(productDao.deleteProductById("PID99999")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductByProductId("PID99999"));
    }

    @Test
    void testGetAllProducts_Success() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.of(Arrays.asList(product)));
        
        List<ProductResponseDTO> products = productService.getAllProducts();
        
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Apple", products.get(0).getProductName());
    }
    
    @Test
    void testGetAllProducts_NoProductsFound() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }
    
    /*** Test Case: Fetch product by name - product not found ***/
    @Test
    void testGetProductByProductName_ProductNotFound() {
        when(productDao.searchProductByName("Unknown Product")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductByProductName("Unknown Product"));
    }

    /*** Test Case: Delete product by valid ID ***/
    @Test
    void testDeleteProductByProductId_ValidId() {
        when(productDao.deleteProductById("PID123")).thenReturn(Optional.of("Product deleted successfully"));
        String result = productService.deleteProductByProductId("PID123");
        assertEquals("Product deleted successfully", result);
    }
    
    /*** Test Case: Delete product by invalid ID ***/
    @Test
    void testDeleteProductByProductId_InvalidId() {
        when(productDao.deleteProductById("PID999")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductByProductId("PID999"));
    }

    

    /*** Test Case: Fetch all products - empty list ***/
    @Test
    void testGetAllProducts_NoProductsAvailable() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }

    /*** Test Case: Fetch products by availability (in stock) ***/
    @Test
    void testFetchProductByAvailability_InStock() {
        when(productDao.fetchProductByAvailability(true)).thenReturn(Optional.of(Arrays.asList(product)));
        List<ProductResponseDTO> result = productService.fetchProductByAvailability(true);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getInStock());
    }

    /*** Test Case: Fetch products by availability - no products available ***/
    @Test
    void testFetchProductByAvailability_NoProductsAvailable() {
        when(productDao.fetchProductByAvailability(false)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.fetchProductByAvailability(false));
    }

    /*** Test Case: Validate image with valid format ***/
    @Test
    void testValidateProductImage_ValidImage() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(500L);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getOriginalFilename()).thenReturn("test.png");

        when(fileStorageProperties.getGetMaxFileSize()).thenReturn(1000L);
        
        assertTrue(productService.validateProductImage(mockFile));
    }

  

    /*** Test Case: Validate image is null ***/
    @Test
    void testValidateProductImage_NullFile() {
        assertThrows(RuntimeException.class, () -> productService.validateProductImage(null));
    }
    
    
    @Test
    void testUpdateProductByProductId_ValidId() {
        // Mock product
        Product mockProduct = new Product();
        mockProduct.setProductId("PID123");
        mockProduct.setProductName("Test Product");
        mockProduct.setProductQuantity(10);
        mockProduct.setProductPrice(99.99);
        mockProduct.setInStock(true);

        // Mock productRequestDTO
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductName("Test Product");
        productRequestDTO.setProductQuantity(10);
        productRequestDTO.setProductPrice(99.99);
        productRequestDTO.setInStock(true);

        // Mock repository behavior
        when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.of(mockProduct));

        // Call method
        ProductResponseDTO result = productService.updateProductByProductId(productRequestDTO, "PID123");

        // Assertions
        assertNotNull(result);
        assertEquals("PID123", result.getProductId());  // Check if ID is set correctly
        assertEquals("Test Product", result.getProductName());  // Check if name is correct
    }


    /*** Test Case: Update product by invalid ID ***/
    @Test
    void testUpdateProductByProductId_InvalidId() {
        when(productDao.updateProduct(any(Product.class))).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, 
                     () -> productService.updateProductByProductId(productRequestDTO, "PID999"));
    }


    /*** Test Case: Store product with missing image file ***/
    @Test
    void testStoreProduct_NullImage() {
        assertThrows(RuntimeException.class, 
                     () -> productService.storeProduct(productRequestDTO, "CAT123", null));
    }

    /*** Test Case: Store product fails due to database error ***/
    @Test
    void testStoreProduct_FailedToSave() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getOriginalFilename()).thenReturn("test.png");

        when(productDao.saveProduct(any(Product.class), eq("CAT123"))).thenReturn(Optional.empty());

        assertThrows(ProductNotCreatedException.class, 
                     () -> productService.storeProduct(productRequestDTO, "CAT123", mockFile));
    }

    /*** Test Case: Store product successfully but image fails to save ***/
    @Test
    void testStoreProduct_ImageStorageFails() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getOriginalFilename()).thenReturn("test.png");

        when(productDao.saveProduct(any(Product.class), eq("CAT123"))).thenReturn(Optional.of(product));
        when(productDao.storeImage(any(MultipartFile.class), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, 
                     () -> productService.storeProduct(productRequestDTO, "CAT123", mockFile));
    }

    @Test
    void testFetchProductByAvailability_OutOfStock() {
        // Mock product (out of stock)
        Product mockProduct = new Product();
        mockProduct.setProductId("PID456");
        mockProduct.setProductName("Out of Stock Product");
        mockProduct.setProductQuantity(0);
        mockProduct.setProductPrice(199.99);
        mockProduct.setInStock(false); // This is important

        // Mock DAO behavior
        when(productDao.fetchProductByAvailability(false)).thenReturn(Optional.of(Arrays.asList(mockProduct)));

        // Call service method
        List<ProductResponseDTO> result = productService.fetchProductByAvailability(false);

        // Assertions
        assertFalse(result.isEmpty()); // Ensure the result is not empty
        assertEquals(1, result.size()); // Check that exactly one product is returned
        assertFalse(result.get(0).getInStock()); // Ensure product is out of stock
        assertEquals("Out of Stock Product", result.get(0).getProductName()); // Check correct product
    }


    /*** Test Case: Fetch all products when database throws exception ***/
    @Test
    void testGetAllProducts_DatabaseException() {
        when(productDao.fetchAllProduct()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> productService.getAllProducts());
    }

    /*** Test Case: Delete product fails due to database error ***/
    @Test
    void testDeleteProductByProductId_DatabaseError() {
        when(productDao.deleteProductById("PID123")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> productService.deleteProductByProductId("PID123"));
    }

    @Test
    void testStoreProduct_NullProductRequestDTO() {
        MultipartFile mockFile = mock(MultipartFile.class);

        Exception exception = assertThrows(RuntimeException.class, 
            () -> productService.storeProduct(null, "CAT123", mockFile));

        assertEquals("ProductRequestDTO must not be null", exception.getMessage());
    }

    

    @Test
    void testUpdateProductByProductId_NullId() {
        Exception exception = assertThrows(ProductNotFoundException.class, 
            () -> productService.updateProductByProductId(productRequestDTO, null));

        assertTrue(exception.getMessage().contains("Failed to update product"));
    }



    @Test
    void testGetAllProducts_EmptyList() {
        when(productDao.fetchAllProduct()).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, 
            () -> productService.getAllProducts());

        assertEquals("No products available!", exception.getMessage());
    }

}
