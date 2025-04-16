package com.srnrit.BMS.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest  {

    @Mock
    private ICategoryDao categoryDAO;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Category Entity
        category = new Category();
        category.setCategoryName("Electronics");
        category.setProducts(new ArrayList<>());  

        // Mock Request DTO
        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setCategoryName("Electronics");

        // Mock Response DTO
        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryName("Electronics");
    }
    


 //  Test for Adding a Category Successfully
    @Test
    void testAddCategoryWithProducts_Success() {
        // Create a mock category object
        Category category = new Category();
        category.setCategoryName("Electronics");

        when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.of(category));

        //  Manually create the expected response instead of relying on EntityToDTO
        CategoryResponseDTO expectedResponse = new CategoryResponseDTO();
        expectedResponse.setCategoryName("Electronics");  

        CategoryResponseDTO response = categoryService.addCategoryWithProducts(categoryRequestDTO);

        assertNotNull(response);
        assertEquals(expectedResponse.getCategoryName(), response.getCategoryName());
    }


    //  Test for Adding a Category Failure
    @Test
    void testAddCategoryWithProducts_Failure() {
        when(categoryDAO.insertCategory(any(Category.class))).thenReturn(Optional.empty());

        assertThrows(CategoryNotCreatedException.class, () -> categoryService.addCategoryWithProducts(categoryRequestDTO));
    }

 // Test for Fetching All Categories (Success)
    @Test
    void testGetAllCategory_Success() {
        List<Category> categoryList = Arrays.asList(category);
        when(categoryDAO.getAllCategory()).thenReturn(Optional.of(categoryList));

        // ✅ Manually create expected response instead of relying on EntityToDTO
        CategoryResponseDTO expectedResponse = new CategoryResponseDTO();
        expectedResponse.setCategoryName(category.getCategoryName()); 

        List<CategoryResponseDTO> response = categoryService.getAllCategory();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(expectedResponse.getCategoryName(), response.get(0).getCategoryName());
    }


    //  Test for Fetching All Categories (Failure)
    @Test
    void testGetAllCategory_Failure() {
        when(categoryDAO.getAllCategory()).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getAllCategory());
    }

    //  Test for Fetching Category by ID (Success)
    @Test
    void testFindCategoryByCategoryId_Success() {
        when(categoryDAO.getCategoryByCategoryId("123")).thenReturn(Optional.of(category));

        // Convert manually instead of mocking
        CategoryResponseDTO expectedResponse = EntityToDTO.toCategoryResponse(category);

        CategoryResponseDTO response = categoryService.findCategoryByCategoryId("123");

        assertNotNull(response);
        assertEquals(expectedResponse.getCategoryName(), response.getCategoryName());
    }


    // Test for Updating Category (Success)
    @Test
    void testUpdateCategory_Success() {
        when(categoryDAO.updateCategory(anyString(), anyString())).thenReturn(Optional.of("Updated Successfully"));

        String response = categoryService.updateCategory("123", "Updated Category");

        assertEquals("Updated Successfully", response);
    }

    // Test for Updating Category (Failure - Category Not Found)
    @Test
    void testUpdateCategory_Failure() {
        when(categoryDAO.updateCategory(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory("123", "Updated Category"));
    }

    

    // Test for Fetching Category by ID (Failure)
    @Test
    void testFindCategoryByCategoryId_Failure() {
        when(categoryDAO.getCategoryByCategoryId("123")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryByCategoryId("123"));
    }

    // Test for Updating Category with Null ID
    @Test
    void testUpdateCategory_NullId() {
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(null, "Updated Category"));
    }

    // Test for Updating Category with Invalid Name
    @Test
    void testUpdateCategory_InvalidName() {
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory("123", ""));
    }
}



