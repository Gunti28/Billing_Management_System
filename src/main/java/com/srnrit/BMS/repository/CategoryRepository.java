package com.srnrit.BMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.srnrit.BMS.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,String>{
	Category findByCategoryNameIgnoreCase(String categoryName);
	boolean existsByCategoryName(String categoryName);
	
	
	@Query(value = "select category.categoryName from Category category")
	List<String> getAllCategoryNames();
}
