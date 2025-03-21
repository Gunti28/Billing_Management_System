package com.srnrit.BMS.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CategoryTest 
{
	

//	  @Test void categoryTest() { 
//	  Category category=new Category();
//	  category.setCategoryName("Watch"); 
//	  List<Product> list=new ArrayList<>();
//	  list.add(new Product("Watch", "default.img",5, 500.0, true)); 
//	  list.add(new Product("Mobile", "default.img",5, 15000.0, true));
//	  category.setProducts(list); System.out.println(category); 
//	  }

	  @Test void categoryTest() { 
	  Category category=new Category();
	  category.setCategoryName("Watch"); 
	  List<Product> list=new ArrayList<>();
	  list.add(new Product("Watch", 5, 500.0, true)); 
	  list.add(new Product("Mobile", 5, 15000.0, true));
	  category.setProducts(list); System.out.println(category); 
	  }

}
