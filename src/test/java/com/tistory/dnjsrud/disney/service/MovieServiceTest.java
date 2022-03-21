package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.domain.Movie;
import com.tistory.dnjsrud.disney.domain.MovieGenre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import com.tistory.dnjsrud.disney.repository.MovieGenreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Test
    public void 영화등록() throws Exception {
        // given
        Genre genre = genreRepository.findById(1L).get();

        // when
        // MovieDTO -> Movie 로 변환 만들기
        movieService.saveMovie("movie1", LocalDateTime.now(), "movie1 내용입니다.", true,
                genre.getId());

        // then
        Assertions.assertThat(movieGenreRepository.count()).isEqualTo(1);
    }
}