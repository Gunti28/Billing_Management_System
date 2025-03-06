package com.srnrit.BMS.service;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;

public interface ICategoryService 
{
  CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO);
}
