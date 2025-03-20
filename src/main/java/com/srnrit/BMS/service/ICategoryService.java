package com.srnrit.BMS.service;

import java.util.List;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.UpdateCategoryRequestDTO;

public interface ICategoryService 
{
	CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);
	List<CategoryResponseDTO> getAllCategory();
	String updateCategory(UpdateCategoryRequestDTO dto);
	CategoryResponseDTO findCategoryByCategoryId(String categoryId);
	CategoryResponseDTO findCategoryByCategoryName(String categoryName);
}
