package com.srnrit.BMS.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;
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


	public static CategoryResponseDTO toCategoryResponse(Category category) 
	{
		if (category == null) 
		   throw new RuntimeException("Category must not be null");
		
		
		CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        BeanUtils.copyProperties(category, categoryResponseDTO);
		
		List<Product> products = category.getProducts();
		List<ProductResponseDTO> productResponseDTOs=null;
		if(products!=null && products.size()>0)
		{
			productResponseDTOs=products.stream().map(product-> EntityToDTO.toProductResponseDTO(product)).collect(Collectors.toList());
			categoryResponseDTO.setProducts(productResponseDTOs);
		}
		return categoryResponseDTO;
	} 


}
