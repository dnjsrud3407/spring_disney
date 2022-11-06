package com.tistory.dnjsrud.disney.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateForm {

    @NotNull(message = "별점은 1점 이상이어야합니다")
    @DecimalMin(value = "1.0", message = "별점은 1점 이상이어야합니다")
    private float star;

    @NotEmpty(message = "평가글을 작성해주세요.")
    private String content;

    private Long userId;

    @NotNull
    private Long movieId;

    public ReviewCreateForm(float star, String content) {
        this.star = star;
        this.content = content;
    }
}
