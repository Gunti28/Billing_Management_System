package com.srnrit.BMS.service;

import java.util.List;

import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;

public interface IProductService {
	
	ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO); // adding product by cat
	
	ProductResponseDTO getProductByProductName(String productName);
	
	String deleteProductByProductId(String productId);

	ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId);
	
	
	
	// fetching product based on availability
	ProductResponseDTO fetchProductByAvailability(ProductRequestDTO productRequestDTO);
	
	
	List<ProductResponseDTO> getAllProducts();
	
	
	

}
