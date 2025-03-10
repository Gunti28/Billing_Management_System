package com.srnrit.BMS.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.impl.ProductDaoImpl;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;
import com.srnrit.BMS.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductDaoTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductDaoImpl productDao;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProductId("P123");
        product.setProductName("Apple");
        product.setInStock(true);
        product.setProductQuantity(100);
    }

    @Test
    void testSaveProduct_Success() {
        Category category = new Category();
        when(categoryRepository.existsById("C001")).thenReturn(true);
        when(categoryRepository.getReferenceById("C001")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<Product> savedProduct = productDao.saveProduct(product, "C001");

        assertTrue(savedProduct.isPresent());
        assertEquals("Apple", savedProduct.get().getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProduct_CategoryNotFound() {
        when(categoryRepository.existsById("C999")).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> productDao.saveProduct(product, "C999"));
    }

    @Test
    void testFetchProductByAvailability() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findByInStock(true)).thenReturn(products);

        Optional<List<Product>> result = productDao.fetchProductByAvailability(true);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void testFetchAllProducts_Empty() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        Optional<List<Product>> result = productDao.fetchAllProduct();

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteProductById_Success() {
        when(productRepository.existsById("P123")).thenReturn(true);
        when(productRepository.getReferenceById("P123")).thenReturn(product);

        Optional<String> response = productDao.deleteProductById("P123");

        assertTrue(response.isPresent());
        assertTrue(response.get().contains("Product successfully deleted"));
        verify(productRepository, times(1)).deleteById("P123");
    }

    @Test
    void testDeleteProductById_NotFound() {
        when(productRepository.existsById("P999")).thenReturn(false);

        Optional<String> response = productDao.deleteProductById("P999");

        assertTrue(response.isEmpty());
    }

    @Test
    void testUpdateProduct_Success() {
        Category mockCategory = mock(Category.class);
        Product spyProduct = spy(product);
        List<Product> productList = new ArrayList<>();
        productList.add(spyProduct);

        when(productRepository.existsById("P123")).thenReturn(true);
        when(productRepository.getReferenceById("P123")).thenReturn(spyProduct);
        when(productRepository.save(any(Product.class))).thenReturn(spyProduct);
        when(spyProduct.getCategory()).thenReturn(mockCategory);
        when(mockCategory.getProducts()).thenReturn(productList);

        spyProduct.setProductName("Updated Apple");

        Optional<Product> updatedProduct = productDao.updateProduct(spyProduct);

        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Apple", updatedProduct.get().getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.existsById("P999")).thenReturn(false);

        Optional<Product> updatedProduct = productDao.updateProduct(product);

        assertTrue(updatedProduct.isEmpty());
    }

    @Test
    void testSearchProductByName() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findByProductNameContainingIgnoreCase("Apple")).thenReturn(products);

        Optional<List<Product>> result = productDao.searchProductByName("Apple");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void testFetchAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findAll()).thenReturn(products);

        Optional<List<Product>> result = productDao.fetchAllProduct();

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }
}
