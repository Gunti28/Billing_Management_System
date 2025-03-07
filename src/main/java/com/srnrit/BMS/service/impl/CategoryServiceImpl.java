package com.srnrit.BMS.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService 
{
	private ICategoryService iCategoryService;

	public CategoryServiceImpl(ICategoryService iCategoryService) {
		super();
		this.iCategoryService = iCategoryService;
	}

	@Override
	public CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO) {
		Category category=new Category();
		category.setCategoryname(categoryRequestDTO.getCategoryName());

		if(categoryRequestDTO.getProducts()!=null)
		{
			
			
			/*
			 * List<Product> products = categoryRequestDTO.getProducts().stream()
			 * .map(productRequest -> new Product( productRequest.getProductName(),
			 * productRequest.getProductImage(), productRequest.getProductPrice(),
			 * productRequest.getProductQuantity(), productRequest.getInStock() ))
			 * .collect(Collectors.toList());
			 * 
			 * products.stream().forEach(product -> category.addProduct(product));
			 */
		}
		else 
		{
            
		}
		return null;
	}

	@Override
	public List<CategoryResponseDTO> getAllCategory() 
	{

		return null;
	}

	@Override
	public String updateCategory(String categoryId, String categoryName) 
	{

		return null;
	}

	@Override
	public CategoryResponseDTO findCategoryByCategoryId(String categoryId) 
	{

		return null;
	}

}
