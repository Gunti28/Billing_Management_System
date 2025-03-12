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

	    
	    @Test
	    void testDeleteCategory_NotFound() {
	        when(categoryRepository.existsById("1")).thenReturn(false);

	        Optional<String> result = categoryDaoImpl.deleteCategory("1");

	        assertTrue(result.isEmpty());
	    }
	}
	    


