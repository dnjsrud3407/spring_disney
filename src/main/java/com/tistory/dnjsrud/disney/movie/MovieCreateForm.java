package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.poster.Poster;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
public class MovieCreateForm {

    @NotEmpty
    private String title;

    @NotEmpty
    private LocalDateTime releaseDate;

    @NotEmpty
    private String content;

    @NotEmpty
    private ArrayList<Long> genreIds;

    @NotEmpty
    private Poster poster;

    @NotEmpty
    private boolean isVisible;

    public MovieCreateForm(String title, LocalDateTime releaseDate, String content, ArrayList<Long> genreIds, Poster poster, boolean isVisible) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.genreIds = genreIds;
        this.poster = poster;
        this.isVisible = isVisible;
    }
}
