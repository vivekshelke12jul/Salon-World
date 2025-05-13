package com.vivek.categoryMicroservice.service;

import com.vivek.categoryMicroservice.model.Category;
import com.vivek.categoryMicroservice.payload.dto.SalonDTO;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    Category createCategory(Category category, SalonDTO salon);

    List<Category> getAllCategories();

    Set<Category> getAllCategoriesBySalon(Integer id);

    Category getCategoryById(Integer id) throws Exception;

    Category updateCategory(Integer id,Category category) throws Exception;

    void deleteCategory(Integer id) throws Exception;
}
