package com.example.demo.controllers;

import com.example.demo.entities.Review;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/reviews")
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReview(@RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Review created successfully", createdReview, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null, false));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllReviews() {
        try {
            List<Review> reviews = reviewService.getAllReviews();
            return ResponseEntity.ok(
                    new ApiResponse("Reviews retrieved successfully", reviews, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, false));
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> getReviewById(@PathVariable Long reviewId) {
        try {
            Optional<Review> review = reviewService.getReviewById(reviewId);
            return ResponseEntity.ok(
                    new ApiResponse("Review retrieved successfully", review.orElseThrow(), true));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, false));
        }
    }

    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse> getAverageRating() {
        try {
            Double averageRating = reviewService.getAverageRating();
            return ResponseEntity.ok(
                    new ApiResponse("Average rating retrieved successfully", averageRating, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, false));
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(
                    new ApiResponse("Review deleted successfully", null, true));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, false));
        }
    }
}