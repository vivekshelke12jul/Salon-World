package com.vivek.categoryMicroservice.controller;


import com.vivek.categoryMicroservice.model.Category;
import com.vivek.categoryMicroservice.payload.dto.SalonDTO;
import com.vivek.categoryMicroservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category/salon-owner")
public class SalonCategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestBody SalonDTO salon
    ) throws Exception {
        Category savedCategory = categoryService.createCategory(category, salon);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
}
