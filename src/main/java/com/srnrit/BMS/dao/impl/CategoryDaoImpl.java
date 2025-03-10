package com.srnrit.BMS.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;

@Component
public class CategoryDaoImpl implements ICategoryDao
{

	private CategoryRepository categoryRepository;

	public CategoryDaoImpl(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}

	//for category insertion
	@Override
	public Optional<Category> insertCategory(Category category) {
		Category existingCategory = this.categoryRepository.findByCategoryName(category.getCategoryName());
		if(existingCategory == null) {
			Category savedCategory = this.categoryRepository.save(category);
			return savedCategory!=null?Optional.of(savedCategory):Optional.empty();
		}
		else {
			throw new CategoryNotCreatedException("Category already available with name : "+category.getCategoryName());
		} 
	}


	//for fetching category
	@Override
	public Optional<List<Category>> getAllCategory() {
		long count = categoryRepository.count();
		if(count > 0) {
			List<Category> categories = categoryRepository.findAll();
			return Optional.of(categories);
		}else {
			return Optional.empty();
		}
	}


	//for updating category
	public Optional<String> updateCategory(String categoryId, String categoryName) {
	    Category category = categoryRepository.findById(categoryId)
	        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
	    category.setCategoryName(categoryName);  

	    categoryRepository.save(category);
	    return Optional.of("Category updated successfully with id: " + categoryId);
	}

	

	//for fetching category by Id
	@Override
	public Optional<Category> getCategoryByCategoryId(String categoryId) {
		if(this.categoryRepository.existsById(categoryId)) {
			return  this.categoryRepository.findById(categoryId);
		}
		return Optional.empty();
	}

	//Delete by id
	@Override
	public Optional<String> deleteCategory(String categoryId) {
		if(categoryRepository.existsById(categoryId)) {
			this.categoryRepository.deleteById(categoryId);
			return Optional.of("categoryId deleted successfully");
		}
		else {
			return Optional.empty();
		}

	}


}
