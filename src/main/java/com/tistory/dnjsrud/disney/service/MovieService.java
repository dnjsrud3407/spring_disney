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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;

    /**
     * 영화 등록 - 영화장르 생성 후 영화 등록
     * @param title, releaseDate, content, visible
     * @return movieId
     */
    @Transactional
    public Long createMovie(String title, LocalDateTime releaseDate, String content, boolean visible, ArrayList<Long> genreIds) {
        // 영화장르 생성
        ArrayList<MovieGenre> movieGenres = new ArrayList<>();
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if(genre != null) {
                MovieGenre movieGenre = MovieGenre.createMovieGenre(genre);

                movieGenres.add(movieGenre);
            }
        }

        // 영화 생성
        Movie movie = Movie.createMovie(title, releaseDate, content, visible, movieGenres);
        movieRepository.save(movie);
        return movie.getId();
    }

    // 영화 전체 조회
    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

    // 영화 정보 조회
    public Movie findMovie(Long movieId) {
        return movieRepository.findById(movieId).orElse(null);
    }

    /**
     * 영화 숨기기 처리
     * @param movieId
     * @param isVisible
     */
    @Transactional
    public void changeVisible(Long movieId, boolean isVisible) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if(movie != null) {
            movie.changeVisible(isVisible);
        }
    }

    /**
     * 영화장르 변경
     * @param movieId
     * @param genreIds
     */
    @Transactional
    public void changeMovieGenre(Long movieId, ArrayList<Long> genreIds) {
        // 영화장르 삭제
        movieGenreRepository.deleteByMovieId(movieId);

        // 영화장르 생성
        ArrayList<MovieGenre> movieGenres = new ArrayList<>();
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if(genre != null) {
                MovieGenre movieGenre = MovieGenre.createMovieGenre(genre);

                movieGenres.add(movieGenre);
            }
        }

        // 영화장르 변경
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            movie.changeMovieGenre(movieGenres);
        }
    }
}
