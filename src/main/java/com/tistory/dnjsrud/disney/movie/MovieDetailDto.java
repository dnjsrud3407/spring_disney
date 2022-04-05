package com.tistory.dnjsrud.disney.movie;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MovieDetailDto {
    private Long id;
    private String title;
    private float star;
    private List<String> genreList = new ArrayList<>();
    private String content;
    private String fileFullPath;

    @QueryProjection
    public MovieDetailDto(Long id, String title, float star, String content, String fileFullPath) {
        this.id = id;
        this.title = title;
        this.star = star;
        this.content = content;
        this.fileFullPath = fileFullPath;
    }

    public void changeGenreList(List<String> genreList) {
        this.genreList = genreList;
    }
}
