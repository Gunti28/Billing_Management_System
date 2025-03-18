package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.*;
import com.srnrit.BMS.entity.*;
import com.srnrit.BMS.exception.categoryexceptions.*;
import com.srnrit.BMS.mapper.*;
import com.srnrit.BMS.service.impl.CategoryServiceImpl;
import com.srnrit.BMS.util.StringUtils;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private ICategoryDao categoryDAO;

	@Mock
	private StringUtils stringUtils;


	private Category category;
	private Product product;
	private CategoryRequestDTO categoryRequest;
	private CategoryResponseDTO categoryResponse;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		categoryRequest = new CategoryRequestDTO();
		categoryRequest.setCategoryName("Electronics");

		ProductRequestDTO productRequest = new ProductRequestDTO();
		productRequest.setProductName("Laptop");
		productRequest.setProductImage("laptop.jpg");
		productRequest.setProductQuantity(10);
		productRequest.setProductPrice(1000.0);
		productRequest.setInStock(true);

		categoryRequest.setProducts(Collections.singletonList(productRequest));

		category = new Category();
		category.setCategoryId("C_01");
		category.setCategoryName("Electronics");

		product = new Product("Laptop", "laptop.jpg", 10, 1000.0, true);
		category.addProduct(product);


	}

	// Success: Add Category**
	@Test
	void testAddCategory_Success() {
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		categoryResponse = categoryService.addCategoryWithProducts(categoryRequest);

		assertNotNull(categoryResponse);
		assertEquals("Electronics", categoryResponse.getCategoryName());
		assertFalse(categoryResponse.getProducts().isEmpty());
	}

	// Failure: Category Not Created**
	@Test
	void testAddCategory_Failure() {
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.empty());
		assertThrows(CategoryNotCreatedException.class, () -> categoryService.addCategoryWithProducts(categoryRequest));
	}

	//  Test: Failure when category request is null
	@Test
	void testAddCategory_NullRequest() {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> categoryService.addCategoryWithProducts(null));

		assertEquals("Category name cannot be blank and name mustn't be null", exception.getMessage());

		verify(categoryDAO, never()).insertCategory(any());
	}

	//  Failure: Blank Category Name**
	@Test
	void testAddCategory_BlankName() {
		categoryRequest.setCategoryName(" "); 
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategoryWithProducts(categoryRequest));

		assertEquals("Category name cannot be blank and name mustn't be null", exception.getMessage());

		verify(categoryDAO, never()).insertCategory(any());
	}


	//  Test: DTO to Entity conversion
	@Test
	void testDtoToEntityConversionAddCategory() {
		categoryRequest.setCategoryName("Electronics");
		ProductRequestDTO productRequest = new ProductRequestDTO();
		productRequest.setProductName("Laptop");

		List<ProductRequestDTO> products = new ArrayList<>();
		products.add(productRequest);
		categoryRequest.setProducts(products);

		Category convertedCategory = DTOToEntity.categoryRequestDTOToCategory(categoryRequest);

		assertNotNull(convertedCategory);
		assertEquals("Electronics", convertedCategory.getCategoryName());
		assertNotNull(convertedCategory.getProducts());
		assertEquals(1, convertedCategory.getProducts().size());
		assertEquals("Laptop", convertedCategory.getProducts().get(0).getProductName());
	}

	//  Test: Entity to DTO conversion
	@Test
	void testEntityToDTOConversionAddCategory() {
		CategoryResponseDTO convertedDTO = EntityToDTO.toCategoryResponse(category);

		assertNotNull(convertedDTO);
		assertEquals("Electronics", convertedDTO.getCategoryName());
		assertEquals(1, convertedDTO.getProducts().size());
	}

	//  Fetching all categories success case
	@Test
	void testGetAllCategories_Success() {
		List<Category> categoryList = Collections.singletonList(category);
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(categoryList));

		List<CategoryResponseDTO> response = categoryService.getAllCategory();

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals("Electronics", response.get(0).getCategoryName());
	}

	// Fetching all categories for failure case
	@Test
	void testGetAllCategories_NoCategories() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(new ArrayList<>()));

		Exception exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.getAllCategory());
		assertEquals("No Category available", exception.getMessage());
	}

	//  Test: Categories are returned as an empty list 
	@Test
	void testGetAllCategories_EmptyList() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		Exception exception = assertThrows(CategoryNotFoundException.class,
				() -> categoryService.getAllCategory());

		assertEquals("No Category available", exception.getMessage());

		verify(categoryDAO, times(1)).getAllCategory();
	}

	//  Test: DTO to Entity conversion
	@Test
	void testDtoToEntityConversionAllCategories() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Electronics");

		ProductRequestDTO productRequestDTO = new ProductRequestDTO();
		productRequestDTO.setProductName("Laptop");
		productRequestDTO.setProductImage("laptop.jpg");
		productRequestDTO.setProductQuantity(10);
		productRequestDTO.setProductPrice(1000.0);
		productRequestDTO.setInStock(true);

		categoryRequestDTO.setProducts(Collections.singletonList(productRequestDTO));

		Category convertedCategory = DTOToEntity.categoryRequestDTOToCategory(categoryRequestDTO);

		assertNotNull(convertedCategory);
		assertEquals("Electronics", convertedCategory.getCategoryName());
		assertEquals(1, convertedCategory.getProducts().size());
	}

	//  Test: Entity to DTO conversion
	@Test
	void testEntityToDTOConversionAllCategories() {
		CategoryResponseDTO convertedDTO = EntityToDTO.toCategoryResponse(category);

		assertNotNull(convertedDTO);
		assertEquals("Electronics", convertedDTO.getCategoryName());
		assertEquals(1, convertedDTO.getProducts().size());
	}

	//  Fetching category by ID (Success)**
	@Test
	void testFindCategoryById_Success() {
		when(categoryDAO.getCategoryByCategoryId("C_01")).thenReturn(Optional.of(category));
		categoryResponse = categoryService.findCategoryByCategoryId("C_01");

		assertNotNull(categoryResponse);
		assertEquals("Electronics", categoryResponse.getCategoryName());
	}

	//  Fetching category by ID Failure
	@Test
	void testFindCategoryById_Failure() {
		when(categoryDAO.getCategoryByCategoryId("C_99")).thenReturn(Optional.empty());
		assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryByCategoryId("C_99"));
	}
	//  Test: Category ID does not exist 
	@Test
	void testFindCategoryByCategoryId_NotFound() {
		String categoryId = "C_99";
		when(categoryDAO.getCategoryByCategoryId(categoryId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class,
				() -> categoryService.findCategoryByCategoryId(categoryId));

		assertEquals("Category not exist with id : C_99", exception.getMessage());

		verify(categoryDAO, times(1)).getCategoryByCategoryId(categoryId);
	}

	//  Test: Category ID is null 
	@Test
	void testFindCategoryByCategoryId_NullId() {
		RuntimeException exception = assertThrows(RuntimeException.class, 
				() -> categoryService.findCategoryByCategoryId(null)
				);

		assertEquals("Category must not be null or blank", exception.getMessage());

		verify(categoryDAO, never()).getCategoryByCategoryId(anyString());
	}

	//  Test: Category ID is blank 
	@Test
	void testFindCategoryByCategoryId_BlankId() {
		String categoryId = "  ";  
		Exception exception = assertThrows(RuntimeException.class,
				() -> categoryService.findCategoryByCategoryId(categoryId));
		System.out.println("Actual exception message: " + exception.getMessage());  
		assertEquals("Category must not be null or blank", exception.getMessage());
		verify(categoryDAO, never()).getCategoryByCategoryId(anyString());
	}
	//  Fetching category by Name Success

	@Test
	void testFindCategoryByName_Success() {
		category = new Category();
		category.setCategoryId("C_01");
		category.setCategoryName("Electronics");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.singletonList(category)));

		List<Category> categoryList = Collections.singletonList(category);
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(categoryList));

		try (MockedStatic<StringUtils> mockedStatic = mockStatic(StringUtils.class)) {
			mockedStatic.when(() -> StringUtils.calculateSimilarCategoryCheck(anyString(), anyString()))
			.thenReturn(2);

			categoryResponse = categoryService.findCategoryByCategoryName("Electronics");

			assertNotNull(categoryResponse);
			assertEquals("Electronics", categoryResponse.getCategoryName());
		}
	}

	@Test
	void testFindCategoryByName_Failure_NoSimilarCategoryFound() {
		List<Category> categoryList = Collections.singletonList(category); // Only "Electronics" exists

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(categoryList));

		try (MockedStatic<StringUtils> mockedStatic = mockStatic(StringUtils.class)) {
			mockedStatic.when(() -> StringUtils.calculateSimilarCategoryCheck(anyString(), anyString()))
			.thenReturn(5); // No match (should trigger CategoryNotFoundException)

			assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryByCategoryName("Toys"));
		}
	}

	// Failure: Empty Category List
	@Test
	void testFindCategoryByName_Failure_EmptyCategoryList() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryByCategoryName("Electronics"));
	}

	// Failure: Null or Empty Input
	@Test
	void testFindCategoryByName_Failure_NullOrEmptyInput() {
		assertThrows(CategoryNotCreatedException.class, () -> categoryService.findCategoryByCategoryName(null));
		assertThrows(CategoryNotCreatedException.class, () -> categoryService.findCategoryByCategoryName("  "));
	}


	//  Test case for null checking
	@Test
	void testFindCategoryByCategoryName_NullName() {
		Exception exception = assertThrows(CategoryNotCreatedException.class, 
				() -> categoryService.findCategoryByCategoryName(null));

		assertEquals("Category name must not be null or empty", exception.getMessage());
		verify(categoryDAO, never()).getCategoryByCategoryName(anyString());
	}



	//  Test case for Blank checking
	@Test
	void testFindCategoryByCategoryName_BlankName() {
		String categoryName = "   ";  

		Exception exception = assertThrows(CategoryNotCreatedException.class, 
				() -> categoryService.findCategoryByCategoryName(categoryName));

		assertEquals("Category name must not be null or empty", exception.getMessage());

		verify(categoryDAO, never()).getAllCategory();
	}

	//  Update category with invalid ID
	@Test
	void testUpdateCategory_InvalidId() {
		assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(null, "Updated Name"));
	}

	//  Update category with invalid Name**
	@Test
	void testUpdateCategory_InvalidName() {
		assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory("C_01", ""));
	}

	//  Update category with short Name**
	@Test
	void testUpdateCategory_ShortName() {
		assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory("C_01", "AB"));
	}

	// DTO to Entity Conversion**
	@Test
	void testDtoToEntityConversion() {
		Category convertedCategory = DTOToEntity.categoryRequestDTOToCategory(categoryRequest);
		assertNotNull(convertedCategory);
		assertEquals("Electronics", convertedCategory.getCategoryName());
	}

	// Entity to DTO Conversion**
	@Test
	void testEntityToDTOConversion() {
		CategoryResponseDTO convertedDTO = EntityToDTO.toCategoryResponse(category);
		assertNotNull(convertedDTO);
		assertEquals("Electronics", convertedDTO.getCategoryName());
	}
	//  Null Check: Throws IllegalArgumentException when categoryId is null
	@Test
	void testUpdateCategory_NullCategoryId() {
		String categoryId = null;
		String categoryName = "Electronics";

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryId must not be null or empty", exception.getMessage());

		// Ensure DAO was never called
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}

	// Blank Check: Throws IllegalArgumentException when categoryId is blank
	@Test
	void testUpdateCategory_BlankCategoryId() {
		String categoryId = "   ";  // Blank spaces
		String categoryName = "Electronics";

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryId must not be null or empty", exception.getMessage());

		// Ensure DAO was never called
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}

	// Null Check: Throws IllegalArgumentException when categoryName is null
	@Test
	void testUpdateCategory_NullCategoryName() {
		String categoryId = "123";
		String categoryName = null;

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryName must not be null or empty", exception.getMessage());
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}

	// Blank Check: Throws IllegalArgumentException when categoryName is blank
	@Test
	void testUpdateCategory_BlankCategoryName() {
		String categoryId = "123";
		String categoryName = "   ";  

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryName must not be null or empty", exception.getMessage());
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}

	//  CategoryName = "Null" Check: Throws IllegalArgumentException
	@Test
	void testUpdateCategory_CategoryNameIsStringNull() {
		String categoryId = "123";
		String categoryName = "Null";  // String "Null"

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryName must not be null or empty", exception.getMessage());

		// Ensure DAO was never called
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}

	//  CategoryName Length Check: Less than 3 characters
	@Test
	void testUpdateCategory_CategoryNameTooShort() {
		String categoryId = "123";
		String categoryName = "AB";  

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.updateCategory(categoryId, categoryName));

		assertEquals("CategoryName must be at least 3 characters long", exception.getMessage());
		verify(categoryDAO, never()).updateCategory(anyString(), anyString());
	}   

}
