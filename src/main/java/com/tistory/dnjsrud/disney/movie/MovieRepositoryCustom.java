package com.tistory.dnjsrud.disney.movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepositoryCustom {
    List<MovieListDto> findMovieListDto();
    Optional<MovieDetailDto> findMovieDetailDtoByMovieId(Long movieId);
    List<String> findGenreNameByMovieId(Long movieId);
}
