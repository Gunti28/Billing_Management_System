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


	}



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


