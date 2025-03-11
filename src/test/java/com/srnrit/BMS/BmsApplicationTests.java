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

	/*
	 * @Test void categoryTest() { Category category=new Category();
	 * category.setCategoryname("Watch"); List<Product> list=new ArrayList<>();
	 * list.add(new Product("Watch", "default.img",5, 500.0, true)); list.add(new
	 * Product("Mobile", "default.img",5, 15000.0, true));
	 * category.setProducts(list); System.out.println(category); }
	 */
	
	  @Autowired 
	  private ICategoryDao iCategoryDao;
	  
	  @Test void categorySavetest() 
	  { 
	  Category category=new Category();
	  category.setCategoryName("Watches"); 
	  Optional<Category>categoryRegister=iCategoryDao.insertCategory(category);
	  assertTrue(categoryRegister.isPresent());
	  System.out.println(categoryRegister.get()); 
	  }	 
}


