package com.tistory.dnjsrud.disney.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    List<Review> findByUserId(Long userId);
    List<Review> findByMovieId(Long movieId);
    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);
    Long deleteByUserIdAndMovieId(Long userId, Long movieId);
}
