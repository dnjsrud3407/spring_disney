package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.domain.Movie;
import com.tistory.dnjsrud.disney.domain.MovieGenre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import com.tistory.dnjsrud.disney.repository.MovieGenreRepository;
import com.tistory.dnjsrud.disney.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;

    /**
     * 영화 등록
     * @param movie
     * @return movieId
     */
    public Long saveMovie(Movie movie) {
        movieRepository.save(movie);
        return movie.getId();
    }

    public void saveMovie(String title, LocalDateTime releaseDate, String content, boolean visible, Long genreId) {
        // 무비장르 생성
        Genre genre = genreRepository.findById(genreId).get();
        MovieGenre movieGenre = MovieGenre.createMovieGenre(genre);
        movieGenreRepository.save(movieGenre);

        Movie movie = Movie.createMovie(title, releaseDate, content, visible, movieGenre);
        movieRepository.save(movie);
    }
}
