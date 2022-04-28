package com.tistory.dnjsrud.disney.review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom {
    Long countByUserId(Long userId);
    Long countByMovieId(Long movieId);
    List<ReviewDetailDto> findReviewDetailDtoListByMovieId(Long movieId);
    Optional<ReviewDetailDto> findReviewDetailDtoByMovieIdAndUserId(Long movieId, Long userId);
    List<ReviewUserDto> findReviewUserDtoByUserId(Long userId);
}
