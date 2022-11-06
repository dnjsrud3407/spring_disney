package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class MovieCreateForm {

    @NotBlank(message = "제목은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String title;

    @NotNull(message = "개봉일은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @NotBlank(message = "줄거리는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String content;

    @NotEmpty(message = "장르는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private ArrayList<Long> genreIds;

    private MultipartFile file;

    private boolean visible;

    public MovieCreateForm(String title, Date releaseDate, String content, ArrayList<Long> genreIds, MultipartFile file, boolean visible) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.genreIds = genreIds;
        this.file = file;
        this.visible = visible;
    }
}
