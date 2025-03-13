package com.srnrit.BMS.mapper;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;

public class DTOToEntity 
{

	public static Category categoryRequestDTOToCategory(CategoryRequestDTO dto)
	{
		Category category = new Category();
		category.setCategoryName(dto.getCategoryName());

		if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
			List<Product> products = dto.getProducts().stream()
					.map(DTOToEntity::toProduct)
					.collect(Collectors.toList());
			category.setProducts(products);
		} else {
			category.setProducts(new ArrayList<>()); 
		}
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
