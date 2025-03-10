package com.srnrit.BMS.dao;

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

import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;
import com.srnrit.BMS.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductDaoImplTest {
	
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductDaoImpl productDao;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId("category001");
        category.setCategoryname("Electronics");

        product = new Product();
        product.setProductId("prod_001");
        product.setProductName("Laptop");
        product.setProductPrice(50000.0);
        product.setProductQuantity(10);
        product.setInStock(true);
        product.setCategory(category);
    }

    // ✅ Test: Save Product (Success)
    @Test
    void testSaveProduct_Success() {
        when(categoryRepository.existsById("category001")).thenReturn(true);
        when(categoryRepository.getReferenceById("category001")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<Product> savedProduct = productDao.saveProduct(product, "category001");

        assertTrue(savedProduct.isPresent());
        assertEquals("Laptop", savedProduct.get().getProductName());
        assertEquals(50000.0, savedProduct.get().getProductPrice());
    }

    // ✅ Test: Save Product (Failure - Category Not Found)
    @Test
    void testSaveProduct_CategoryNotFound() {
        when(categoryRepository.existsById("invalid_category")).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> productDao.saveProduct(product, "invalid_category"));
    }

    // ✅ Test: Fetch Product By Availability (Success)
    @Test
    void testFetchProductByAvailability_Success() {
        when(productRepository.findByInStock(true)).thenReturn(Arrays.asList(product));

        Optional<List<Product>> products = productDao.fetchProductByAvailability(true);

        assertTrue(products.isPresent());
        assertEquals(1, products.get().size());
    }

    // ✅ Test: Fetch Product By Availability (Failure - No Products Found)
    @Test
    void testFetchProductByAvailability_Failure() {
        when(productRepository.findByInStock(false)).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.fetchProductByAvailability(false);
        assertFalse(result.isPresent());
    }

    // ✅ Test: Delete Product By ID (Success)
    @Test
    void testDeleteProductById_Success() {
        when(productRepository.existsById("prod_001")).thenReturn(true);
        when(productRepository.getReferenceById("prod_001")).thenReturn(product);

        Optional<String> result = productDao.deleteProductById("prod_001");

        assertTrue(result.isPresent());
        assertEquals("Product successfully deleted with Id:prod_001", result.get());
    }

    // ✅ Test: Delete Product By ID (Failure - Product Not Found)
    @Test
    void testDeleteProductById_Failure() {
        when(productRepository.existsById("invalid_id")).thenReturn(false);

        Optional<String> result = productDao.deleteProductById("invalid_id");
        assertFalse(result.isPresent());
    }

    // ✅ Test: Update Product (Success)
    @Test
    void testUpdateProduct_Success() {
        // Arrange
        String productId = "prod_001";

        // Old product before update
        Product oldProduct = new Product();
        oldProduct.setProductId(productId);
        oldProduct.setProductName("Old Laptop");
        oldProduct.setProductPrice(40000.0);
        oldProduct.setProductQuantity(5);
        oldProduct.setInStock(true);
        oldProduct.setCategory(category);

        // Updated product details
        Product updatedProduct = new Product();
        updatedProduct.setProductId(productId);
        updatedProduct.setProductName("New Gaming Laptop");
        updatedProduct.setProductPrice(60000.0);
        updatedProduct.setProductQuantity(15);
        updatedProduct.setInStock(false);
        updatedProduct.setCategory(category);

        // Mock repository calls
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReferenceById(productId)).thenReturn(oldProduct);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Optional<Product> result = productDao.updateProduct(updatedProduct);

        // Assert
        assertTrue(result.isPresent(), "Product update failed - returned empty Optional<>");
        assertEquals("New Gaming Laptop", result.get().getProductName(), "Product name mismatch");
        assertEquals(60000.0, result.get().getProductPrice(), "Product price mismatch");
        assertEquals(15, result.get().getProductQuantity(), "Product quantity mismatch");
        assertFalse(result.get().getInStock(), "Product stock status mismatch");

        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ✅ Test: Update Product (Failure - Product Not Found)
    @Test
    void testUpdateProduct_Failure() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("invalid_id");

        when(productRepository.existsById(updatedProduct.getProductId())).thenReturn(false);

        Optional<Product> result = productDao.updateProduct(updatedProduct);
        assertFalse(result.isPresent());
    }

    // ✅ Test: Search Product By Name (Success)
    @Test
    void testSearchProductByName_Success() {
        when(productRepository.findByProductNameContainingIgnoreCase("Laptop"))
                .thenReturn(Arrays.asList(product));

        Optional<List<Product>> result = productDao.searchProductByName("Laptop");
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    // ✅ Test: Search Product By Name (Failure - No Match)
    @Test
    void testSearchProductByName_Failure() {
        when(productRepository.findByProductNameContainingIgnoreCase("Unknown")).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.searchProductByName("Unknown");
        assertFalse(result.isPresent());
    }
}
