package com.srnrit.BMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private ICategoryDao categoryDAO;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;

    @BeforeEach
    void setUp() {
        category = new Category("1", "Electronics", null);
        categoryRequestDTO = new CategoryRequestDTO("Electronics", null);
    }

    // Test case for updating category name successfully
    @Test
    void testUpdateCategory_NotFound() {
        when(categoryDAO.updateCategory("99", "Unknown")).thenReturn(Optional.empty());

        Exception exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory("99", "Unknown");
        });

        assertEquals("Category not exist with id : 99", exception.getMessage());
    }


//    //  Test case when category is not found
//    @Test
//    void testUpdateCategory_NotFound() {
//        when(categoryDAO.updateCategory("99", "Unknown")).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(CategoryNotFoundException.class, () -> {
//            categoryService.updateCategory("99", "Unknown");
//        });
//
//        assertEquals("Category not exist with id: 99", exception.getMessage());
//    }
}
