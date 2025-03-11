package com.srnrit.BMS.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.impl.ProductDaoImpl;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
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
	        category.setCategoryId("1");
	        category.setCategoryname("Electronics");

	        product = new Product();
	        product.setProductId("101");
	        product.setProductName("Laptop");
	        product.setProductPrice(50000.0);
	        product.setProductQuantity(10);
	        product.setInStock(true);
	        product.setCategory(category);
	    }
	    
//	    @Test
//	    void testSaveProduct_Success() {
//	        when(categoryRepository.existsById("1")).thenReturn(true);
//	        when(categoryRepository.getReferenceById("1")).thenReturn(category);
//	        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//	        Optional<Product> savedProduct = productDao.saveProduct(product, "1");
//
//	        assertTrue(savedProduct.isPresent());
//	        assertEquals("Laptop", savedProduct.get().getProductName());
//	    }
	    
	    @Test
	    void testFetchProductByAvailability() {
	        when(productRepository.findByInStock(true)).thenReturn(Arrays.asList(product));

	        Optional<List<Product>> products = productDao.fetchProductByAvailability(true);

	        assertTrue(products.isPresent());
	        assertEquals(1, products.get().size());
	    }
	    
	    @Test
	    void testDeleteProductById_Success() {
	        when(productRepository.existsById("101")).thenReturn(true);
	        when(productRepository.getReferenceById("101")).thenReturn(product);

	        Optional<String> result = productDao.deleteProductById("101");

	        assertTrue(result.isPresent());
	        assertEquals("Product successfully deleted with Id:101", result.get());
	    }
	    
//	    @Test
//	    void testDeleteProductById_NotFound() {
//	        when(productRepository.existsById("999")).thenReturn(false);
//
//	        Optional<String> result = productDao.deleteProductById("999");
//
//	        assertFalse(result.isPresent());
//	    }
	    
//	    @Test
//	    void testUpdateProduct_Success() {
//	        when(productRepository.existsById("101")).thenReturn(true);
//	        when(productRepository.getReferenceById("101")).thenReturn(product);
//	        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//	        Optional<Product> updatedProduct = productDao.updateProduct(product);
//
//	        assertTrue(updatedProduct.isPresent());
//	        assertEquals(50000.0, updatedProduct.get().getProductPrice());
//	    }
	    
//	    @Test
//	    void testUpdateProduct_NotFound() {
//	        when(productRepository.existsById("999")).thenReturn(false);
//
//	        Optional<Product> updatedProduct = productDao.updateProduct(product);
//
//	        assertFalse(updatedProduct.isPresent());
//	    }

	     
	

	
}
