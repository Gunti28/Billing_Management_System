package com.srnrit.BMS.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.UpdateCategoryRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.ICategoryService;
import com.srnrit.BMS.util.StringUtils;

@Service
public class CategoryServiceImpl implements ICategoryService 
{
	private final Set<String> updatedCategories = new HashSet<>();
	private ICategoryDao categoryDAO;

	public CategoryServiceImpl(ICategoryDao categoryDAO) {
		super();
		this.categoryDAO = categoryDAO;
	}

	//service for to add category with products
	@Override
	public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) 
	{
		if (categoryRequestDTO == null) {
			throw new IllegalArgumentException("CategoryRequestDTO can't be null");
		}

		String categoryName = categoryRequestDTO.getCategoryName().trim();
		
		if (categoryName.isEmpty()) {  
	        throw new IllegalArgumentException("Category name cannot be blank");
	    }
		
		// Fetch all category names
		List<String> categoryNames = this.categoryDAO.fetchAllCategoryNames();

		if(categoryNames!=null)
		{
			Optional<String> existingCategory = categoryNames.stream().filter(c-> c.equalsIgnoreCase(categoryName)).findFirst();
		    
			if(existingCategory.isPresent())
		    	throw new CategoryNameAlreadyExistsException(
						"A category already exists with the name: " + existingCategory.get());
		    
			existingCategory = categoryNames.stream().filter(c->categoryName.toLowerCase().contains(c.toLowerCase())).findFirst();
		    
			if(existingCategory.isPresent())
		    	throw new CategoryNameAlreadyExistsException(
						"A category already exists with the name: " + existingCategory.get());
		 }
		    Category category = new Category();
		    BeanUtils.copyProperties(categoryRequestDTO, category);
		    
		    Optional<Category> insertCategory = categoryDAO.insertCategory(category);
		    
			if (insertCategory.isPresent()) 
			{	
				System.out.println(insertCategory.get());
				return EntityToDTO.toCategoryResponse(insertCategory.get());
			}
			
			else throw new CategoryNotCreatedException("Category not created!");
	}




	//service for Get All Category details 
	@Override
	public List<CategoryResponseDTO> getAllCategory() 
	{
		Optional<List<Category>> allCategory = categoryDAO.getAllCategory();

		if(allCategory.isPresent() && !allCategory.get().isEmpty()) 
		{
			List<CategoryResponseDTO> allCategoryResponse=new ArrayList<>();
			List<Category> categories=allCategory.get();

			for(Category category:categories) 
			{
				CategoryResponseDTO categoryResponseDTO=EntityToDTO.toCategoryResponse(category);
				allCategoryResponse.add(categoryResponseDTO);
			}
			return allCategoryResponse;
		}
		else {
			throw new CategoryNotFoundException("No Category available");
		}
	}


	//Service for updating CategoryName with CategoryId
	@Override
	public String updateCategory(UpdateCategoryRequestDTO dto)
	{
		String categoryId=dto.getCategoryId();
		String categoryName=dto.getCategoryName();


		Optional<String> updatedCategory = this.categoryDAO.updateCategory(categoryId, categoryName);
		if (updatedCategory.isPresent()) 
		{
			if (updatedCategories.contains(categoryId)) {
				return "Category already updated once with id: " + categoryId;
			}
			updatedCategories.add(categoryId);
			return updatedCategory.get();
		} else {
			throw new CategoryNotFoundException("Category not found with id: " + categoryId);
		}	
	}


	//Service for fetching Category details by CategoryId
	@Override
	public CategoryResponseDTO findCategoryByCategoryId(String categoryId) 
	{
		if(categoryId == null || categoryId.trim().isEmpty())
		{
			throw new IllegalArgumentException("Category must not be null or blank");			
		}
		else 
		{
			Optional<Category> optionalcategory = this.categoryDAO.getCategoryByCategoryId(categoryId.trim());
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

	}

	//Service for fetching Category details by CategoryName
	@Override
	public CategoryResponseDTO findCategoryByCategoryName(String categoryName) {
		if (categoryName == null || categoryName.trim().isEmpty()) {
			throw new CategoryNotCreatedException("Category name must not be null or empty");
		}

		List<Category> allCategories = this.categoryDAO.getAllCategory().orElse(Collections.emptyList());


		Optional<Category> similarCategory = allCategories.stream()
				.filter(category -> StringUtils.calculateSimilarCategoryCheck(category.getCategoryName().toLowerCase(), categoryName.toLowerCase().trim()) <= 2)
				.findFirst();

		return similarCategory
				.map(EntityToDTO::toCategoryResponse) 
				.orElseThrow(() -> new CategoryNotFoundException("No category found with name: " + categoryName));
	}
}


