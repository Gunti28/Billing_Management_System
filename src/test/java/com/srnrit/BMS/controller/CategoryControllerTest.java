package com.srnrit.BMS.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.UpdateCategoryRequestDTO;
import com.srnrit.BMS.service.ICategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
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

    // Positive Test: Add Category
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

    // Negative Test: Add Category with Invalid Data
    @Test
    void testAddCategory_Failure_InvalidData() throws Exception {
        categoryRequestDTO.setCategoryName(""); // Invalid name

        mockMvc.perform(post("/category/addCategoryWithProducts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isBadRequest());
    }
    

    // Positive Test: Get All Categories
    @Test
    void testGetAllCategories_Success() throws Exception {
        when(categoryService.getAllCategory()).thenReturn(List.of(categoryResponseDTO));

        mockMvc.perform(get("/category/allCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));
    }

    // Negative Test: Get All Categories - No Data Found
    @Test
    void testGetAllCategories_Failure_NoData() throws Exception {
        when(categoryService.getAllCategory()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/category/allCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // Positive Test: Get Category by ID
    @Test
    void testFindCategoryById_Success() throws Exception {
        when(categoryService.findCategoryByCategoryId("123")).thenReturn(categoryResponseDTO);

        mockMvc.perform(get("/category/categoryById").param("categoryId", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    // Negative Test: Get Category by ID - Not Found
    @Test
    void testFindCategoryById_Failure_NotFound() throws Exception {
        when(categoryService.findCategoryByCategoryId("999")).thenReturn(null);

        mockMvc.perform(get("/category/categoryById").param("categoryId", "999"))
                .andExpect(status().isNotFound());
    }

    // Positive Test: Update Category
    @Test
    void testUpdateCategory_Success() throws Exception {
        UpdateCategoryRequestDTO updateRequest = new UpdateCategoryRequestDTO();
        updateRequest.setCategoryId("123");
        updateRequest.setCategoryName("UpdatedName");

        when(categoryService.updateCategory("123", "UpdatedName"))
                .thenReturn("Category updated successfully");

        mockMvc.perform(put("/category/updateCategoryById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Category updated successfully"));
    }

    // Negative Test: Update Category - Invalid Data
    @Test
    void testUpdateCategory_Failure_InvalidData() throws Exception {
        UpdateCategoryRequestDTO updateRequest = new UpdateCategoryRequestDTO();
        updateRequest.setCategoryId("123");
        updateRequest.setCategoryName(""); // Invalid name

        mockMvc.perform(put("/category/updateCategoryById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }
}
