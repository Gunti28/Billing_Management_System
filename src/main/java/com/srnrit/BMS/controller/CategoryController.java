package com.srnrit.BMS.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.dto.UpdateCategoryRequestDTO;
import com.srnrit.BMS.service.ICategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
	private  ICategoryService categoryService;

	public CategoryController(ICategoryService categoryService){
		this.categoryService=categoryService;
	}

	//API to create a Category with Products in it
	@PostMapping(value="/addCategoryWithProducts")
	public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequestDTO categoryRequest) {
		CategoryResponseDTO createdCategory= categoryService.addCategoryWithProducts(categoryRequest);
		return new ResponseEntity<>(createdCategory,HttpStatus.CREATED);
	}

	//API to get all the categories present in the DB
	@GetMapping(value="/allCategories")
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategory() {
		List<CategoryResponseDTO> categories=categoryService.getAllCategory();
		return new ResponseEntity<>(categories,HttpStatus.OK);
	}

	//API to update a Category by using CategoryId
	@PutMapping("/updateCategoryById")
	public ResponseEntity<?> updateCategory(@Valid @RequestBody UpdateCategoryRequestDTO categoryRequest) {
		String categoryId = categoryRequest.getCategoryId();
		String categoryName = categoryRequest.getCategoryName();

		String updateCategory = categoryService.updateCategory(categoryId, categoryName);
		return new ResponseEntity<>(updateCategory, HttpStatus.OK);
	}

	//API to Get Category by using CategoryId
	@GetMapping("/categoryById")
	public ResponseEntity<?> findCategoryById(@RequestParam String categoryId) {
		CategoryResponseDTO foundCategory = categoryService.findCategoryByCategoryId(categoryId);
		if (foundCategory == null) {
			return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(foundCategory, HttpStatus.OK);
	}



	//API to Get Category by using CategoryId
	@GetMapping("/categoryByName")
	public ResponseEntity<?> findCategoryByCategoryName(@RequestParam String categoryName){
		CategoryResponseDTO foundCategory =categoryService.findCategoryByCategoryName(categoryName);
		return  new ResponseEntity<>(foundCategory,HttpStatus.OK) ;
	}

}