package com.tistory.dnjsrud.disney.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewDetailDto {
    private Long id;
    private float star;
    private String content;
    private String nickname;

    @QueryProjection
    public ReviewDetailDto(Long id, float star, String content, String nickname) {
        this.id = id;
        this.star = star;
        this.content = content;
        this.nickname = nickname;
    }
}
