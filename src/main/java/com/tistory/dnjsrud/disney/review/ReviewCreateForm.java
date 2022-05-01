package com.tistory.dnjsrud.disney.review;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReviewCreateForm {

    @NotEmpty
    private float star;

    @NotEmpty
    private String content;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long movieId;

    public ReviewCreateForm(float star, String content, Long userId, Long movieId) {
        this.star = star;
        this.content = content;
        this.userId = userId;
        this.movieId = movieId;
    }
}
