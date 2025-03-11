package com.srnrit.BMS.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.IProductService;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.idgenerator.ProductImageFileNameGenerator;

@Service
public class ProductServiceImpl implements IProductService {
    
    private final ProductDao productDao;
    private final FileStorageProperties fileStorageProperties;

    public ProductServiceImpl(ProductDao productDao, FileStorageProperties fileStorageProperties) {
        this.productDao = productDao;
        this.fileStorageProperties = fileStorageProperties;
    }

    /**
     * Store a new product along with an image file
     */
    @Override
    public ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO, MultipartFile productImage) {
        if (productRequestDTO == null) {
            throw new IllegalArgumentException("Invalid product data!");
        }

        Product product = DTOToEntity.toProduct(productRequestDTO);

        // Handle image saving if provided
        if (productImage != null && !productImage.isEmpty()) {
            String imagePath = saveImage(productImage);
            product.setProductImage(imagePath);
        }

        Optional<Product> productStored = this.productDao.saveProduct(product, productRequestDTO.getCategoryId());

        return EntityToDTO.toProductResponseDTO(
            productStored.orElseThrow(() -> new ProductNotCreatedException("Failed to save product."))
        );
    }

    /**
     * Save image file and return its stored path
     */
    private String saveImage(MultipartFile imageFile) {
        try {
            String storagePath = fileStorageProperties.getImageStoragePath();

            // Ensure the storage directory exists
            File directory = new File(storagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique filename with sequence (imageName_1.png)
            String uniqueFileName = ProductImageFileNameGenerator.getNewFileName(imageFile.getOriginalFilename());
            String filePath = storagePath + File.separator + uniqueFileName;

            // Save the file to the system
            File file = new File(filePath);
            imageFile.transferTo(file);

            return uniqueFileName; // Store only the generated file name in the DB
        } catch (IOException e) {
            throw new RuntimeException("Error saving image file: " + e.getMessage());
        }
    }

    /**
     * Fetch product by name
     */
    @Override
    public ProductResponseDTO getProductByProductName(String productName) {
        Optional<List<Product>> products = this.productDao.searchProductByName(productName);
        return products.flatMap(list -> list.stream().findFirst().map(EntityToDTO::toProductResponseDTO))
                       .orElseThrow(() -> new ProductNotFoundException("No product found with name: " + productName));
    }

    /**
     * Delete product by ID
     */
    @Override
    public String deleteProductByProductId(String productId) {
        Optional<String> result = this.productDao.deleteProductById(productId);
        return result.orElseThrow(() -> new ProductNotFoundException("Product not found for deletion with ID: " + productId));
    }

    /**
     * Update product by ID
     */
    @Override
    public ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId) {
        Product product = DTOToEntity.toProduct(productRequestDTO);
        product.setProductId(productId);

        Optional<Product> updatedProduct = this.productDao.updateProduct(product);
        return EntityToDTO.toProductResponseDTO(updatedProduct.orElseThrow(() -> new ProductNotFoundException("Failed to update product with ID: " + productId)));
    }

    /**
     * Fetch all products
     */
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        Optional<List<Product>> products = this.productDao.fetchAllProduct();
        return products.orElseThrow(() -> new RuntimeException("No products available!"))
                       .stream()
                       .map(EntityToDTO::toProductResponseDTO)
                       .collect(Collectors.toList());
    }

    /**
     * Fetch products by availability status
     */
    @Override
    public List<ProductResponseDTO> fetchProductByAvailability(Boolean inStock) {
        Optional<List<Product>> products = this.productDao.fetchProductByAvailability(inStock);
        return products.orElseThrow(() -> new RuntimeException("No products found with specified availability!"))
                       .stream()
                       .map(EntityToDTO::toProductResponseDTO)
                       .collect(Collectors.toList());
    }
}
