package com.srnrit.BMS.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.IProductService;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(ProductController.class)
public class ProductControllerTest 
{
	
	    private MockMvc mockMvc;

	    @Mock
	    private IProductService productService;

	    @InjectMocks
	    private ProductController productController;

	    private ObjectMapper objectMapper;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	        objectMapper = new ObjectMapper();
	    }

	 /*   @Test
	    void testGetAllProducts_Success() throws Exception {
	        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("No products found in the database."));
	    }*/
	    
	 // Test case: Add Product by Category (Successful)
	    @Test
	    void testAddProductByCategory_Success() throws Exception {
	        ProductRequestDTO requestDTO = new ProductRequestDTO();
	        requestDTO.setProductName("Test Product");

	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Test Product");

	        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
	        MockMultipartFile productJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestDTO));

	        when(productService.storeProduct(any(ProductRequestDTO.class), eq("CAT123"), any(MultipartFile.class)))
	                .thenReturn(responseDTO);

	        mockMvc.perform(multipart("/product/addProductByCategory/CAT123")
	                .file(file)
	                .file(productJson)
	                .contentType(MediaType.MULTIPART_FORM_DATA))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.productName").value("Test Product"));
	    }

	    // Test case: Fetch Product by Availability (Available)
	    @Test
	    void testFetchProductByAvailability_Available() throws Exception {
	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Available Product");

	        when(productService.fetchProductByAvailability(true))
	                .thenReturn(Collections.singletonList(responseDTO));

	        mockMvc.perform(get("/product/fetchByAvailability?inStock=true"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].productName").value("Available Product"));
	    }

	    // Test case: Fetch Product by Availability (No Products Found)
	    @Test
	    void testFetchProductByAvailability_NoProducts() throws Exception {
	        when(productService.fetchProductByAvailability(true)).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/fetchByAvailability?inStock=true"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("No products available with the given availability status."));
	    }

	    // Test case: Delete Product by ID (Successful)
	    @Test
	    void testDeleteProductById_Success() throws Exception {
	        when(productService.deleteProductByProductId("PID123")).thenReturn("Product deleted successfully");

	        mockMvc.perform(delete("/product/delete/PID123"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.status").value("success"))
	                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
	    }

	    // Test case: Delete Product by ID (Product Not Found)
	    @Test
	    void testDeleteProductById_NotFound() throws Exception {
	        when(productService.deleteProductByProductId("INVALID_ID"))
	                .thenThrow(new ProductNotFoundException("Product not found"));

	        mockMvc.perform(delete("/product/delete/INVALID_ID"))
	                .andExpect(status().isNotFound())
	                .andExpect(jsonPath("$.message").value("Product not found for deletion."));
	    }


	    // Test case: Search Product by Name (Success)
	    @Test
	    void testSearchProductByName_Success() throws Exception {
	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Test Product");

	        when(productService.getProductByProductName("Test Product")).thenReturn(responseDTO);

	        mockMvc.perform(get("/product/searchByName?productName=Test Product"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.productName").value("Test Product"));
	    }

	    // Test case: Search Product by Name (Not Found)
	    @Test
	    void testSearchProductByName_NotFound() throws Exception {
	        when(productService.getProductByProductName("NonExistent"))
	                .thenThrow(new ProductNotFoundException("No product found"));

	        mockMvc.perform(get("/product/searchByName?productName=NonExistent"))
	                .andExpect(status().isNotFound())
	                .andExpect(content().string("Error: No product found"));
	    }

	    // Test case: Get All Products (Success)
	    @Test
	    void testGetAllProducts_Success() throws Exception {
	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Product1");

	        when(productService.getAllProducts()).thenReturn(Collections.singletonList(responseDTO));

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].productName").value("Product1"));
	    }

	    // Test case: Get All Products (No Products Found)
	    @Test
	    void testGetAllProducts_NoProducts() throws Exception {
	        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("No products found in the database."));
	    }

	    
	 // Test case: Add Product by Category - Missing Image
	    @Test
	    void testAddProductByCategory_MissingImage() throws Exception {
	        ProductRequestDTO requestDTO = new ProductRequestDTO();
	        requestDTO.setProductName("Test Product");

	        MockMultipartFile productJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestDTO));

	        mockMvc.perform(multipart("/product/addProductByCategory/CAT123")
	                .file(productJson) // No image file included
	                .contentType(MediaType.MULTIPART_FORM_DATA))
	                .andExpect(status().isBadRequest());
	    }

	   
	    
	 // Test case: Delete Product - Invalid ID
	    @Test
	    void testDeleteProduct_InvalidId() throws Exception {
	        when(productService.deleteProductByProductId("INVALID_ID"))
	                .thenThrow(new ProductNotFoundException("Product not found"));

	        mockMvc.perform(delete("/product/delete/INVALID_ID"))
	                .andExpect(status().isNotFound())
	                .andExpect(content().string(containsString("Product not found")));
	    }
	    
	 // Test case: Fetch All Products - Empty Database
	    @Test
	    void testGetAllProducts_Empty() throws Exception {
	        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("No products found in the database")));
	    }

	    @Test
	    void testSearchProductByName_CaseInsensitive() throws Exception {
	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Test Product");

	        when(productService.getProductByProductName(anyString())).thenReturn(responseDTO);

	        mockMvc.perform(get("/product/searchByName")
	                .param("productName", "TEST PRODUCT"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.productName").value("Test Product"));
	    }


	 // Test case: Fetch Products by Availability - No Matching Products
	    @Test
	    void testFetchProductByAvailability_NoResults() throws Exception {
	        when(productService.fetchProductByAvailability(false)).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/fetchByAvailability")
	                .param("inStock", "false"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("No products available with the given availability status.")));
	    }



	 // Test case: Delete Product - Successful Deletion
	    @Test
	    void testDeleteProduct_Success() throws Exception {
	        when(productService.deleteProductByProductId("PID123")).thenReturn("Product deleted successfully");

	        mockMvc.perform(delete("/product/delete/PID123"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("Product deleted successfully")));
	    }

	    
	    @Test
	    void testFetchAllProducts_EmptyDatabase() throws Exception {
	        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("No products found in the database."));
	    }

	 

	    
	    @Test
	    void testFetchProductByAvailability_InvalidInput() throws Exception {
	        mockMvc.perform(get("/product/fetchByAvailability")
	                .param("inStock", "invalidValue"))
	                .andExpect(status().isBadRequest());
	    }

	   

	    
	    @Test
	    void testFetchProductById_ValidId() throws Exception {
	        ProductResponseDTO responseDTO = new ProductResponseDTO();
	        responseDTO.setProductName("Test Product");

	        when(productService.getProductByProductName(anyString())).thenReturn(responseDTO);

	        mockMvc.perform(get("/product/searchByName")
	                .param("productName", "Test Product"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.productName").value("Test Product"));
	    }

	    @Test
	    void testFetchProductByAvailability_NullParam() throws Exception {
	        mockMvc.perform(get("/product/fetchByAvailability"))
	                .andExpect(status().isOk()); // Should return default inStock=true
	    }
	    
	    @Test
	    void testDeleteProduct_AlreadyDeleted() throws Exception {
	        when(productService.deleteProductByProductId("PID123"))
	                .thenThrow(new ProductNotFoundException("Product not found"));

	        mockMvc.perform(delete("/product/delete/PID123"))
	                .andExpect(status().isNotFound())
	                .andExpect(content().string(containsString("Product not found")));
	    }


	    @Test
	    void testUpdateProduct_NullRequestBody() throws Exception {
	        mockMvc.perform(put("/product/update/PID123")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("")) // Empty content
	                .andExpect(status().isBadRequest());
	    }


	    @Test
	    void testGetAllProducts_EmptyDatabase() throws Exception {
	        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/getAllProducts"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("No products found in the database.")));
	    }

	    
	    @Test
	    void testFetchProductByAvailability_InvalidType() throws Exception {
	        mockMvc.perform(get("/product/fetchByAvailability")
	                .param("inStock", "invalidValue")) // Non-boolean value
	                .andExpect(status().isBadRequest());
	    }
	    
	    @Test
	    void testAddProductByCategory_MissingFile() throws Exception {
	        ProductRequestDTO requestDTO = new ProductRequestDTO();
	        requestDTO.setProductName("Test Product");

	        mockMvc.perform(multipart("/product/addProductByCategory/CAT123")
	                .param("product", objectMapper.writeValueAsString(requestDTO))
	                .contentType(MediaType.MULTIPART_FORM_DATA))
	                .andExpect(status().isBadRequest());
	    }


	    @Test
	    void testFetchProductByAvailability_False_NoProducts() throws Exception {
	        when(productService.fetchProductByAvailability(false)).thenReturn(Collections.emptyList());

	        mockMvc.perform(get("/product/fetchByAvailability")
	                .param("inStock", "false"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("No products available")));
	    }


	    @Test
	    void testDeleteProduct_ValidId() throws Exception {
	        when(productService.deleteProductByProductId("PID123")).thenReturn("Product deleted successfully");

	        mockMvc.perform(delete("/product/delete/PID123"))
	                .andExpect(status().isOk())
	                .andExpect(content().string(containsString("Product deleted successfully")));
	    }

	   

	}



