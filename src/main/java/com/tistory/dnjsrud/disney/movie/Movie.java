package com.tistory.dnjsrud.disney.movie;

import com.sun.istack.NotNull;
import com.tistory.dnjsrud.disney.global.BaseEntity;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.review.Review;
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
public class Movie extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime releaseDate;

    @NotNull
    private String content;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieGenre> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews = new ArrayList<>();

    @NotNull
    private boolean isVisible;

    private Movie(String title, LocalDateTime releaseDate, String content, boolean isVisible) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.isVisible = isVisible;
    }

    //== 연관관계 메서드 ==//
    public void addMovieGenre(MovieGenre movieGenre) {
        movieGenres.add(movieGenre);
        movieGenre.setMovie(this);
    }

    //== 생성 메서드 ==//
    public static Movie createMovie(String title, LocalDateTime releaseDate, String content,
                                    boolean isVisible, ArrayList<MovieGenre> movieGenres) {
        Movie movie = new Movie(title, releaseDate, content, isVisible);
        for (MovieGenre movieGenre : movieGenres) {
            movie.addMovieGenre(movieGenre);
        }
        return movie;
    }

    //== 비즈니스 로직 ==//
    public void changeVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void changeMovieGenre(ArrayList<MovieGenre> movieGenres) {
        this.movieGenres.clear();
        this.movieGenres = new ArrayList<>();
        for (MovieGenre movieGenre : movieGenres) {
            this.addMovieGenre(movieGenre);
        }
    }
}
