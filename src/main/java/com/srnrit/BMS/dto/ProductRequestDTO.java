package com.srnrit.BMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequestDTO {

	private String categoryId;
	private String productName;
	private String productImage;
	private Integer productQuantity;
	private Double productPrice;
	private Boolean inStock;

}
