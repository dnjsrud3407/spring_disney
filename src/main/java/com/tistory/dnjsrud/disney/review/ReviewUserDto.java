package com.tistory.dnjsrud.disney.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ReviewUserDto {
    private Long movieId;
    private String title;
    private String storedFileName;
    private float star;
    private String content;

    @QueryProjection
    public ReviewUserDto(Long movieId, String title, String storedFileName, float star, String content) {
        this.movieId = movieId;
        this.title = title;
        this.storedFileName = storedFileName;
        this.star = star;
        this.content = content;
    }
}
