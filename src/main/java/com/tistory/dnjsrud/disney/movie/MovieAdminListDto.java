package com.tistory.dnjsrud.disney.movie;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class MovieAdminListDto {
    private Long id;
    private String title;
    private Date releaseDate;
    private float star;
    private List<String> genreNameList = new ArrayList<>();
    private String storedFileName;
    private Long reviewCount;
    private boolean visible;

    @QueryProjection
    public MovieAdminListDto(Long id, String title, Date releaseDate, float star, String storedFileName, boolean visible) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.star = star;
        this.storedFileName = storedFileName;
        this.visible = visible;
    }

    //== 비즈니스 로직 ==//
    public void changeGenreList(List<String> genreNameList) {
        this.genreNameList = genreNameList;
    }

    public void changeReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
