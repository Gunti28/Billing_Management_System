package com.srnrit.BMS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.entity.Category;

@SpringBootTest
class BmsApplicationTests 
{

	@Autowired
	private ICategoryDao iCategoryDao;
	@Test
	void categorySavetest() 
	{
		Category category=new Category();
		category.setCategoryname("Watches");
		Optional<Category> categoryRegister=iCategoryDao.insertCategory(category);
		assertTrue(categoryRegister.isPresent());
		System.out.println(categoryRegister.get());
	}

}


