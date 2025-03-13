package com.srnrit.BMS.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.impl.CategoryDaoImpl;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryDAOTest 
{
	   @Mock
	    private CategoryRepository categoryRepository;

	    @InjectMocks
	    private CategoryDaoImpl categoryDaoImpl;

	    private Category category;

	    @BeforeEach
	    void setUp() {
	        category = new Category();
	        category.setCategoryId("1");
	        category.setCategoryName("Electronics");
	    }
	    
	    // Test for insertCategory
	    @Test
	    void testInsertCategory_Success() {
	        when(categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName())).thenReturn(null);
	        when(categoryRepository.save(category)).thenReturn(category);

	        Optional<Category> result = categoryDaoImpl.insertCategory(category);

	        assertTrue(result.isPresent());
	        assertEquals(category.getCategoryName(), result.get().getCategoryName());
	    }
	    
	    //Test for Category Already exist or not
	    @Test
	    void testInsertCategory_AlreadyExists() {
	        when(categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName())).thenReturn(category);

	        assertThrows(CategoryNotCreatedException.class, () -> categoryDaoImpl.insertCategory(category));
	    }

	    // Test for getAllCategory
	    @Test
	    void testGetAllCategory_Success() {
	        List<Category> categories = Arrays.asList(category);
	        when(categoryRepository.count()).thenReturn(1L);
	        when(categoryRepository.findAll()).thenReturn(categories);

	        Optional<List<Category>> result = categoryDaoImpl.getAllCategory();

	        assertTrue(result.isPresent());
	        assertEquals(1, result.get().size());
	    }

	    //test for category empty or not
	    @Test
	    void testGetAllCategory_Empty() {
	        when(categoryRepository.count()).thenReturn(0L);

	        Optional<List<Category>> result = categoryDaoImpl.getAllCategory();

	        assertTrue(result.isEmpty());
	    }

	    // Test for updateCategory
	    @Test
	    void testUpdateCategory_Success() {
	        when(categoryRepository.findById("1")).thenReturn(Optional.of(category));
	        when(categoryRepository.save(category)).thenReturn(category);

	        Optional<String> result = categoryDaoImpl.updateCategory("1", "Home Appliances");

	        assertTrue(result.isPresent());
	        assertEquals("Category updated successfully with id: 1", result.get());
	    }

	    //test for Updating category not found
	    @Test
	    void testUpdateCategory_NotFound() {
	        when(categoryRepository.findById("1")).thenReturn(Optional.empty());

	        assertThrows(CategoryNotFoundException.class, () -> categoryDaoImpl.updateCategory("1", "Home Appliances"));
	    }

	    // Test for getCategoryByCategoryId
	    @Test
	    void testGetCategoryByCategoryId_Success() {
	        when(categoryRepository.existsById("1")).thenReturn(true);
	        when(categoryRepository.findById("1")).thenReturn(Optional.of(category));

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryId("1");

	        assertTrue(result.isPresent());
	        assertEquals(category.getCategoryName(), result.get().getCategoryName());
	    }

	 // Test for getCategoryByCategoryId not found
	    @Test
	    void testGetCategoryByCategoryId_NotFound() {
	        when(categoryRepository.existsById("1")).thenReturn(false);

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryId("1");

	        assertTrue(result.isEmpty());
	    }

	    // Test for deleteCategory
	    @Test
	    void testDeleteCategory_Success() {
	        when(categoryRepository.existsById("1")).thenReturn(true);
	        doNothing().when(categoryRepository).deleteById("1");

	        Optional<String> result = categoryDaoImpl.deleteCategory("1");

	        assertTrue(result.isPresent());
	        assertEquals("categoryId deleted successfully", result.get());
	    }

	    
	    //Test for deleteCategory not found
	    @Test
	    void testDeleteCategory_NotFound() {
	        when(categoryRepository.existsById("1")).thenReturn(false);

	        Optional<String> result = categoryDaoImpl.deleteCategory("1");

	        assertTrue(result.isEmpty());
	    }
	    
	    
	    //Test for GetCategoryByCategoryName success
	    @Test
	    void testGetCategoryByCategoryName_Success() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("Electronics")).thenReturn(category);

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryName("Electronics");

	        assertTrue(result.isPresent());
	        assertEquals("Electronics", result.get().getCategoryName());
	    }

	  //Test for GetCategoryByCategoryName success not found
	    @Test
	    void testGetCategoryByCategoryName_NotFound() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("NonExistentCategory")).thenReturn(null);

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryName("NonExistentCategory");

	        assertTrue(result.isEmpty());
	    }

	    //Test for GetCategoryByCategoryName is null input
	    @Test
	    void testGetCategoryByCategoryName_NullInput() {
	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryName(null);

	        assertTrue(result.isEmpty(), "Expected empty Optional when category name is null");
	    }

	    //Test for GetCategoryByCategoryName is empty string
	    @Test
	    void testGetCategoryByCategoryName_EmptyString() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("")).thenReturn(null);

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryName("");

	        assertTrue(result.isEmpty(), "Expected empty Optional when category name is an empty string");
	    }

	    @Test
	    void testGetCategoryByCategoryName_WhitespaceString() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("   ")).thenReturn(null);

	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryName("   ");

	        assertTrue(result.isEmpty(), "Expected empty Optional when category name is only whitespace");
	    }

	    
	    @Test
	    void testInsertCategory_NullCategory() {
	        assertThrows(NullPointerException.class, () -> categoryDaoImpl.insertCategory(null));
	    }

	   

	    @Test
	    void testUpdateCategory_BlankName() {
	        when(categoryRepository.findById("1")).thenReturn(Optional.of(category));

	        Optional<String> result = categoryDaoImpl.updateCategory("1", "");

	        assertTrue(result.isPresent(), "Expected successful update even if name is blank");
	    }

	    
	    
	    @Test
	    void testGetCategoryByCategoryId_NullId() {
	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryId(null);

	        assertTrue(result.isEmpty(), "Expected empty Optional when ID is null");
	    }

	    @Test
	    void testGetCategoryByCategoryId_EmptyId() {
	        Optional<Category> result = categoryDaoImpl.getCategoryByCategoryId("");

	        assertTrue(result.isEmpty(), "Expected empty Optional when ID is empty");
	    }


	    @Test
	    void testDeleteCategory_NullId() {
	        Optional<String> result = categoryDaoImpl.deleteCategory(null);

	        assertTrue(result.isEmpty(), "Expected empty Optional when deleting a null ID");
	    }

	    @Test
	    void testDeleteCategory_EmptyId() {
	        Optional<String> result = categoryDaoImpl.deleteCategory("");

	        assertTrue(result.isEmpty(), "Expected empty Optional when deleting an empty ID");
	    }
	
	    
	    @Test
	    void testInsertCategory_Failure_CategoryAlreadyExists() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("Electronics")).thenReturn(category);

	        CategoryNotCreatedException exception = assertThrows(
	                CategoryNotCreatedException.class,
	                () -> categoryDaoImpl.insertCategory(category)
	        );

	        assertEquals("Category already available with name : Electronics", exception.getMessage());
	        verify(categoryRepository, never()).save(any());
	    }
	
	    
	    @Test
	    void testInsertCategory_Failure_SaveReturnsNull() {
	        when(categoryRepository.findByCategoryNameIgnoreCase("Electronics")).thenReturn(null);
	        when(categoryRepository.save(category)).thenReturn(null);

	        Optional<Category> result = categoryDaoImpl.insertCategory(category);

	        assertFalse(result.isPresent());
	        verify(categoryRepository, times(1)).save(category);
	    }
	
	    
	    @Test
	    void testInsertCategory_Failure_NullCategory() {
	        assertThrows(NullPointerException.class, () -> categoryDaoImpl.insertCategory(null));
	        verify(categoryRepository, never()).save(any());
	    }
	    
	    
	    @Test
	    void testInsertCategory_Failure_CategoryNameNull_Debug() {
	        try {
	            categoryDaoImpl.insertCategory(category);
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	        }
	    }
	    
	    
	 // Test Case: Category name is blank
	    @Test
	    void testInsertCategory_Failure_CategoryNameBlank() {
	        category.setCategoryName("");

	        when(categoryRepository.findByCategoryNameIgnoreCase("")).thenReturn(null);
	        when(categoryRepository.save(category)).thenReturn(category);

	        Optional<Category> result = categoryDaoImpl.insertCategory(category);

	        assertTrue(result.isPresent());
	        assertEquals("", result.get().getCategoryName());
	        verify(categoryRepository, times(1)).save(category);
	    }
	    
	    
	    @Test
	    void testGetAllCategory_EmptyDatabase() {
	        when(categoryRepository.count()).thenReturn(0L);

	        Optional<List<Category>> result = categoryDaoImpl.getAllCategory();

	        assertFalse(result.isPresent());
	        verify(categoryRepository, never()).findAll();
	    }
	    
	    
	    @Test
	    void testUpdateCategory_Failure_CategoryNotFound() {
	        when(categoryRepository.findById("999")).thenReturn(Optional.empty());

	        Exception exception = assertThrows(
	                CategoryNotFoundException.class,
	                () -> categoryDaoImpl.updateCategory("999", "Updated Category")
	        );

	        assertEquals("Category not found with id: 999", exception.getMessage());
	    
	    }
	    
	    
	    @Test
	    void testUpdateCategory_Failure_CategoryNameAlreadyExists_Debug() {
	        Category category = new Category();
	        category.setCategoryId("123");
	        category.setCategoryName("Old Category");

	        when(categoryRepository.findById("123")).thenReturn(Optional.of(category));
	        when(categoryRepository.existsByCategoryName("Electronics")).thenReturn(true);

	        try {
	            categoryDaoImpl.updateCategory("123", "Electronics");
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	        }
	    }
	    
	    
	    @Test
	    void testUpdateCategory_Failure_BlankCategoryName_Debug() {
	        Category category = new Category();
	        category.setCategoryId("123");
	        category.setCategoryName("Old Category");

	        when(categoryRepository.findById("123")).thenReturn(Optional.of(category));

	        try {
	            categoryDaoImpl.updateCategory("123", "");
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	        }
	    }
	    
	    
	    @Test
	    void testManualCheckForNullCategoryId() {
	        try {
	            categoryDaoImpl.updateCategory(null, "Updated Category");
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getMessage());
	        }
	    }

	    
	    @Test
	    void testUpdateCategory_Failure_NullCategoryName_Debug() {
	        try {
	            categoryDaoImpl.updateCategory("123", null);
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	        }
	    }
	    
	    
	    @Test
	    void testGetCategoryByCategoryName_BlankInput_Debug() {
	        try {
	            categoryDaoImpl.getCategoryByCategoryName("");
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getClass().getSimpleName() + " - " + e.getMessage());
	        }
	    }
	    
	    @Test
	    void testDeleteCategory_ManuallyTriggerException() {
	        try {
	            categoryDaoImpl.deleteCategory(null);
	        } catch (Exception e) {
	            System.out.println("Exception Thrown: " + e.getMessage());
	        }
	    }
	    
	    
	    @Test
	    void testDeleteCategory_BlankCategoryId() {
	        Optional<String> result = categoryDaoImpl.deleteCategory("");

	        assertFalse(result.isPresent()); // Ensure the response is empty
	    }
	    
}