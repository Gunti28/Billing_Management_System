package com.srnrit.BMS.service;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;

import java.util.List;


public interface ICategoryService 
{
  CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);
  List<CategoryResponseDTO> getAllCategory();
}
