package com.vivek.reviewMicroservice.repository;

import com.vivek.reviewMicroservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findReviewsByUserId(Integer userId);
    List<Review> findReviewsBySalonId(Integer productId);
}
