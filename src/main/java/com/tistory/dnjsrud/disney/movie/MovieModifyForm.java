package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.poster.Poster;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieModifyForm {

    @NotNull
    private Long movieId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "개봉일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @NotBlank(message = "줄거리는 필수입니다.")
    private String content;

    @NotEmpty(message = "장르는 필수입니다.")
    private ArrayList<Long> genreIds = new ArrayList<>();

    private String originalFileName;

    private MultipartFile file;

    private boolean visible;

    public void changeGenreIds(List<MovieGenre> movieGenres) {
        for (MovieGenre movieGenre : movieGenres) {
            this.genreIds.add(movieGenre.getGenre().getId());
        }
    }
}
