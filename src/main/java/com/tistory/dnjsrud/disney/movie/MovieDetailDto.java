package com.tistory.dnjsrud.disney.movie;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class MovieDetailDto {
    private Long id;
    private String title;
    private Date releaseDate;
    private float star;
    private List<String> genreNameList = new ArrayList<>();
    private String content;
    private String storedFileName;

    @QueryProjection
    public MovieDetailDto(Long id, String title, Date releaseDate, float star, String content, String storedFileName) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.star = star;
        this.content = content;
        this.storedFileName = storedFileName;
    }

    public void changeGenreList(List<String> genreNameList) {
        this.genreNameList = genreNameList;
    }
}
