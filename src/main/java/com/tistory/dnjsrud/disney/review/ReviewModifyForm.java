package com.tistory.dnjsrud.disney.review;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReviewModifyForm {

    @NotEmpty
    private Long reviewId;

    @NotEmpty
    private float originalStar;

    @NotEmpty
    private float star;

    @NotEmpty
    private String content;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long movieId;

    public ReviewModifyForm(Long reviewId, float originalStar, float star, String content, Long userId, Long movieId) {
        this.reviewId = reviewId;
        this.originalStar = originalStar;
        this.star = star;
        this.content = content;
        this.userId = userId;
        this.movieId = movieId;
    }
}
