package com.srnrit.BMS.dto;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor
public class ProductRequestDTO {

   

    @NotNull(message = "Product name cannot be null")
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 50, message = "Product name cannot be more than 50 characters")
    private String productName;

     @NotNull(message = "Product quantity cannot be null")
    @Min(value = 0, message = "Product quantity cannot be negative")
    private Integer productQuantity;

    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    private Double productPrice;

    @NotNull(message = "Stock availability must be specified")
    private Boolean inStock;
    
    
    
}
