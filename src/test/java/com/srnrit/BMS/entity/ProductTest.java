package com.srnrit.BMS.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    
    private Product product;
    private Category category;


    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryName("Food");
        
        product = new Product("Bongu Biryani", 10, 299.99, true);

    @Test
    void productTest() {
        // Create a product instance
        Product product = new Product("Bongu Biryani", 10, 299.99, true);


        product.setProductId("P12345");
        product.setProductImage("biryani.jpg");
        product.setCategory(category);
    }

    @Test
    void testProductFields() {
        assertEquals("P12345", product.getProductId());
        assertEquals("Bongu Biryani", product.getProductName());
        assertEquals("biryani.jpg", product.getProductImage());
        assertEquals(10, product.getProductQuantity());
        assertEquals(299.99, product.getProductPrice());
        assertTrue(product.getInStock());
    }

    @Test

    void testProductCategory() {

    void productWithCategoryTest() {
        // Create a category
        Category category = new Category();
        category.setCategoryName("Food");

        // Create a product and assign it to the category
        Product product = new Product("Bongu Biryani", 10, 299.99, true);
        product.setCategory(category);

        // Assertions

        assertNotNull(product.getCategory());
        assertEquals("Food", product.getCategory().getCategoryName());
    }
}
