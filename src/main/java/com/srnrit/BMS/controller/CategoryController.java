package com.srnrit.BMS.controller;

import com.srnrit.BMS.dto.CategoryRequestDTO;
import com.srnrit.BMS.dto.CategoryResponseDTO;
import com.srnrit.BMS.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CategoryController {

    ICategoryService categoryService;

    @PostMapping(value="/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequestDTO categoryRequest)
    {
        return new ResponseEntity<CategoryResponseDTO> (this.categoryService.addCategory(categoryRequest), HttpStatus.OK);
    }

    @GetMapping(value="/allCategories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory()
    {
        return new ResponseEntity<List<CategoryResponseDTO>>(this.categoryService.getAllCategory(),HttpStatus.OK);
    }
}
