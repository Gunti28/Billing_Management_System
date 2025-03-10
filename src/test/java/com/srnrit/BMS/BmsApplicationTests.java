package com.srnrit.BMS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;

@SpringBootTest
class BmsApplicationTests 
{

	
	  @Test void categoryTest() 
	  { 
	  Product product=new Product();
	  product.setProductName("Biryani");
	  product.setProductPrice(500.0);
	  product.setProductImage("default.png");
	  product.setProductQuantity(1);
	  product.setInStock(true);
	  
	  Product product1=new Product();
	  product1.setProductName("Biryani");
	  product1.setProductPrice(500.0);
	  product1.setProductImage("default.png");
	  product1.setProductQuantity(1);
	  product1.setInStock(true);
	  
	  
	  
	  
	  
//	  List<Product> list=new ArrayList<>();
//	  list.add(new Product("Watch", "default.img",5, 500.0, true)); list.add(new
//	  Product("Mobile", "default.img",5, 15000.0, true));
//	  category.setProducts(list); System.out.println(category); }
	 
	
	/*
	  @Autowired 
	  private ICategoryDao iCategoryDao;
	  
	  @Test void categorySavetest() 
	  { 
	  Category category=new Category();
	  category.setCategoryname("Watches"); 
	  Optional<Category>categoryRegister=iCategoryDao.insertCategory(category);
	  assertTrue(categoryRegister.isPresent());
	  System.out.println(categoryRegister.get()); 
	  }	 
	  */
	
	  }
}


