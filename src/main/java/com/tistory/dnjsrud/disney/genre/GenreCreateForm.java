package com.tistory.dnjsrud.disney.genre;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class GenreCreateForm {

    @NotEmpty
    private String genreName;

    public GenreCreateForm(String genreName) {
        this.genreName = genreName;
    }
}
