package com.srnrit.BMS.mapper;


import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Product;

public class DTOToEntity 
{
	public static Category categoryRequestDTOToCategory(CategoryRequestDTO dto)
	{
		Category category = new Category();
		BeanUtils.copyProperties(dto, category);		
		System.out.println(category);
		return category;		
	}

	public static Product toProduct(ProductRequestDTO productRequestDTO) {
		return new Product(
				productRequestDTO.getProductName(),
				productRequestDTO.getProductImage(),
				productRequestDTO.getProductQuantity(),
				productRequestDTO.getProductPrice(),
				productRequestDTO.getInStock()
				);
	}
	

}
