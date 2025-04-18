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

    // Test: Save Product (Success)
    @Test
    void testSaveProduct_Success() {
        String categoryId = "category001";
        Category category = new Category();
        category.setCategoryId(categoryId);

        Product product = new Product();
        product.setProductId("prod_001");
        product.setProductName("Laptop");
        product.setProductQuantity(3);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<Product> result = productDao.saveProduct(product, categoryId);
        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getProductName());
        assertEquals("prod_001", result.get().getProductId());
        
    }

    // Test: Save Product (Failure - Category Not Found)
    @Test
    void testSaveProduct_CategoryNotFound() {
        String categoryId = "invalid_category";
        Product product = new Product();
        product.setProductName("Mobile");

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> productDao.saveProduct(product, categoryId));
    }
    
    // Test: Save Product (Failure Duplicate Product Name)
    @Test
    void testSaveProduct_DuplicateProductName() {
        String categoryId = "category001";
        Category category = new Category();
        category.setCategoryId(categoryId);

        Product product = new Product();
        product.setProductName("Laptop");

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(productRepository.existsByProductNameIgnoreCase("Laptop")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productDao.saveProduct(product, categoryId));
    }

    
    //Save Product (Failure - Null Product)
    @Test
    void testSaveProduct_NullProduct() {
        String categoryId = "category001";
        assertThrows(IllegalArgumentException.class, () -> productDao.saveProduct(null, categoryId));
    }


    //Test: Save Product (Failure - Empty Product Name)
    @Test
    void testSaveProduct_EmptyProductName() {
        String categoryId = "category001";
        Product product = new Product();
        product.setProductName("");  // Empty name

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, () -> productDao.saveProduct(product, categoryId));
    }


    // Test: Fetch Product By Availability (Success)
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

    // Test: Fetch Product By Availability (Failure - No Products)
    @Test
    void testFetchProductByAvailability_Failure() {
        when(productRepository.findByInStock(false)).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.fetchProductByAvailability(false);
        assertFalse(result.isPresent());
    }

    // Test : Fetch Product By Availability (Failure - Null Availability)
    @Test
    void testFetchProductByAvailability_NullAvailability() {
        assertThrows(NullPointerException.class, () -> productDao.fetchProductByAvailability(null));
    }
    
    //Test: Fetch Product By Availability (Failure - No Products Exist in DB at all)
    @Test
    void testFetchProductByAvailability_NoProductsInDB() {
        when(productRepository.findByInStock(true)).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.fetchProductByAvailability(true);
        assertFalse(result.isPresent());
    }



    // Test: Delete Product By ID (Success)
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

    // Test: Delete Product By ID (Failure - Product Not Found)
    @Test
    void testDeleteProductById_Failure() {
        String productId = "invalid_id";
        when(productRepository.existsById(productId)).thenReturn(false);

        Optional<String> result = productDao.deleteProductById(productId);
        assertFalse(result.isPresent());
    }
    
    //Test : Delete Product By ID (Failure - Null ID)
    @Test
    void testDeleteProductById_NullId() {
        assertThrows(NullPointerException.class, () -> productDao.deleteProductById(null));
    }
    
    //Test: Delete Product By ID (Failure - Product Belongs to Multiple Categories, Check Behavior)
    @Test
    void testDeleteProductById_ProductWithMultipleCategories() {
        String productId = "prod_001";
        Product product = new Product();
        product.setProductId(productId);

        Category category1 = new Category();
        Category category2 = new Category();
        
        category1.addProduct(product);
        category2.addProduct(product);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReferenceById(productId)).thenReturn(product);

        Optional<String> result = productDao.deleteProductById(productId);
        
        assertTrue(result.isPresent());
        assertEquals("Product successfully deleted with Id:prod_001", result.get());
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

        // Log the returned product details before assertions
        if (result.isPresent()) {
            System.out.println("Product update successful! New details: " + result.get());
        } else {
            System.err.println("Product update failed! Returned Optional.empty()");
        }

        // Assertions with better failure messages
        assertTrue(result.isPresent(), "Product update failed - returned empty Optional");
        assertEquals("New Name", result.get().getProductName(), "Product name mismatch");
        assertEquals("updated.png", result.get().getProductImage(), "Product image mismatch");
        assertEquals(10, result.get().getProductQuantity(), "Product quantity mismatch");
        assertEquals(700.0, result.get().getProductPrice(), "Product price mismatch");
        assertFalse(result.get().getInStock(), "Product stock status mismatch");

        // Verify save() was called once
        verify(productRepository, times(1)).save(any(Product.class));
    }



    // Test: Update Product (Failure - Product Not Found)
    @Test
    void testUpdateProduct_Failure() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("invalid_id");

        when(productRepository.existsById(updatedProduct.getProductId())).thenReturn(false);

        Optional<Product> result = productDao.updateProduct(updatedProduct);
        assertFalse(result.isPresent());
    }
    
    //Test: Update Product (Failure - Null Product)
    @Test
    void testUpdateProduct_NullProduct() {
        assertThrows(NullPointerException.class, () -> productDao.updateProduct(null));
    }
    
    //Test: Update Product (Failure - Update with Same Values, Check Behavior)
    @Test
    void testUpdateProduct_NoChanges() {
        String productId = "prod_001";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Laptop");
        existingProduct.setProductImage("laptop.png");
        existingProduct.setProductQuantity(5);
        existingProduct.setProductPrice(1000.0);
        existingProduct.setInStock(true);
        
        // New product with same values
        Product updatedProduct = new Product();
        updatedProduct.setProductId(productId);
        updatedProduct.setProductName("Laptop");
        updatedProduct.setProductImage("laptop.png");
        updatedProduct.setProductQuantity(5);
        updatedProduct.setProductPrice(1000.0);
        updatedProduct.setInStock(true);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.getReferenceById(productId)).thenReturn(existingProduct);
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Optional<Product> result = productDao.updateProduct(updatedProduct);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getProductName());
        assertEquals("laptop.png", result.get().getProductImage());
        assertEquals(5, result.get().getProductQuantity());
        assertEquals(1000.0, result.get().getProductPrice());
        assertTrue(result.get().getInStock());
    }



    // Test: Search Product By Name (Success)
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

    // Test: Search Product By Name (Failure - No Match)
    @Test
    void testSearchProductByName_Failure() {
        when(productRepository.findByProductNameContainingIgnoreCase("Unknown")).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.searchProductByName("Unknown");
        assertFalse(result.isPresent());
    }

    //Test: Search Product By Name (Failure - Null Name)
    @Test
    void testSearchProductByName_NullName() {
        assertThrows(NullPointerException.class, () -> productDao.searchProductByName(null));
    }
    
    //Test: Search Product By Name (Case Insensitivity)
    @Test
    void testSearchProductByName_CaseInsensitive() {
        Product product1 = new Product();
        product1.setProductId("prod_001");
        product1.setProductName("Laptop");

        List<Product> mockProductList = Arrays.asList(product1);

        when(productRepository.findByProductNameContainingIgnoreCase(anyString()))
                .thenReturn(mockProductList);  // Ensure mock returns expected data

        Optional<List<Product>> result = productDao.searchProductByName("LAPTOP");

        assertTrue(result.isPresent(), "Expected a non-empty result but got empty");
        assertEquals(1, result.get().size(), "Expected exactly one product in result");
        assertEquals("Laptop", result.get().get(0).getProductName(), "Product name mismatch");
    }
   
    //Test: Fetch All Products (Success)
    @Test
    void testFetchAllProduct_Success() {
        Product product1 = new Product();
        product1.setProductId("prod_001");

        Product product2 = new Product();
        product2.setProductId("prod_002");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        Optional<List<Product>> result = productDao.fetchAllProduct();
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }
    
    //Test: Fetch All Products (Failure)
    @Test
    void testFetchAllProduct_Failure() {
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        Optional<List<Product>> result = productDao.fetchAllProduct();
        assertFalse(result.isPresent());
    }


}
