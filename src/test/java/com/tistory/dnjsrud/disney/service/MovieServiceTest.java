package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.domain.Movie;
import com.tistory.dnjsrud.disney.domain.MovieGenre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import com.tistory.dnjsrud.disney.repository.MovieGenreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        genreService.createGenre(genre);
        genreService.createGenre(genre2);
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
}