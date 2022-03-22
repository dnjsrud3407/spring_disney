package com.tistory.dnjsrud.disney.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieGenre {

    @Id @GeneratedValue
    @Column(name = "movie_genre_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    private void setGenre(Genre genre) {
        this.genre = genre;
    }

    //== 생성 메서드 ==//
    public static MovieGenre createMovieGenre(Genre genre) {
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setGenre(genre);
        return movieGenre;
    }
}
