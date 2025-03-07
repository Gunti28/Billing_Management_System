package com.srnrit.BMS.service;

import java.util.List;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;

public interface IProductService {
    
    // Adding product by category
    ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO);
    
    // Fetching product by name
    ProductResponseDTO getProductByProductName(String productName);
    
    // Deleting product by product ID
    String deleteProductByProductId(String productId);

    // Updating product by product ID
    ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId);
    
    // Fetching products based on availability
    List<ProductResponseDTO> fetchProductByAvailability(Boolean inStock);

    // Fetch all products
    List<ProductResponseDTO> getAllProducts();
}
