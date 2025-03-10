package com.srnrit.BMS.dao;

import java.util.List;
import java.util.Optional;

import com.srnrit.BMS.entity.Category;

public interface ICategoryDao 
{
    Optional<Category> insertCategory(Category category);
    
    Optional<List<Category>> getAllCategory();
    
	Optional<String> updateCategory(String categoryId,String categoryName);
	
	Optional<Category> getCategoryByCategoryId(String categoryId) ;
	
	Optional<String> deleteCategory(String categoryId); 
	
	
	
}


