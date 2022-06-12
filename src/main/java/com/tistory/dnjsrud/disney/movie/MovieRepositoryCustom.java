package com.tistory.dnjsrud.disney.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MovieRepositoryCustom {
    Page<MovieListDto> findMovieListDto(Pageable pageable);
    Page<MovieListDto> searchMovieListDto(Pageable pageable, MovieSearchCondition condition);
    Optional<MovieDetailDto> findMovieDetailDtoByMovieId(Long movieId);
    List<String> findGenreNameByMovieId(Long movieId);
    List<MovieAdminListDto> findMovieAdminListDto();
}
