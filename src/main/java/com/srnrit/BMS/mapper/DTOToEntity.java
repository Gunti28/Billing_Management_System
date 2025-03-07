package com.srnrit.BMS.mapper;

import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.entity.Category;

public class DTOToEntity 
{
	public static Category categoryRequestDTOToCategory(CategoryRequestDTO dto)
	{
		Category category = new Category();
		BeanUtils.copyProperties(dto, category);		
		System.out.println(category);
		return category;		
	}
	

}
