package com.tistory.dnjsrud.disney.movie;

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

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "개봉일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @NotBlank(message = "줄거리는 필수입니다.")
    private String content;

    @NotEmpty(message = "장르는 필수입니다.")
    private ArrayList<Long> genreIds;

    @NotNull(message = "포스터는 필수입니다.")
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
