package com.srnrit.BMS.service.impl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService 
{
	private ICategoryDao categoryDAO;


	public CategoryServiceImpl(ICategoryDao categoryDAO) {
		super();
		this.categoryDAO = categoryDAO;
	}

	@Override
	public CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO) {
		Category category=new Category();
		category.setCategoryname(categoryRequestDTO.getCategoryName());

		if(categoryRequestDTO.getProducts()!=null)
		{
			List<Product> products = categoryRequestDTO.getProducts().stream()
					.map(productRequest -> new Product(
							productRequest.getProductName(),
							productRequest.getProductImage().toString(),							
							productRequest.getProductQuantity(),
							productRequest.getProductPrice(),
							productRequest.getInStock()
							))
					.collect(Collectors.toList());

			products.stream().forEach(product -> category.addProduct(product));

		}
		else 
		{
			Optional<Category> insertedCategory = this.categoryDAO.insertCategory(category);

			/*
			 * if(insertedCategory.isPresent()) { CategoryResponseDTO categoryResponse =
			 * EntityToDTO.toCategoryResponse(category); return categoryResponse; } else {
			 * throw new CategoryNotCreatedException("Category not created !"); }
			 */
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
