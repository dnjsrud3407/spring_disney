package com.tistory.dnjsrud.disney.genre;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
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

    @NotBlank(message = "장르는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String genreName;

    public GenreModifyForm(Long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
