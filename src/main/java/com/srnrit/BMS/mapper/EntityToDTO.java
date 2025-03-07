package com.srnrit.BMS.mapper;

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

}
