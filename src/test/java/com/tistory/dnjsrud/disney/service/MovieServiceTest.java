package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.movie.MovieService;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void beforeAll() {
        Genre genre = new Genre("공포", true);
        Genre genre2 = new Genre("연애", true);
        Genre genre3 = new Genre("액션", true);
        Genre genre4 = new Genre("애니메이션", true);
        genreService.createGenre(genre);
        genreService.createGenre(genre2);
        genreService.createGenre(genre3);
        genreService.createGenre(genre4);
    }

    @Test
    public void 영화등록() throws Exception {
        // given
        ArrayList<Long> genreIds = new ArrayList<>();
        genreIds.add(1L);
        genreIds.add(2L);

        // when
        // MovieDTO -> Movie 로 변환 만들기
        Long createMovieId = movieService.createMovie("movie1", LocalDateTime.now(), "movie1 내용입니다.", true, genreIds);

        // then
        em.flush();
        em.clear();
        Movie findMovie = movieService.findMovie(createMovieId);
        assertThat(findMovie.getMovieGenres().size()).isEqualTo(2);
        assertThat(findMovie.getMovieGenres().get(0).getGenre().getGenreName()).isEqualTo("공포");
    }

    @Test
    public void 숨김처리() throws Exception {
        // given
        ArrayList<Long> genreIds = new ArrayList<>();
        genreIds.add(1L);
        genreIds.add(2L);

        // when
        // MovieDTO -> Movie 로 변환 만들기
        Long createMovieId = movieService.createMovie("movie1", LocalDateTime.now(), "movie1 내용입니다.", true, genreIds);

        // then
        Movie findMovie = movieService.findMovie(createMovieId);
        findMovie.changeVisible(false);
        em.flush();
        em.clear();
        findMovie = movieService.findMovie(createMovieId);
        assertThat(findMovie.isVisible()).isEqualTo(false);
    }

    @Test
    @Rollback(value = false)
    public void 장르변경() throws Exception {
        // given
        ArrayList<Long> genreIds = new ArrayList<>();
        genreIds.add(1L);
        genreIds.add(2L);
        Long createMovieId = movieService.createMovie("movie1", LocalDateTime.now(), "movie1 내용입니다.", true, genreIds);

        // when
        genreIds.clear();
        genreIds.add(3l);
        genreIds.add(4l);
        Movie findMovie = movieService.findMovie(createMovieId);
        movieService.changeMovieGenre(findMovie.getId(), genreIds);

        em.flush();
        em.clear();

        // then
        findMovie = movieService.findMovie(createMovieId);
        assertThat(findMovie.getMovieGenres().get(0).getGenre().getGenreName()).isEqualTo("액션");
    }
}