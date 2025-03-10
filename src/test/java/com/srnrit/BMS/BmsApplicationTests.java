package com.srnrit.BMS;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.ICategoryDao;
import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.ICategoryService;

@SpringBootTest
class BmsApplicationTests 
{
	/*
	 * @Autowired private ICategoryDao iCategoryDao;
	 */

	
	/*
	 * @Test void categoryTest() { Category category=new Category();
	 * category.setCategoryname("Watch"); List<Product> list=new ArrayList<>();
	 * list.add(new Product("Watch", "default.img",5, 500.0, true)); list.add(new
	 * Product("Mobile", "default.img",5, 15000.0, true));
	 * category.setProducts(list); System.out.println(category); }
	 */
	 
	
	/*
	 * @Autowired private ICategoryDao iCategoryDao;
	 * 
	 * @Test void categorySavetest() { Category category=new Category();
	 * category.setCategoryname("Watches");
	 * Optional<Category>categoryRegister=iCategoryDao.insertCategory(category);
	 * assertTrue(categoryRegister.isPresent());
	 * System.out.println(categoryRegister.get()); }
	 */
	
	/*
	 * @Test void getAllCategory() { Category category=new Category();
	 * 
	 * }
	 */
	@Autowired
	private ICategoryService iCategoryService;

	/*
	@Test
	void addCategoryService() 
	{
		  Category category=new Category();
		  category.setCategoryname("Watch"); 
		  List<Product> list=new ArrayList<>();
		  list.add(new Product("Watch", "default.img",5, 500.0, true)); 
		  list.add(new Product("Mobile", "default.img",5, 15000.0, true));
		  category.setProducts(list); System.out.println(category);
		  
		  CategoryRequestDTO categoryRequestDTO=new CategoryRequestDTO();
		  BeanUtils.copyProperties(categoryRequestDTO, category);		
		  CategoryResponseDTO categoryResponse = iCategoryService.addCategoryWithProducts(categoryRequestDTO);
		  System.out.println(categoryResponse);
	}

	@Test
	void getAllCategory()
	{
		List<CategoryResponseDTO> allCategory = iCategoryService.getAllCategory();
		System.out.println(allCategory);
	}

	@Test
	void getAllCategory()
	{
		CategoryResponseDTO categoryByCategoryId = iCategoryService.findCategoryByCategoryId("cid_01");
		System.out.println(categoryByCategoryId);
	}
*/
	
	
}


