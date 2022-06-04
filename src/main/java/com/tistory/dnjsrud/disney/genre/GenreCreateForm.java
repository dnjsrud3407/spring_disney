package com.tistory.dnjsrud.disney.genre;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreCreateForm {

    @NotBlank(message = "장르는 필수입니다.")
    private String genreName;
}
