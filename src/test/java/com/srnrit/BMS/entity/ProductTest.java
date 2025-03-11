package com.srnrit.BMS.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void productTest() {
        // Create a product instance
        Product product = new Product("Bongu Biryani", "biryani.jpg", 10, 299.99, true);

        // Set additional fields
        product.setProductId("P12345");

        // Assertions to verify the object properties
        assertEquals("P12345", product.getProductId());
        assertEquals("Bongu Biryani", product.getProductName());
        assertEquals("biryani.jpg", product.getProductImage());
        assertEquals(10, product.getProductQuantity());
        assertEquals(299.99, product.getProductPrice());
        assertTrue(product.getInStock());

        // Print product object
        System.out.println(product);
    }

    @Test
    void productWithCategoryTest() {
        // Create a category
        Category category = new Category();
        category.setCategoryName("Food");

        // Create a product and assign it to the category
        Product product = new Product("Bongu Biryani", "biryani.jpg", 10, 299.99, true);
        product.setCategory(category);

        // Assertions
        assertNotNull(product.getCategory());
        assertEquals("Food", product.getCategory().getCategoryName());

        System.out.println("Product with Category: " + product);
    }
}

