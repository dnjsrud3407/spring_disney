package com.tistory.dnjsrud.disney.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom {
    Long countByUserId(Long userId);
    Long countByMovieId(Long movieId);
    Page<ReviewDetailDto> findReviewDetailDtoListByMovieId(Pageable pageable, Long movieId);
    Page<ReviewDetailDto> findReviewDetailDtoListByMovieIdNotUserId(Pageable pageable, Long movieId, Long userId);
    Optional<ReviewDetailDto> findReviewDetailDtoByMovieIdAndUserId(Long movieId, Long userId);
    List<ReviewUserDto> findReviewUserDtoByUserId(Long userId);
}
