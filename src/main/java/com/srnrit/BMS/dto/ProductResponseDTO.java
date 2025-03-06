package com.srnrit.BMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

	private String productId;
	private String productName;
	private String productImage;
	private Integer productQuantity;
	private Double productPrice;
	private Boolean inStock;
}
