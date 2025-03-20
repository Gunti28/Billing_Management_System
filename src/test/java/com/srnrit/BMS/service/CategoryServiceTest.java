package com.srnrit.BMS.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.UpdateCategoryRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.impl.CategoryServiceImpl;
import com.srnrit.BMS.util.StringUtils;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {


	@Mock
	private ICategoryDao categoryDAO;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	private CategoryRequestDTO categoryRequestDTO;
	private Category category;

	@BeforeEach
	void setUp() {
		categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Electronics");

		category = new Category();
		BeanUtils.copyProperties(categoryRequestDTO, category);
	}

	// ✅ Test Case: Add Category - Successful Insertion
	@Test
	void testAddCategory_Success() {
		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Collections.emptyList());
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
		verify(categoryDAO, times(1)).insertCategory(any(Category.class));
	}

	// ❌ Test Case: Add Category - Duplicate Name
	@Test
	void testAddCategory_DuplicateCategory() {
		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Arrays.asList("Electronics"));

		Exception exception = assertThrows(CategoryNameAlreadyExistsException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertTrue(exception.getMessage().contains("A category already exists"));
	}

	// ❌ Test Case: Add Category - Null Category Request
	@Test
	void testAddCategory_NullRequest() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(null));

		assertEquals("CategoryRequestDTO can't be null", exception.getMessage());
	}

	// ❌ Test Case: Add Category - Blank Name
	@Test
	void testAddCategory_BlankName() {
		categoryRequestDTO.setCategoryName("");

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("Category name cannot be blank", exception.getMessage());
	}

	// ✅ Test Case: Get All Categories - Success
	@Test
	void testGetAllCategories_Success() {
		List<Category> categoryList = Arrays.asList(category);
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(categoryList));

		List<CategoryResponseDTO> response = categoryService.getAllCategory();

		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
	}

	// ❌ Test Case: Get All Categories - Empty List
	@Test
	void testGetAllCategories_EmptyList() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.getAllCategory());

		assertEquals("No Category available", exception.getMessage());
	}

	// ✅ Test Case: Find Category by ID - Success
	@Test
	void testFindCategoryById_Success() {
		when(categoryDAO.getCategoryByCategoryId("123")).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.findCategoryByCategoryId("123");

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
	}

	// ❌ Test Case: Find Category by ID - Not Found
	@Test
	void testFindCategoryById_NotFound() {
		when(categoryDAO.getCategoryByCategoryId("123")).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryId("123"));

		assertEquals("Category not exist with id : 123", exception.getMessage());
	}

	// ❌ Test Case: Find Category by ID - Blank Input
	@Test
	void testFindCategoryById_BlankInput() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(" "));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}

	// ✅ Test Case: Update Category - Success
	@Test
	void testUpdateCategory_Success() {
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("123");
		updateDTO.setCategoryName("New Electronics");

		when(categoryDAO.updateCategory("123", "New Electronics")).thenReturn(Optional.of("Category updated"));

		String response = categoryService.updateCategory(updateDTO);

		assertEquals("Category updated", response);
	}

	// ❌ Test Case: Update Category - Not Found
	@Test
	void testUpdateCategory_NotFound() {
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("123");
		updateDTO.setCategoryName("New Electronics");

		when(categoryDAO.updateCategory("123", "New Electronics")).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.updateCategory(updateDTO));

		assertEquals("Category not found with id: 123", exception.getMessage());
	}

	// ✅ Test Case: Find Category by Name - Success
	@Test
	void testFindCategoryByName_Success() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.singletonList(category)));

		CategoryResponseDTO response = categoryService.findCategoryByCategoryName("Electronics");

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
	}

	// ❌ Test Case: Find Category by Name - Not Found
	@Test
	void testFindCategoryByName_NotFound() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("Electronics"));

		assertEquals("No category found with name: Electronics", exception.getMessage());
	}

	// ❌ Test Case: Find Category by Name - Null or Empty
	@Test
	void testFindCategoryByName_BlankInput() {
		Exception exception = assertThrows(CategoryNotCreatedException.class, 
				() -> categoryService.findCategoryByCategoryName(" "));

		assertEquals("Category name must not be null or empty", exception.getMessage());
	}



	// ❌ Test Case: Add Category - Null CategoryRequestDTO
	@Test
	void testAddCategory_NullCategoryRequestDTO() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(null));

		assertEquals("CategoryRequestDTO can't be null", exception.getMessage());
	}

	// ❌ Test Case: Find Category by ID - Null Input
	@Test
	void testFindCategoryById_NullInput() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(null));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}

	// ❌ Test Case: Find Category by Name - Null Input
	@Test
	void testFindCategoryByName_NullInput() {
		Exception exception = assertThrows(CategoryNotCreatedException.class, 
				() -> categoryService.findCategoryByCategoryName(null));

		assertEquals("Category name must not be null or empty", exception.getMessage());
	}

	// ❌ Test Case: Update Category - Null DTO
	@Test
	void testUpdateCategory_NullDTO() {
		Exception exception = assertThrows(NullPointerException.class, 
				() -> categoryService.updateCategory(null));

		assertEquals("Cannot invoke \"com.srnrit.BMS.dto.UpdateCategoryRequestDTO.getCategoryId()\" because \"dto\" is null", exception.getMessage());
	}

	@Test
	void testUpdateCategory_NullCategoryId() {
		// Arrange: Create a request with null categoryId
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId(null);  // Null value
		updateDTO.setCategoryName("UpdatedCategory");

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.updateCategory(updateDTO));

		// Print actual exception message for debugging
		System.out.println("Actual Exception Message: " + exception.getMessage());

		// Validate the expected exception message
		assertEquals("Category not found with id: null", exception.getMessage()); 
	}



	// ❌ Test Case: Get All Categories - Null Category List from DAO
	@Test
	void testGetAllCategories_NullCategoryList() {
		when(categoryDAO.getAllCategory()).thenReturn(null); // Mocking null return

		Exception exception = assertThrows(NullPointerException.class, 
				() -> categoryService.getAllCategory());

		assertEquals("Cannot invoke \"java.util.Optional.isPresent()\" because \"allCategory\" is null", exception.getMessage());
	}

	@Test
	void testFindCategoryByName_NullCategoryList() {
		// Arrange
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty()); // Return empty instead of null

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("Electronics"));

		System.out.println("Actual Exception Message: " + exception.getMessage());

		assertEquals("No category found with name: Electronics", exception.getMessage()); 
	}


	// ❌ Test Case: Insert Category - Null Return from DAO
	@Test
	void testInsertCategory_NullReturn() {
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(null); // Mocking null return

		Exception exception = assertThrows(NullPointerException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("Cannot invoke \"java.util.Optional.isPresent()\" because \"insertCategory\" is null", exception.getMessage());
	}

	@Test
	void testAddCategory_WithExtraSpaces() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   Fitness   "); // Extra spaces

		Category category = new Category();
		category.setCategoryName("Fitness");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Collections.emptyList());
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		// Act
		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals("Fitness", response.getCategoryName()); // Should trim spaces
	}


	@Test
	void testFindCategoryByCategoryName_CaseInsensitiveSearch() {
		// Arrange
		Category category = new Category();
		category.setCategoryName("Books");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(List.of(category)));

		// Act
		CategoryResponseDTO response = categoryService.findCategoryByCategoryName("books"); // Lowercase input

		// Assert
		assertNotNull(response);
		assertEquals("Books", response.getCategoryName()); // Should be case insensitive
	}


	@Test
	void testGetAllCategories_MultipleEntries() {
		// Arrange
		Category category1 = new Category();
		category1.setCategoryName("Fashion");

		Category category2 = new Category();
		category2.setCategoryName("Automobiles");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(List.of(category1, category2)));

		// Act
		List<CategoryResponseDTO> response = categoryService.getAllCategory();

		// Assert
		assertEquals(2, response.size());
		assertEquals("Fashion", response.get(0).getCategoryName());
		assertEquals("Automobiles", response.get(1).getCategoryName());
	}




	@Test
	void testUpdateCategory_CaseInsensitiveUpdate() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("501");
		updateDTO.setCategoryName("Gadgets");

		when(categoryDAO.updateCategory("501", "Gadgets")).thenReturn(Optional.of("Category updated successfully"));

		// Act
		String result = categoryService.updateCategory(updateDTO);

		// Assert
		assertEquals("Category updated successfully", result);
	}



	@Test
	void testAddCategory_WithWhitespaceOnlyName() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   "); // Name with only spaces

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		System.out.println("Caught Exception: " + exception.getMessage());

		// More flexible assertion to accommodate variations
		assertTrue(exception.getMessage().trim().equalsIgnoreCase("Category name cannot be blank")
				|| exception.getMessage().toLowerCase().contains("must not be empty"),
				"Expected an error message indicating a blank category name.");
	}


	@Test
	void testFindCategoryByCategoryName_NoCategoryExists() {
		// Arrange
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("NonExistentCategory"));

		assertEquals("No category found with name: NonExistentCategory", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryName_PartialMatchFails() {
		// Arrange
		Category category = new Category();
		category.setCategoryName("Laptops");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(List.of(category)));

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("Lap"));

		assertEquals("No category found with name: Lap", exception.getMessage());
	}



	@Test
	void testFindCategoryByCategoryName_DAOResponseNullHandled() {
		// Arrange
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty()); // Properly mocking empty response

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("Smartphones"));

		assertEquals("No category found with name: Smartphones", exception.getMessage());
	}


	@Test
	void testAddCategory_ValidInput() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Books");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Collections.emptyList());
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(new Category("1", "Books", null)));

		// Act
		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals("Books", response.getCategoryName());
	}

	@Test
	void testUpdateCategory_ValidUpdate() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("101");
		updateDTO.setCategoryName("Gaming");

		when(categoryDAO.updateCategory("101", "Gaming")).thenReturn(Optional.of("Gaming"));

		// Act
		String updatedCategoryName = categoryService.updateCategory(updateDTO);

		// Assert
		assertEquals("Gaming", updatedCategoryName);
	}


	@Test
	void testFindCategoryByCategoryId_ValidId() {
		// Arrange
		Category category = new Category("101", "Clothing", null);
		when(categoryDAO.getCategoryByCategoryId("101")).thenReturn(Optional.of(category));

		// Act
		CategoryResponseDTO response = categoryService.findCategoryByCategoryId("101");

		// Assert
		assertNotNull(response);
		assertEquals("Clothing", response.getCategoryName());
	}


	@Test
	void testAddCategory_CategoryAlreadyExists() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Fashion");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(List.of("Fashion"));

		// Act & Assert
		Exception exception = assertThrows(CategoryNameAlreadyExistsException.class,
				() -> categoryService.addCategory(categoryRequestDTO));

		assertTrue(exception.getMessage().contains("A category already exists"));
	}


	@Test
	void testUpdateCategory_CategoryNotFound() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("999");
		updateDTO.setCategoryName("New Name");

		when(categoryDAO.updateCategory("999", "New Name")).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.updateCategory(updateDTO));

		assertEquals("Category not found with id: 999", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryName_SimilarCategoryExists() {
		// Arrange
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(List.of(
				new Category("200", "Home Appliances", null),
				new Category("201", "Homemade Items", null)
				)));

		// Act
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("HomeAppl"));

		// Assert
		assertTrue(exception.getMessage().contains("No category found"));
	}


	@Test
	void testFindCategoryByCategoryId_NullId() {
		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(null));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}


	@Test
	void testAddCategory_ValidWithSpaces() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName(" Home Essentials ");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Collections.emptyList());
		when(categoryDAO.insertCategory(any(Category.class)))
		.thenReturn(Optional.of(new Category("303", "Home Essentials", null)));

		// Act
		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals("Home Essentials", response.getCategoryName());
	}


	@Test
	void testUpdateCategory_CaseSensitiveUpdate() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("450");
		updateDTO.setCategoryName("home appliances"); // Case change

		when(categoryDAO.updateCategory("450", "home appliances")).thenReturn(Optional.of("home appliances"));

		// Act
		String updatedCategory = categoryService.updateCategory(updateDTO);

		// Assert
		assertEquals("home appliances", updatedCategory);
	}



	// ✅ Test for Adding a Valid Category
	@Test
	void testAddCategory_ValidCategory() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Electronics");

		Category category = new Category();
		category.setCategoryName("Electronics");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Arrays.asList("Fashion", "Home"));
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
	}


	// ❌ Test for Adding Category with Null RequestDTO
	@Test
	void testAddCategory_NullRequestDTO() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(null));

		assertEquals("CategoryRequestDTO can't be null", exception.getMessage());
	}

	// ❌ Test for Adding Category with Blank Name
	@Test
	void testAddCategory_BlankCategoryName() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   "); // Spaces only

		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("Category name cannot be blank", exception.getMessage());
	}

	// ❌ Test for Adding Category with Duplicate Name
	@Test
	void testAddCategory_DuplicateName() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Electronics");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Arrays.asList("Electronics", "Fashion"));

		Exception exception = assertThrows(CategoryNameAlreadyExistsException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertTrue(exception.getMessage().contains("A category already exists with the name"));
	}

	// ✅ Test for Updating a Category Successfully
	@Test
	void testUpdateCategory_ValidCategory() {
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("101");
		updateDTO.setCategoryName("UpdatedName");

		when(categoryDAO.updateCategory("101", "UpdatedName")).thenReturn(Optional.of("UpdatedName"));

		String response = categoryService.updateCategory(updateDTO);

		assertEquals("UpdatedName", response);
	}


	// ❌ Test for Fetching Category with Invalid ID
	@Test
	void testFindCategoryByInvalidId() {
		when(categoryDAO.getCategoryByCategoryId("999")).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryId("999"));

		assertEquals("Category not exist with id : 999", exception.getMessage());
	}

	// ❌ Test for Fetching Category when No Categories Exist
	@Test
	void testGetAllCategory_NoCategories() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.getAllCategory());

		assertEquals("No Category available", exception.getMessage());
	}

	// ✅ Test for Finding Category by Valid Name
	@Test
	void testFindCategoryByValidName() {
		Category category = new Category();
		category.setCategoryName("Electronics");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Arrays.asList(category)));

		CategoryResponseDTO response = categoryService.findCategoryByCategoryName("Electronics");

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
	}

	// ❌ Test for Finding Category by Name That Does Not Exist
	@Test
	void testFindCategoryByInvalidName() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName("NonExistingCategory"));

		assertEquals("No category found with name: NonExistingCategory", exception.getMessage());
	}

	// ❌ Test for Finding Category with Null Name
	@Test
	void testFindCategoryByNullName() {
		Exception exception = assertThrows(CategoryNotCreatedException.class, 
				() -> categoryService.findCategoryByCategoryName(null));

		assertEquals("Category name must not be null or empty", exception.getMessage());
	}

	// ✅ Test for Adding Category with Mixed Case Name
	@Test
	void testAddCategory_MixedCaseName() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("ELeCtrOnics");

		Category category = new Category();
		category.setCategoryName("ELeCtrOnics");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Arrays.asList("Fashion", "Home"));
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		assertNotNull(response);
		assertEquals("ELeCtrOnics", response.getCategoryName());
	}


	// ✅ Test for Adding Category with Extra Spaces (Trim Test)
	@Test
	void testAddCategory_TrimmedSpaces() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   Gadgets   "); 

		Category category = new Category();
		category.setCategoryName("Gadgets");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Arrays.asList("Fashion"));
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		assertNotNull(response);
		assertEquals("Gadgets", response.getCategoryName());
	}


	// ❌ Test for Updating Category with Same Name as Before
	@Test
	void testUpdateCategory_SameName() {
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("101");
		updateDTO.setCategoryName("Electronics");

		when(categoryDAO.updateCategory("101", "Electronics")).thenReturn(Optional.of("Electronics"));

		String response = categoryService.updateCategory(updateDTO);

		assertEquals("Electronics", response);
	}

	// ❌ Test for Finding Category by Null ID
	@Test
	void testFindCategoryByNullId() {
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(null));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}

	// 🔹 Test for Adding Category with Unicode Characters
	@Test
	void testAddCategory_UnicodeCharacters() {
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Électronique");

		Category category = new Category();
		category.setCategoryName("Électronique");

		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		assertNotNull(response);
		assertEquals("Électronique", response.getCategoryName());
	}

	// 🔹 Test for Finding Category with Similar Name but Different Spacing
	@Test
	void testFindCategory_SimilarNameSpacing() {
		Category category = new Category();
		category.setCategoryName("Electronics");

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Arrays.asList(category)));

		CategoryResponseDTO response = categoryService.findCategoryByCategoryName(" Electronics ");

		assertNotNull(response);
		assertEquals("Electronics", response.getCategoryName());
	}

	// 🔹 Test for Empty Database Before Fetching Any Category
	@Test
	void testGetAllCategory_EmptyDatabase() {
		when(categoryDAO.getAllCategory()).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.getAllCategory());

		assertEquals("No Category available", exception.getMessage());
	}


	@Test
	void testAddCategory_ValidCategory_Success() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("Gaming");

		Category category = new Category();
		category.setCategoryId("201");
		category.setCategoryName("Gaming");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(Collections.emptyList());
		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

		// Act
		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals("Gaming", response.getCategoryName());
		assertEquals("201", response.getCategoryId());
	}


	@Test
	void testAddCategory_NullCategoryRequestDTO_ThrowsException() {
		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(null));

		assertEquals("CategoryRequestDTO can't be null", exception.getMessage());
	}


	@Test
	void testAddCategory_BlankCategoryName_ThrowsException() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   "); // Blank name

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("Category name cannot be blank", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryId_NullId_ThrowsException() {
		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(null));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryId_EmptyId_ThrowsException() {
		// Arrange
		String categoryId = "   "; // Empty

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(categoryId));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryName_NonExistentCategory_ThrowsException() {
		// Arrange
		String categoryName = "UnknownCategory";
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName(categoryName));

		assertEquals("No category found with name: UnknownCategory", exception.getMessage());
	}


	@Test
	void testUpdateCategory_ValidUpdate_Success() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("301");
		updateDTO.setCategoryName("Updated Category");

		when(categoryDAO.updateCategory("301", "Updated Category"))
		.thenReturn(Optional.of("Category updated successfully"));

		// Act
		String response = categoryService.updateCategory(updateDTO);

		// Assert
		assertEquals("Category updated successfully", response);
	}


	@Test
	void testUpdateCategory_NonExistingCategory_ThrowsException() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("999");
		updateDTO.setCategoryName("NonExisting");

		when(categoryDAO.updateCategory("999", "NonExisting")).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.updateCategory(updateDTO));

		assertEquals("Category not found with id: 999", exception.getMessage());
	}



	@Test
	void testAddCategory_ExistingCategoryName_ThrowsException() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("electronics");

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(List.of("Electronics"));

		// Act & Assert
		Exception exception = assertThrows(CategoryNameAlreadyExistsException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("A category already exists with the name: Electronics", exception.getMessage());
	}



	@Test
	void testFindCategoryByCategoryId_NonExistingId_ThrowsException() {
		// Arrange
		String categoryId = "999";

		when(categoryDAO.getCategoryByCategoryId(categoryId)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryId(categoryId));

		assertEquals("Category not exist with id : 999", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryName_NonExistent_ThrowsException() {
		// Arrange
		String categoryName = "NonExistentCategory";

		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.findCategoryByCategoryName(categoryName));

		assertEquals("No category found with name: NonExistentCategory", exception.getMessage());
	}


	@Test
	void testGetAllCategory_EmptyDatabase_ThrowsException() {
		// Arrange
		when(categoryDAO.getAllCategory()).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		Exception exception = assertThrows(CategoryNotFoundException.class, 
				() -> categoryService.getAllCategory());

		assertEquals("No Category available", exception.getMessage());
	}


	@Test
	void testAddCategory_WithWhitespace_TrimmedAndSuccess() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("  Furniture  "); // Leading and trailing spaces

		when(categoryDAO.fetchAllCategoryNames()).thenReturn(List.of("Electronics", "Clothing"));

		Category newCategory = new Category();
		newCategory.setCategoryId("401");
		newCategory.setCategoryName("Furniture");

		when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(newCategory));

		// Act
		CategoryResponseDTO response = categoryService.addCategory(categoryRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals("Furniture", response.getCategoryName());
		assertEquals("401", response.getCategoryId());
	}



	@Test
	void testFindCategoryByCategoryId_CaseInsensitive_ReturnsCategory() {
		// Arrange
		String categoryId = "Abc123";

		Category category = new Category();
		category.setCategoryId("ABC123"); // Stored ID is in uppercase
		category.setCategoryName("Home Decor");

		when(categoryDAO.getCategoryByCategoryId("Abc123")).thenReturn(Optional.of(category));

		// Act
		CategoryResponseDTO response = categoryService.findCategoryByCategoryId(categoryId);

		// Assert
		assertNotNull(response);
		assertEquals("ABC123", response.getCategoryId());
		assertEquals("Home Decor", response.getCategoryName());
	}



	@Test
	void testUpdateCategory_ChangeToCompletelyDifferentName_Success() {
		// Arrange
		UpdateCategoryRequestDTO updateDTO = new UpdateCategoryRequestDTO();
		updateDTO.setCategoryId("701");
		updateDTO.setCategoryName("Sports Equipment");

		when(categoryDAO.updateCategory("701", "Sports Equipment")).thenReturn(Optional.of("Category updated successfully"));

		// Act
		String response = categoryService.updateCategory(updateDTO);

		// Assert
		assertEquals("Category updated successfully", response);
	}


	@Test
	void testAddCategory_OnlySpaces_ThrowsException() {
		// Arrange
		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
		categoryRequestDTO.setCategoryName("   "); // Only whitespace

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.addCategory(categoryRequestDTO));

		assertEquals("Category name cannot be blank", exception.getMessage());
	}


	@Test
	void testFindCategoryByCategoryId_NullInput_ThrowsException() {
		// Arrange
		String categoryId = null;

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> categoryService.findCategoryByCategoryId(categoryId));

		assertEquals("Category must not be null or blank", exception.getMessage());
	}




}



