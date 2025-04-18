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

import java.util.Arrays;
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
        UpdateCategoryRequestDTO categoryRequestDTO = new UpdateCategoryRequestDTO();
        categoryRequestDTO.setCategoryId("123");
        categoryRequestDTO.setCategoryName("UpdatedName");

        when(categoryService.updateCategory("123", "UpdatedName")).thenReturn("Category updated successfully");
        mockMvc.perform(put("/category/updateCategoryById")
                .contentType(MediaType.APPLICATION_JSON)  
                .content(asJsonString(categoryRequestDTO))) 
                .andExpect(status().isOk())
                .andExpect(content().string("Category updated successfully"));

        verify(categoryService, times(1)).updateCategory("123", "UpdatedName");
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}