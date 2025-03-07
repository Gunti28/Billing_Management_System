package com.srnrit.BMS.mapper;

import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;
import java.util.List;
import java.util.stream.Collectors;

import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;

public class EntityToDTO {
	
	
	public static ProductResponseDTO toProductResponseDTO(Product product) {
		return new ProductResponseDTO(
				product.getProductId(),
				product.getProductName(),
				product.getProductImage(),
				product.getProductQuantity(),
				product.getProductPrice(),
				product.getInStock()
				);
	}





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
