package com.tistory.dnjsrud.disney.movie;

import com.sun.istack.NotNull;
import com.tistory.dnjsrud.disney.global.BaseEntity;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.poster.Poster;
import com.tistory.dnjsrud.disney.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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
    private Date releaseDate;

    @NotNull
    @Column(length = 4000)
    private String content;

    @NotNull
    private float star;

    @NotNull
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieGenre> movieGenres = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews = new ArrayList<>();

    @NotNull
    private boolean visible;

    private Movie(String title, Date releaseDate, String content, boolean visible, Poster poster) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.content = content;
        this.visible = visible;
        this.poster = poster;
    }

    //== 연관관계 메서드 ==//
    public void addMovieGenre(MovieGenre movieGenre) {
        movieGenres.add(movieGenre);
        movieGenre.setMovie(this);
    }

    //== 생성 메서드 ==//
    public static Movie createMovie(String title, Date releaseDate, String content,
                                    boolean visible, ArrayList<MovieGenre> movieGenres, Poster poster) {
        Movie movie = new Movie(title, releaseDate, content, visible, poster);
        for (MovieGenre movieGenre : movieGenres) {
            movie.addMovieGenre(movieGenre);
        }
        return movie;
    }

    //== 비즈니스 로직 ==//
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changePoster(Poster poster) {
        this.poster = poster;
    }

    public void changeStar(float star) {
        this.star = star;
    }

    public void changeVisible(boolean visible) {
        this.visible = visible;
    }

    public void changeMovieGenre(ArrayList<MovieGenre> movieGenres) {
        this.movieGenres.clear();
        this.movieGenres = new ArrayList<>();
        for (MovieGenre movieGenre : movieGenres) {
            this.addMovieGenre(movieGenre);
        }
    }
}
