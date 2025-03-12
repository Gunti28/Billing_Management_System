package com.srnrit.BMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnrit.BMS.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,String>{
	Category findByCategoryNameIgnoreCase(String categoryName);
	boolean existsByCategoryName(String categoryName);
}
