package com.srnrit.BMS.controller;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/updateCategory/{CategoryId}")
    public ResponseEntity<?>  updateCategory(@PathVariable String CategoryId,@RequestParam String CategoryName){
       String updateCategory =categoryService.updateCategory(CategoryId,CategoryName);
       return new ResponseEntity<>(updateCategory,HttpStatus.OK);
    }


    
    //API to Get Category by using CategoryId
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> findCategoryById(@PathVariable String categoryId){
        CategoryResponseDTO foundCategory =categoryService.findCategoryByCategoryId(categoryId);
        return  new ResponseEntity<>(foundCategory,HttpStatus.OK) ;
    }

}
