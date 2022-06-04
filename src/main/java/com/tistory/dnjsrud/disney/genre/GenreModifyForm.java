package com.tistory.dnjsrud.disney.genre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class GenreModifyForm {

    @NotNull
    private Long genreId;

    @NotBlank
    private String genreName;

    public GenreModifyForm(Long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
