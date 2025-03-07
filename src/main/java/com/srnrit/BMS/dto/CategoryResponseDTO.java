package com.srnrit.BMS.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponseDTO 
{
    private String categoryId;
    private String categoryName;
    
    private List<ProductResponseDTO> products;
}
