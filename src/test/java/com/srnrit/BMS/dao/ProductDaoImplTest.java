package com.srnrit.BMS.dao;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.srnrit.BMS.dao.impl.ProductDaoImpl;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;
import com.srnrit.BMS.repository.ProductRepository;

class ProductDaoImplTest {

    @InjectMocks
    private ProductDaoImpl productDao;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test: Save Product (Success)
    @Test
    void testSaveProduct_Success() {
        String categoryId = "category001";
        Category category = new Category();
        category.setCategoryId(categoryId);

        Product product = new Product();
        product.setProductId("prod_001");
        product.setProductName("Laptop");

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<Product> result = productDao.saveProduct(product, categoryId);
        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getProductName());
    }

    // ✅ Test: Save Product (Failure - Category Not Found)
    @Test
    void testSaveProduct_CategoryNotFound() {
        String categoryId = "invalid_category";
        Product product = new Product();
        product.setProductName("Mobile");

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> productDao.saveProduct(product, categoryId));
    }

    // ✅ Test: Fetch Product By Availability (Success)
    @Test
    void testFetchProductByAvailability_Success() {
        Product product1 = new Product();
        product1.setProductId("prod_001");
        product1.setInStock(true);

        Product product2 = new Product();
        product2.setProductId("prod_002");
        product2.setInStock(true);

        when(productRepository.findByInStock(true)).thenReturn(Arrays.asList(product1, product2));

        Optional<List<Product>> result = productDao.fetchProductByAvailability(true);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    // ✅ Test: Fetch Product By Availability (Failure - No Products)
    @Test
    void testFetchProductByAvailability_Failure() {
        when(productRepository.findByInStock(false)).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.fetchProductByAvailability(false);
        assertFalse(result.isPresent());
    }

    // ✅ Test: Delete Product By ID (Success)
    @Test
    void testDeleteProductById_Success() {
        String productId = "prod_001";
        Product product = new Product();
        product.setProductId(productId);
        Category category = new Category();
        category.addProduct(product);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReferenceById(productId)).thenReturn(product);

        Optional<String> result = productDao.deleteProductById(productId);
        assertTrue(result.isPresent());
        assertEquals("Product successfully deleted with Id:prod_001", result.get());
    }

    // ✅ Test: Delete Product By ID (Failure - Product Not Found)
    @Test
    void testDeleteProductById_Failure() {
        String productId = "invalid_id";
        when(productRepository.existsById(productId)).thenReturn(false);

        Optional<String> result = productDao.deleteProductById(productId);
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        String productId = "prod_001";

        // Old product before update
        Product oldProduct = new Product();
        oldProduct.setProductId(productId);
        oldProduct.setProductName("Old Name");
        oldProduct.setProductImage("old.png");
        oldProduct.setProductQuantity(5);
        oldProduct.setProductPrice(500.0);
        oldProduct.setInStock(true);
        Category category = new Category();
        oldProduct.setCategory(category);

        // Updated product details
        Product updatedProduct = new Product();
        updatedProduct.setProductId(productId);
        updatedProduct.setProductName("New Name");
        updatedProduct.setProductImage("updated.png");
        updatedProduct.setProductQuantity(10);
        updatedProduct.setProductPrice(700.0);
        updatedProduct.setInStock(false);
        updatedProduct.setCategory(category);

        // Mock repository calls
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReferenceById(productId)).thenReturn(oldProduct);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Optional<Product> result = productDao.updateProduct(updatedProduct);

        // Log before assertions
        if (result.isPresent()) {
            log.info("✅ Product update successful! New details: {}", result.get());
        } else {
            log.error("❌ Product update failed! Returned Optional.empty()");
        }

        // Assert
        assertTrue(result.isPresent(), "Product update failed - returned empty Optional<>");
        assertEquals("New Name", result.get().getProductName(), "Product name mismatch");
        assertEquals("updated.png", result.get().getProductImage(), "Product image mismatch");
        assertEquals(10, result.get().getProductQuantity(), "Product quantity mismatch");
        assertEquals(700.0, result.get().getProductPrice(), "Product price mismatch");
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
        Product product1 = new Product();
        product1.setProductId("prod_001");
        product1.setProductName("Laptop");

        Product product2 = new Product();
        product2.setProductId("prod_002");
        product2.setProductName("Gaming Laptop");

        when(productRepository.findByProductNameContainingIgnoreCase("Laptop"))
                .thenReturn(Arrays.asList(product1, product2));

        Optional<List<Product>> result = productDao.searchProductByName("Laptop");
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    // ✅ Test: Search Product By Name (Failure - No Match)
    @Test
    void testSearchProductByName_Failure() {
        when(productRepository.findByProductNameContainingIgnoreCase("Unknown")).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.searchProductByName("Unknown");
        assertFalse(result.isPresent());
    }
}