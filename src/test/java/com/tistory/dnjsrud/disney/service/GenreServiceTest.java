package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void 장르등록() throws Exception {
        // given
        Genre genre1 = new Genre("호러", true);
        Genre genre2 = new Genre("연애", true);

        // when
        genreService.saveGenre(genre1);
        genreService.saveGenre(genre2);

        // then
        Assertions.assertThat(genreRepository.count()).isEqualTo(2);
    }
}