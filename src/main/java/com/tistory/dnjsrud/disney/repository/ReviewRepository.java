package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByUserId(Long userId);
    List<Review> findReviewsByMovieId(Long movieId);
    Optional<Review> findReviewByUserIdAndMovieId(Long userId, Long movieId);
    Long deleteReviewByUserIdAndMovieId(Long userId, Long movieId);
}
