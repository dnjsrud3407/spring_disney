package com.tistory.dnjsrud.disney.moviegenre;

import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
    Long deleteByMovieId(Long movieId);
}
