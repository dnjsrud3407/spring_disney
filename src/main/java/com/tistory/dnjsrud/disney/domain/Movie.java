package com.tistory.dnjsrud.disney.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime releaseDate;

    @NotNull
    private String content;

    @OneToMany(mappedBy = "movie")
    private List<MovieGenre> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews = new ArrayList<>();

    @NotNull
    private boolean visible;

    //== 연관관계 메서드 ==//
    public void addMovieGenre(MovieGenre movieGenre) {
        movieGenres.add(movieGenre);
        movieGenre.setMovie(this);
    }

    //== 생성 메서드 ==//
    public static Movie createMovie(String title, LocalDateTime releaseDate, String content, boolean visible, MovieGenre... movieGenres) {
        Movie movie = new Movie(title, releaseDate, content, visible);
        for (MovieGenre movieGenre : movieGenres) {
            movie.addMovieGenre(movieGenre);
        }
        return movie;
    }

    private Movie(String title, LocalDateTime releaseDate, String content, boolean visible) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.visible = visible;
    }
}
