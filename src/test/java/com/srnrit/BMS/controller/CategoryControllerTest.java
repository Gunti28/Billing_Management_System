package com.srnrit.BMS.controller;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    // Test case for adding a category
    @Test
    void testAddCategory_Success() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Electronics", null);
        CategoryResponseDTO responseDTO = new CategoryResponseDTO("1", "Electronics", null);

        when(categoryService.addCategoryWithProducts(any(CategoryRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/category/addCategoryWithProducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\": \"Electronics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value("1"))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    // Test case for fetching all categories
    @Test
    void testGetAllCategories_Success() throws Exception {
        List<CategoryResponseDTO> categories = Arrays.asList(
                new CategoryResponseDTO("1", "Electronics", null),
                new CategoryResponseDTO("2", "Clothing", null)
        );

        when(categoryService.getAllCategory()).thenReturn(categories);

        mockMvc.perform(get("/category/allCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(jsonPath("$[1].categoryName").value("Clothing"));
    }

    // Test case for updating a category
    @Test
    void testUpdateCategory_Success() throws Exception {
        when(categoryService.updateCategory(eq("1"), eq("Updated Electronics"))).thenReturn("Category Updated Successfully");

        mockMvc.perform(put("/category/1/Updated Electronics"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category Updated Successfully"));
    }

    // Test case for fetching a category by ID
    @Test
    void testFindCategoryById_Success() throws Exception {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO("1", "Electronics", null);

        when(categoryService.findCategoryByCategoryId("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value("1"))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    // Test case for category not found
    @Test
    void testFindCategoryById_NotFound() throws Exception {
        when(categoryService.findCategoryByCategoryId("99")).thenThrow(new RuntimeException("Category not found"));

        mockMvc.perform(get("/category/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Category not found"));
    }
}
