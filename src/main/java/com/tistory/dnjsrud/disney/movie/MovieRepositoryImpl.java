package com.tistory.dnjsrud.disney.movie;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

import static com.tistory.dnjsrud.disney.genre.QGenre.genre;
import static com.tistory.dnjsrud.disney.movie.QMovie.movie;
import static com.tistory.dnjsrud.disney.moviegenre.QMovieGenre.movieGenre;
import static com.tistory.dnjsrud.disney.poster.QPoster.poster;

public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryImpl(JPAQueryFactory queryFactory) {this.queryFactory = queryFactory;}

    @Override
    public List<MovieListDto> findMovieListDto() {
        return queryFactory
                .select(new QMovieListDto(movie.id, movie.title, movie.star, poster.fileFullPath))
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.visible.eq(true))
                .fetch();
    }

    @Override
    public Optional<MovieDetailDto> findMovieDetailDtoByMovieId(Long movieId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QMovieDetailDto(movie.id, movie.title, movie.star, movie.content, poster.fileFullPath))
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.id.eq(movieId), movie.visible.eq(true))
                .fetchOne()
        );
    }

    @Override
    public List<String> findGenreNameByMovieId(Long movieId) {
        return queryFactory
                .select(genre.genreName)
                .from(movieGenre)
                .leftJoin(movieGenre.genre, genre)
                .where(movieGenre.movie.id.eq(movieId))
                .fetch();
    }
}
