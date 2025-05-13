package com.vivek.categoryMicroservice.service;

import com.vivek.categoryMicroservice.model.Category;
import com.vivek.categoryMicroservice.payload.dto.SalonDTO;
import com.vivek.categoryMicroservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category, SalonDTO salon) {

        Category newCategory=new Category();
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salon.getId());
        return categoryRepository.save(newCategory);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Integer id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Integer id) throws Exception {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found")
                );
    }

    @Override
    public Category updateCategory(Integer id, Category category) throws Exception {

        Category existingCategory=getCategoryById(id);
        existingCategory.setName(category.getName());
        existingCategory.setImage(category.getImage());

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Integer id) throws Exception {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
