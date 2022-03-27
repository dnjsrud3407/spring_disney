package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
    Long deleteByMovieId(Long movieId);
}
