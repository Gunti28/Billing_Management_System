package com.srnrit.BMS.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;

public class EntityToDTO 
{
	/*
	 * public static CategoryResponseDTO toCategoryResponse(Category category) {
	 * if(category.getProducts()!=null) { List<ProductResponseDTO> categoryResponses
	 * = category .getProducts() .stream()
	 * .map("EntityToDTO :: toProductResponseDTO") .collect(Collectors.toList());
	 * return new CategoryResponseDTO(category.getCategoryId(),
	 * category.getCategoryname(),categoryResponses); } return new
	 * CategoryResponseDTO(category.getCategoryId(),
	 * category.getCategoryname(),null); }
	 */
}
