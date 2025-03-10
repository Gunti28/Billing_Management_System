package com.srnrit.BMS.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.service.ICategoryService;

@WebMvcTest(CategoryController.class) // Loads only CategoryController for testing
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setCategoryName("Electronics");

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryName("Electronics");
    }

    //  Test for Adding a Category (Success)
    @Test
    void testAddCategory_Success() throws Exception {
        when(categoryService.addCategoryWithProducts(any(CategoryRequestDTO.class)))
                .thenReturn(categoryResponseDTO);

        mockMvc.perform(post("/category/addCategoryWithProducts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(categoryService, times(1)).addCategoryWithProducts(any(CategoryRequestDTO.class));
    }

    //  Test for Getting All Categories (Success)
    @Test
    void testGetAllCategories_Success() throws Exception {
        List<CategoryResponseDTO> categories = Arrays.asList(categoryResponseDTO);

        when(categoryService.getAllCategory()).thenReturn(categories);

        mockMvc.perform(get("/category/allCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));

        verify(categoryService, times(1)).getAllCategory();
    }

    // Test for Getting Category by ID (Success)
    @Test
    void testFindCategoryById_Success() throws Exception {
        when(categoryService.findCategoryByCategoryId("123")).thenReturn(categoryResponseDTO);

        mockMvc.perform(get("/category/categoryById/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        verify(categoryService, times(1)).findCategoryByCategoryId("123");
    }

    // Test for Updating a Category (Success)
    @Test
    void testUpdateCategory_Success() throws Exception {
        when(categoryService.updateCategory("123", "UpdatedName")).thenReturn("Category updated successfully");

        mockMvc.perform(put("/category/updateCategory/123/UpdatedName"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category updated successfully"));

        verify(categoryService, times(1)).updateCategory("123", "UpdatedName");
    }

}