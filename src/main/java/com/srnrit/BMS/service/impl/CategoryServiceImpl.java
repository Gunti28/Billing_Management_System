package com.srnrit.BMS.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
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
	public CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO) {
		if (categoryRequestDTO == null || categoryRequestDTO.getCategoryName() == null || categoryRequestDTO.getCategoryName().trim().isEmpty() || categoryRequestDTO.getCategoryName().equalsIgnoreCase("null")) {
			throw new IllegalArgumentException("Category name cannot be blank and name mustn't be null");
		}
		String newCategoryName = categoryRequestDTO.getCategoryName().trim();

		List<Category> existingCategories = categoryDAO.getAllCategory().orElse(Collections.emptyList());

		existingCategories.stream()
		.filter(existingCategory -> StringUtils.calculateSimilarCategoryCheck(newCategoryName, existingCategory.getCategoryName()) <= 3)
		.findFirst()
		.ifPresent(existingCategory -> {
			throw new CategoryNameAlreadyExistsException(
					"A similar category already exists with the name: " + existingCategory.getCategoryName()
					);
		});


		Category category=new Category();
		category.setCategoryName(categoryRequestDTO.getCategoryName());

		if(categoryRequestDTO.getProducts()!=null) {
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

	//service for Get All Category details 
	@Override
	public List<CategoryResponseDTO> getAllCategory() {
		Optional<List<Category>> allCategory = categoryDAO.getAllCategory();

		if(allCategory.isPresent() && !allCategory.get().isEmpty()) {
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


	//Service for updating CategoryName with CategoryId
	@Override
	public String updateCategory(String categoryId, String categoryName) {

		if (categoryId == null || categoryId.isBlank()) {
			throw new IllegalArgumentException("CategoryId must not be null or empty");
		}
		if (categoryName == null || categoryName.isBlank()  || categoryName.equalsIgnoreCase("Null")) {
			throw new IllegalArgumentException("CategoryName must not be null or empty");
		}
		if (categoryName.length() < 3) {
			throw new IllegalArgumentException("CategoryName must be at least 3 characters long");
		}

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

	}

	//Service for fetching Category details by CategoryName
	@Override
	public CategoryResponseDTO findCategoryByCategoryName(String categoryName) {
		if (categoryName == null || categoryName.trim().isEmpty()) {
			throw new CategoryNotCreatedException("Category name must not be null or empty");
		}

		List<Category> allCategories = this.categoryDAO.getAllCategory().orElse(Collections.emptyList());


		Optional<Category> similarCategory = allCategories.stream()
				.filter(category -> StringUtils.calculateSimilarCategoryCheck(category.getCategoryName().toLowerCase(), categoryName.toLowerCase()) <= 2)
				.findFirst();

		return similarCategory
				.map(EntityToDTO::toCategoryResponse) 
				.orElseThrow(() -> new CategoryNotFoundException("No category found with name: " + categoryName));
	}
}


