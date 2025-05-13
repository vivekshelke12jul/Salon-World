package com.vivek.categoryMicroservice.repository;

import com.vivek.categoryMicroservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer> {
    Set<Category> findBySalonId(Integer id);
}
