package com.srnrit.BMS.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor  
public class ProductRequestDTO {

    @NotNull(message = "Category ID cannot be null")
    @NotBlank(message = "Category ID cannot be blank")
    private String categoryId;

    @NotNull(message = "Product name cannot be null")
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 50, message = "Product name cannot be more than 50 characters")
    private String productName;

    
    private String productImage;  // Stores the file path instead of MultipartFile

    @NotNull(message = "Product quantity cannot be null")
    @Min(value = 0, message = "Product quantity cannot be negative")
    private Integer productQuantity;

    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    private Double productPrice;

    @NotNull(message = "Stock availability must be specified")
    private Boolean inStock;

    // Manually defined constructor to handle MultipartFile
    public ProductRequestDTO(String categoryId, String productName, MultipartFile productImage,
                             Integer productQuantity, Double productPrice, Boolean inStock) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.inStock = inStock;

        // Convert MultipartFile to a file path (assuming you save images in a directory)
        this.productImage = (productImage != null) ? productImage.getOriginalFilename() : null;
    }
}
