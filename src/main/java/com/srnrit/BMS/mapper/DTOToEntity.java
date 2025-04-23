package com.srnrit.BMS.mapper;


import org.springframework.beans.BeanUtils;
<<<<<<< HEAD
=======

import com.srnrit.BMS.dto.UpdateUserRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.entity.User;

public class DTOToEntity {
	
	public static User userRequestDtoToUserEntity(UserRequestDTO dto)
	{
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		user.setUserPhone(Long.parseLong(dto.getUserPhone()));
		return user;
	}
	
	public static User userUpdateRequestDtoToUserEntity(UpdateUserRequestDTO dto)
	{
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		user.setUserPhone(Long.parseLong(dto.getUserPhone()));
		return user;
	}
	
	


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
>>>>>>> 02d058093685d0df4d9480f65b8a49b1ba11c605

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;

public class DTOToEntity 
{

	public static Category categoryRequestDTOToCategory(CategoryRequestDTO dto)
	{
		Category category = new Category();
		BeanUtils.copyProperties(dto, category);
		return category;		
	}

	public static Product toProduct(ProductRequestDTO productRequestDTO) {
		return new Product(
				productRequestDTO.getProductName(),
				productRequestDTO.getProductImage(),
				productRequestDTO.getProductQuantity(),
				productRequestDTO.getProductPrice(),
				productRequestDTO.getInStock()
				);
	}



}
