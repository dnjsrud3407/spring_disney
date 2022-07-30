package com.tistory.dnjsrud.disney.genre;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreCreateForm {

    @NotBlank(message = "장르는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String genreName;
}
