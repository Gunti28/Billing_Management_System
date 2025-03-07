package com.srnrit.BMS.mapper;

import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Product;

public class DTOToEntity {
	
	
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
