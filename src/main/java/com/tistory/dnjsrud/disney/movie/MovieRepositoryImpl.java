package com.tistory.dnjsrud.disney.movie;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
    public Page<MovieListDto> findMovieListDto(Pageable pageable) {
        List<MovieListDto> content = queryFactory
                .select(new QMovieListDto(movie.id, movie.title, movie.releaseDate, movie.star, poster.storedFileName))
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.visible.eq(true))
                .orderBy(movie.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(movie.count())
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.visible.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MovieListDto> searchMovieListDto(Pageable pageable, MovieSearchCondition condition) {
        List<MovieListDto> content = queryFactory
                .select(new QMovieListDto(movie.id, movie.title, movie.releaseDate, movie.star, poster.storedFileName)).distinct()
                .from(movie)
                .join(movie.poster, poster)
                .join(movie.movieGenres, movieGenre)
                .join(movieGenre.genre, genre)
                .where(movie.visible.eq(true),
                        movieTitleLike(condition.getTitle()),
                        genreIdEq(condition.getGenreId()))
                .orderBy(movie.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(movie.countDistinct())
                .from(movie)
                .join(movie.poster, poster)
                .join(movie.movieGenres, movieGenre)
                .join(movieGenre.genre, genre)
                .where(movie.visible.eq(true),
                        movieTitleLike(condition.getTitle()),
                        genreIdEq(condition.getGenreId()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate movieTitleLike(String title) {
        // where 에 null이 들어가면 조건문에서 무시한다
        return title == null ? null : movie.title.contains(title);
    }

    private Predicate genreIdEq(Long genreId) {
        return genreId == null ? null : genre.id.eq(genreId);
    }

    @Override
    public Optional<MovieDetailDto> findMovieDetailDtoByMovieId(Long movieId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QMovieDetailDto(movie.id, movie.title, movie.releaseDate, movie.star, movie.content, poster.storedFileName))
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.id.eq(movieId), movie.visible.eq(true))
                .fetchOne()
        );
    }

    @Override
    public Page<MovieAdminListDto> findMovieAdminListDto(Pageable pageable) {
        List<MovieAdminListDto> content = queryFactory
                .select(new QMovieAdminListDto(movie.id, movie.title, movie.releaseDate, movie.star, poster.storedFileName, movie.visible))
                .from(movie)
                .join(movie.poster, poster)
                .orderBy(movie.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(movie.count())
                .from(movie)
                .join(movie.poster, poster);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MovieAdminListDto> findMovieAdminListDto(Pageable pageable, MovieSearchCondition condition) {
        List<MovieAdminListDto> content = queryFactory
                .select(new QMovieAdminListDto(movie.id, movie.title, movie.releaseDate, movie.star, poster.storedFileName, movie.visible)).distinct()
                .from(movie)
                .join(movie.poster, poster)
                .join(movie.movieGenres, movieGenre)
                .join(movieGenre.genre, genre)
                .where(movieTitleLike(condition.getTitle()),
                        genreIdEq(condition.getGenreId()))
                .orderBy(movie.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(movie.count())
                .from(movie)
                .join(movie.poster, poster)
                .where(movieTitleLike(condition.getTitle()),
                        genreIdEq(condition.getGenreId()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<MovieListDto> findMovieListDtoTop(int num) {
        return queryFactory
                .select(new QMovieListDto(movie.id, movie.title, movie.releaseDate, movie.star, poster.storedFileName))
                .from(movie)
                .join(movie.poster, poster)
                .where(movie.visible.eq(true))
                .orderBy(movie.star.desc(), movie.id.desc())
                .limit(num)
                .fetch();
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
