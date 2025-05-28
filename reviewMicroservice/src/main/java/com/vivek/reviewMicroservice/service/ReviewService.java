package com.vivek.reviewMicroservice.service;

import com.vivek.reviewMicroservice.model.Review;
import com.vivek.reviewMicroservice.payload.dto.SalonDTO;
import com.vivek.reviewMicroservice.payload.dto.UserDTO;
import com.vivek.reviewMicroservice.payload.request.CreateReviewRequest;
import com.vivek.reviewMicroservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    public Review createReview(CreateReviewRequest req,
                               UserDTO user,
                               SalonDTO salon) {
        Review newReview = new Review();

        newReview.setReviewText(req.getReviewText());
        newReview.setRating(req.getReviewRating());
        newReview.setUserId(user.getId());
        newReview.setSalonId(salon.getId());

        return reviewRepository.save(newReview);
    }

    public List<Review> getReviewsBySalonId(Integer productId) {
        return reviewRepository.findReviewsBySalonId(productId);
    }


    public Review updateReview(Integer reviewId,
                               String reviewText,
                               double rating,
                               Integer userId) throws Exception {
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()-> new Exception("Review Not found"));

        if(review.getUserId()!=userId){
            throw new Exception("You do not have permission to delete this review");
        }

        review.setReviewText(reviewText);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    public void deleteReview(Integer reviewId,Integer userId) throws Exception{
        Review review=reviewRepository.findById(reviewId).orElseThrow(()-> new Exception("Review Not found"));
        if(review.getUserId()!=userId){
            throw new Exception("You do not have permission to delete this review");
        }
        reviewRepository.delete(review);
    }

}
