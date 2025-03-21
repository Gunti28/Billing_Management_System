package com.srnrit.BMS.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.service.IProductService;

class ProductControllerTest {

	@Mock
	private IProductService productService;

	@InjectMocks
	private ProductController productController;

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper(); // <-- Ensure initialization

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}

	// 1. Add Product by Category (Success)
	@Test
	void testAddProductByCategory_Success() throws Exception {
		MockMultipartFile image = new MockMultipartFile("productImage", "test.jpg", "image/jpeg",
				"image data".getBytes());

		ProductRequestDTO requestDTO = new ProductRequestDTO();
		ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "1", 10, 100.0, true);

		when(productService.storeProduct(any(ProductRequestDTO.class), null, image)).thenReturn(responseDTO);

		mockMvc.perform(multipart("/product/addProductByCategory").file(image).param("categoryId", "1")
				.param("productName", "TestProduct").param("productQuantity", "10").param("productPrice", "100.0")
				.param("inStock", "true")).andExpect(status().isCreated())
				.andExpect(jsonPath("$.productName").value("TestProduct"));

		verify(productService, times(1)).storeProduct(any(ProductRequestDTO.class), null, image);
	}

	// 1.1 Add Product by Category (Category Not Found)
	@Test
	void testAddProductByCategory_CategoryNotFound() throws Exception {
		when(productService.storeProduct(any(ProductRequestDTO.class), any(), any()))
				.thenThrow(new CategoryNotFoundException("Category not found"));

		mockMvc.perform(multipart("/product/addProductByCategory")
				.file(new MockMultipartFile("productImage", "test.jpg", "image/jpeg", "image data".getBytes()))
				.param("categoryId", "1").param("productName", "TestProduct").param("productQuantity", "10")
				.param("productPrice", "100.0").param("inStock", "true")).andExpect(status().isNotFound())
				.andExpect(content().string("Error: Category not found"));
	}

	// 2. Fetch Products by Availability (Success)
	@Test
	void testFetchProductByAvailability_Success() throws Exception {
		ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "1", 10, 100.0, true);

		when(productService.fetchProductByAvailability(true)).thenReturn(Collections.singletonList(responseDTO));

		mockMvc.perform(get("/product/fetchByAvailability").param("inStock", "true")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].productName").value("TestProduct"));
	}

	// 2.1 Fetch Products by Availability (No Products Found)
	@Test
	void testFetchProductByAvailability_NoProducts() throws Exception {
		when(productService.fetchProductByAvailability(false)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/product/fetchByAvailability").param("inStock", "false")).andExpect(status().isOk())
				.andExpect(content().string("No products available with the given availability status."));
	}

	// 3. Delete Product by ID (Success)
	@Test
	void testDeleteProduct_Success() throws Exception {
		String productId = "P123";
		when(productService.deleteProductByProductId(productId)).thenReturn("Product deleted successfully");

		// Ensure the path matches the controller's @DeleteMapping
		mockMvc.perform(delete("/product/delete/{productId}", productId)).andExpect(status().isOk())
				.andExpect(content().string("Product deleted successfully"));

		verify(productService, times(1)).deleteProductByProductId(productId);
	}

	// 3.1 Delete Product by ID (Product Not Found)
	@Test
	void testDeleteProduct_ProductNotFound() throws Exception {
		String productId = "P999";
		when(productService.deleteProductByProductId(productId))
				.thenThrow(new ProductNotFoundException("Product not found"));

		mockMvc.perform(delete("/product/delete/{productId}", productId)) // Adjust path if needed
				.andExpect(status().isNotFound()).andExpect(content().string("Error: Product not found")); // Ensure
																											// message
																											// matches
																											// exactly

		verify(productService, times(1)).deleteProductByProductId(productId);
	}

	@Test
	void testDeleteProduct_InternalServerError() throws Exception {
		String productId = "P456";

		doThrow(new RuntimeException("Unexpected error")).when(productService).deleteProductByProductId(productId);

		mockMvc.perform(delete("/product/delete/{productId}", productId)) // Adjust path if needed
				.andExpect(status().isInternalServerError()) // Expect 500
				.andExpect(
						content().string("An unexpected error occurred while deleting the product: Unexpected error"));

		verify(productService, times(1)).deleteProductByProductId(productId);
	}

	// 4. Update Product (Success)
	@Test
	void testUpdateProduct_Success() throws Exception {
		// Mock request & response (Don't use MultipartFile)
		ProductRequestDTO requestDTO = new ProductRequestDTO("category001", null, 1190.67, false); // Use `null` for
																									// MultipartFile

		ProductResponseDTO responseDTO = new ProductResponseDTO("prodId_013", "Updated Mutton SP Biryani",
				"category001", 0, 1190.67, false);

		when(productService.updateProductByProductId(any(ProductRequestDTO.class), eq("prodId_013")))
				.thenReturn(responseDTO);

		mockMvc.perform(put("/product/update/prodId_013").contentType("application/json")
				.content(objectMapper.writeValueAsString(requestDTO))) // No MultipartFile serialization
				.andExpect(status().isOk()).andExpect(jsonPath("$.productName").value("Updated Mutton SP Biryani"))
				.andExpect(jsonPath("$.productPrice").value(1190.67)).andExpect(jsonPath("$.inStock").value(false));

		verify(productService, times(1)).updateProductByProductId(any(ProductRequestDTO.class), eq("prodId_013"));
	}

	// 4.1 Update Product (Not Found)
	@Test
	void testUpdateProduct_NotFound() throws Exception {
		ProductRequestDTO requestDTO = new ProductRequestDTO("category001", 0, 1190.67, false);

		when(productService.updateProductByProductId(any(ProductRequestDTO.class), eq("invalidId")))
				.thenThrow(new ProductNotFoundException("Product not found"));

		mockMvc.perform(put("/product/update/invalidId").contentType("application/json")
				.content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isNotFound())
				.andExpect(content().string("Error: Product not found"));

		verify(productService, times(1)).updateProductByProductId(any(ProductRequestDTO.class), eq("invalidId"));
	}

	// 5. Search Product by Name (Success)
	@Test
	void testSearchProductByName_Success() throws Exception {
		ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "category001", 10, 100.0, true);

		when(productService.getProductByProductName("TestProduct")).thenReturn(responseDTO);

		mockMvc.perform(get("/product/searchByName").param("productName", "TestProduct")).andExpect(status().isOk())
				.andExpect(jsonPath("$.productName").value("TestProduct"));
	}

	// 5.1 Search Product by Name (Not Found)
	@Test
	void testSearchProductByName_NotFound() throws Exception {
		when(productService.getProductByProductName("UnknownProduct"))
				.thenThrow(new ProductNotFoundException("Product not found"));

		mockMvc.perform(get("/product/searchByName").param("productName", "UnknownProduct"))
				.andExpect(status().isNotFound()).andExpect(content().string("Error: Product not found"));
	}

	// 6. Fetch All Products (Success)
	@Test
	void testFetchAllProducts_Success() throws Exception {
		ProductResponseDTO responseDTO = new ProductResponseDTO("1", "TestProduct", "category001", 10, 100.0, true);

		when(productService.getAllProducts()).thenReturn(Collections.singletonList(responseDTO));

		mockMvc.perform(get("/product/getAllProducts")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].productName").value("TestProduct"));
	}

	// 6.1 Fetch All Products (Empty)
	@Test
	void testFetchAllProducts_Empty() throws Exception {
		when(productService.getAllProducts()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/product/getAllProducts")).andExpect(status().isOk())
				.andExpect(content().string("No products found in the database."));
	}

}
