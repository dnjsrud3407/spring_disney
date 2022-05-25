package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.poster.Poster;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Getter
public class MovieModifyForm {

    @NotEmpty
    private Long movieId;

    @NotEmpty
    private String title;

    @NotEmpty
    private Date releaseDate;

    @NotEmpty
    private String content;

    @NotEmpty
    private ArrayList<Long> genreIds;

    @NotEmpty
    private Poster poster;

    @NotEmpty
    private boolean visible;

    public MovieModifyForm(Long movieId, String title, Date releaseDate, String content, ArrayList<Long> genreIds, Poster poster, boolean visible) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.genreIds = genreIds;
        this.poster = poster;
        this.visible = visible;
    }
}
