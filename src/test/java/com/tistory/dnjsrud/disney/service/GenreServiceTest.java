package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

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
    public void 장르등록() throws Exception {
        // given
        Genre genre = new Genre("호러", true);

        // when
        genreService.createGenre(genre);

        // then
        Genre findGenre = genreService.findGenre(genre.getId());
        org.junit.jupiter.api.Assertions.assertEquals(genre, findGenre);
    }

    @Test
    public void 중복_장르_예외() throws Exception {
        // given
        Genre genre1 = new Genre("호러", true);
        Genre genre2 = new Genre("호러", true);

        // when
        genreService.createGenre(genre1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            genreService.createGenre(genre2);
        });

        // then

    }

    @Test
    public void 숨김처리() throws Exception {
        // given
        Genre genre = genreRepository.findById(1L).get();
        if (genre.isVisible()) {
            genre.changeVisible(false);
        }

        em.flush();
        em.clear();

        // when

        // then
        genre = genreRepository.findById(1L).get();
        Assertions.assertThat(genre.isVisible()).isEqualTo(false);
    }
}