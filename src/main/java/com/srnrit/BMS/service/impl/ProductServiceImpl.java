package com.srnrit.BMS.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {
    
    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Storing a new product by category
    @Override
    public ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO != null) {
            Product product = DTOToEntity.toProduct(productRequestDTO);
            Optional<Product> productStored = this.productDao.saveProduct(product, productRequestDTO.getCategoryId());

            return EntityToDTO.toProductResponseDTO(
                productStored.orElseThrow(() -> new ProductNotCreatedException("Product not created"))
            );
        } else {
            throw new RuntimeException("Invalid JSON!");
        }
    }

    // Fetch a product by name
    @Override
    public ProductResponseDTO getProductByProductName(String productName) {
        Optional<List<Product>> products = this.productDao.searchProductByName(productName);
        if (products.isPresent() && !products.get().isEmpty()) {
            return EntityToDTO.toProductResponseDTO(products.get().get(0)); // Return first match
        } else {
            throw new RuntimeException("Product not found!");
        }
    }

    // Delete a product by ID
    @Override
    public String deleteProductByProductId(String productId) {
        Optional<String> result = this.productDao.deleteProductById(productId);
        return result.orElseThrow(() -> new RuntimeException("Product not found for deletion"));
    }

    // Update a product by ID
    @Override
    public ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId) {
        Product product = DTOToEntity.toProduct(productRequestDTO);
        product.setProductId(productId); // Ensure correct ID is updated

        Optional<Product> updatedProduct = this.productDao.updateProduct(product);
        return EntityToDTO.toProductResponseDTO(updatedProduct.orElseThrow(() -> new RuntimeException("Product update failed!")));
    }

    // Fetch all products from the database
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        Optional<List<Product>> products = this.productDao.fetchAllProduct();
        return products.orElseThrow(() -> new RuntimeException("No products found!"))
                .stream()
                .map(EntityToDTO::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    // Fetch products based on availability
    @Override
    public List<ProductResponseDTO> fetchProductByAvailability(Boolean inStock) {
        Optional<List<Product>> products = this.productDao.fetchProductByAvailability(inStock);
        return products.orElseThrow(() -> new RuntimeException("No products found with the given availability!"))
                .stream()
                .map(EntityToDTO::toProductResponseDTO)
                .collect(Collectors.toList());
    }
    
}
