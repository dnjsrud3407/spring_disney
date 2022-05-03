package com.tistory.dnjsrud.disney.genre;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class GenreModifyForm {

    @NotEmpty
    private Long genreId;

    @NotEmpty
    private String genreName;

    public GenreModifyForm(Long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
