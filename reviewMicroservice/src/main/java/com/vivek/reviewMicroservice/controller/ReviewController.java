package com.vivek.reviewMicroservice.controller;

import com.vivek.reviewMicroservice.mapper.ReviewMapper;
import com.vivek.reviewMicroservice.model.Review;
import com.vivek.reviewMicroservice.payload.dto.ReviewDTO;
import com.vivek.reviewMicroservice.payload.dto.SalonDTO;
import com.vivek.reviewMicroservice.payload.dto.UserDTO;
import com.vivek.reviewMicroservice.payload.request.CreateReviewRequest;
import com.vivek.reviewMicroservice.payload.response.ApiResponse;
import com.vivek.reviewMicroservice.service.ReviewService;
import com.vivek.reviewMicroservice.service.client.SalonFeignClient;
import com.vivek.reviewMicroservice.service.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserFeignClient userService;
    @Autowired
    private SalonFeignClient salonService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(
            @PathVariable Integer salonId) {

        List<Review> reviews = reviewService.getReviewsBySalonId(salonId);

        List<ReviewDTO> reviewDTOS =  reviews.stream().map((review)->
                {
                    UserDTO user= null;
                    try {
                        user = userService.getUserById(review.getUserId()).getBody();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return ReviewMapper.mapToDTO(review,user);
                }
        ).toList();

        return ResponseEntity.ok(reviewDTOS);

    }

    @PostMapping("/salon/{salonId}")
    public ResponseEntity<ReviewDTO> writeReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Integer salonId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();
        SalonDTO product = salonService.getSalonById(salonId).getBody();


        Review review = reviewService.createReview(
                req, user, product
        );
        UserDTO reviewer = userService.getUserById(
                review.getUserId()
        ).getBody();

        ReviewDTO reviewDTO= ReviewMapper.mapToDTO(review,reviewer);

        return ResponseEntity.ok(reviewDTO);

    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Integer reviewId,
            @RequestHeader("Authorization") String jwt)
            throws Exception {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        Review review = reviewService.updateReview(
                reviewId,
                req.getReviewText(),
                req.getReviewRating(),
                user.getId()
        );
        return ResponseEntity.ok(review);

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Integer reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception
    {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse("Review deleted successfully");


        return ResponseEntity.ok(res);

    }
}
