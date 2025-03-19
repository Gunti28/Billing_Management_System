package com.srnrit.BMS.mapper;

import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.entity.User;

import java.util.List;
import java.util.stream.Collectors;

import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;


public class EntityToDTO {
	
	public static UserResponseDTO userEntityToUserResponseDTO(User user)
	{
		UserResponseDTO dto = new UserResponseDTO();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}


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


	public static CategoryResponseDTO toCategoryResponse(Category category) 
	{
		if (category == null) {
			return null; 
		}

		List<ProductResponseDTO> categoryResponses = null;

		if(category.getProducts()!=null && !category.getProducts().isEmpty()) 
		{ 
			categoryResponses= category
					.getProducts()
					.stream()
					.map(EntityToDTO :: toProductResponseDTO) 
					.collect(Collectors.toList());
		}
		return new CategoryResponseDTO(category.getCategoryId(),category.getCategoryName(),categoryResponses); 
	} 


}
