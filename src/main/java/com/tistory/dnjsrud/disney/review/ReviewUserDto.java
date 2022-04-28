package com.tistory.dnjsrud.disney.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.ToString;

@ToString
public class ReviewUserDto {
    private Long movieId;
    private String fileFullPath;
    private float star;
    private String content;

    @QueryProjection
    public ReviewUserDto(Long movieId, String fileFullPath, float star, String content) {
        this.movieId = movieId;
        this.fileFullPath = fileFullPath;
        this.star = star;
        this.content = content;
    }
}
