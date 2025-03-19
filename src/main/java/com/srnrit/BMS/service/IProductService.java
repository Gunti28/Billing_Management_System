package com.srnrit.BMS.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;

public interface IProductService {
    
    // Adding product by category (supports image upload)
    ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO, MultipartFile productImage);
    
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
