package com.srnrit.BMS.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testAddProductByCategory_Success() throws Exception {
        MockMultipartFile image = new MockMultipartFile("productImage", "test.jpg", "image/jpeg", "image data".getBytes());

        ProductRequestDTO requestDTO = new ProductRequestDTO("1", "TestProduct", null, 10, 100.0, true);
        ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "1", 10, 100.0, true);

        when(productService.storeProduct(any(ProductRequestDTO.class), any(MockMultipartFile.class))).thenReturn(responseDTO);

        mockMvc.perform(multipart("/product/addProductByCategory")
                        .file(image)
                        .param("categoryId", "1")
                        .param("productName", "TestProduct")
                        .param("productQuantity", "10")
                        .param("productPrice", "100.0")
                        .param("inStock", "true"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("TestProduct"));

        verify(productService, times(1)).storeProduct(any(ProductRequestDTO.class), any(MockMultipartFile.class));
    }

    @Test
    void testAddProductByCategory_CategoryNotFound() throws Exception {
        when(productService.storeProduct(any(ProductRequestDTO.class), any(MockMultipartFile.class)))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(multipart("/product/addProductByCategory")
                        .file(new MockMultipartFile("productImage", "test.jpg", "image/jpeg", "image data".getBytes()))
                        .param("categoryId", "1")
                        .param("productName", "TestProduct")
                        .param("productQuantity", "10")
                        .param("productPrice", "100.0")
                        .param("inStock", "true"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error: Category not found"));
    }

    @Test
    void testFetchProductByAvailability_Success() throws Exception {
        ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "1", 10, 100.0, true);

        when(productService.fetchProductByAvailability(true)).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/product/fetchByAvailability").param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("TestProduct"));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        when(productService.deleteProductByProductId("1")).thenReturn("Product deleted successfully");

        mockMvc.perform(delete("/product/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        when(productService.deleteProductByProductId("1")).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(delete("/product/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error: Product not found"));
    }
}
