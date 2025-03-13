package com.srnrit.BMS.service;

import java.util.List;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;

public interface ICategoryService 
{
	CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO);
	List<CategoryResponseDTO> getAllCategory();
	String updateCategory(String categoryId,String categoryName);
	CategoryResponseDTO findCategoryByCategoryId(String categoryId);
	CategoryResponseDTO findCategoryByCategoryName(String categoryName);
}
