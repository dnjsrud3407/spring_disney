package com.tistory.dnjsrud.disney.genre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class GenreCreateForm {

    @NotBlank(message = "장르는 필수입니다.")
    private String genreName;

    public GenreCreateForm(String genreName) {
        this.genreName = genreName;
    }
}
