package com.srnrit.BMS.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService 
{
    private ICategoryDao iCategoryDao;
      
	public CategoryServiceImpl(ICategoryDao iCategoryDao) 
	{
		super();
		this.iCategoryDao = iCategoryDao;
	}

	@Override
	public CategoryResponseDTO addCategoryWithProducts(CategoryRequestDTO categoryRequestDTO) 
	{
		Category category=new Category();
		category.setCategoryname(categoryRequestDTO.getCategoryName());
		if(categoryRequestDTO !=null)
		{
			
		}
		else 
		{
			throw new CategoryNotCreatedException("Category not created !");
		}
		
		return null;
	}

	@Override
	public List<CategoryResponseDTO> getAllCategory() {
		return null;
	}

	@Override
	public String updateCategory(String categoryId, String categoryName) {
		return null;
	}

	@Override
	public CategoryResponseDTO findCategoryByCategoryId(String categoryId) {
		return null;
	}

}
