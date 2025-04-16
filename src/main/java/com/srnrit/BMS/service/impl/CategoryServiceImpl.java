package com.srnrit.BMS.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
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

	//Add Category Method for the Service Layer
	@Override
	public CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO) {
		Category category=new Category();
		category.setCategoryName(categoryRequestDTO.getCategoryName());

		if(categoryRequestDTO.getProducts()!=null) {
			List<Product> products = categoryRequestDTO.getProducts().stream()
					.map(productRequest -> new Product(
							productRequest.getProductName(),						
							productRequest.getProductQuantity(),
							productRequest.getProductPrice(),
							productRequest.getInStock()
							))
					.collect(Collectors.toList());

			products.stream().forEach(product -> category.addProduct(product));

			Optional<Category> insertCategory = this.categoryDAO.insertCategory(category);
			if(insertCategory.isPresent()) {
				CategoryResponseDTO categoryResponse = EntityToDTO.toCategoryResponse(category);
				return categoryResponse;
			}
			else {
				throw new CategoryNotCreatedException("Category not created !");
			}
		}
		else {
			Optional<Category> insertedCategory = this.categoryDAO.insertCategory(category);

			if(insertedCategory.isPresent()) {
				CategoryResponseDTO categoryResponse = EntityToDTO.toCategoryResponse(category); 
				return categoryResponse; 
			} 
			else {
				throw new CategoryNotCreatedException("Category not created !"); 
			}
		}
	}

	//Get All Category details 
	@Override
	public List<CategoryResponseDTO> getAllCategory() {
		Optional<List<Category>> allCategory = categoryDAO.getAllCategory();

		if(allCategory.isPresent()) {
			List<CategoryResponseDTO> allCategoryResponse=new ArrayList<>();
			List<Category> categories=allCategory.get();

			for(Category category:categories) {
				List<Product> products=category.getProducts();
				List<ProductResponseDTO> productResponse=new ArrayList<>();
				for(Product product:products) {
					ProductResponseDTO productResponseDTO=EntityToDTO.toProductResponseDTO(product);
					productResponse.add(productResponseDTO);
				}
				CategoryResponseDTO categoryResponseDTO=EntityToDTO.toCategoryResponse(category);
				categoryResponseDTO.setProducts(productResponse);

				allCategoryResponse.add(categoryResponseDTO);
			}
			return allCategoryResponse;
		}
		else {
			throw new CategoryNotFoundException("No Category available");
		}
	}


    //Here we written logic for updating CategoryName with CategoryId
	@Override
	public String updateCategory(String categoryId, String categoryName) {

	    if (categoryId == null || categoryId.trim().isEmpty()) {
	        throw new IllegalArgumentException("CategoryId must not be null or empty");
	    }
	    if (categoryName == null || categoryName.trim().isEmpty()) {
	        throw new IllegalArgumentException("CategoryName must not be null or empty");
	    }
	    if (categoryName.length() < 3) {
	        throw new IllegalArgumentException("CategoryName must be at least 3 characters long");
	    }
	    Optional<String> updateCategory = this.categoryDAO.updateCategory(categoryId, categoryName);
	    return updateCategory.orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
	}

	
    //Here we written logic for fetching Category details by CategoryId
	@Override
	public CategoryResponseDTO findCategoryByCategoryId(String categoryId) 
	{

		if(categoryId != null)
		{
			Optional<Category> optionalcategory = this.categoryDAO.getCategoryByCategoryId(categoryId);
			if(optionalcategory.isPresent())
			{
				Category category=optionalcategory.get();
				CategoryResponseDTO categoryResponse = EntityToDTO.toCategoryResponse(category);
				return categoryResponse;
			}
			else 
			{
				throw new CategoryNotFoundException("Category not exist with id : "+categoryId);
			}
		}
		else 
		{
			throw new RuntimeException("Category must not be null");
		}

	}


}
