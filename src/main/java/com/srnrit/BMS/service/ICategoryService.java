package com.srnrit.BMS.service;

import java.util.List;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;

import java.util.List;


public interface ICategoryService 
{
<<<<<<< HEAD
  CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);
  List<CategoryResponseDTO> getAllCategory();
=======
  CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO);
  List<CategoryResponseDTO> getAllCategory();
  String updateCategory(String categoryId,String categoryName);
  CategoryResponseDTO findCategoryByCategoryId(String categoryId);
>>>>>>> e4970a01a95a2156505783fe73e01ebec456f952
}
