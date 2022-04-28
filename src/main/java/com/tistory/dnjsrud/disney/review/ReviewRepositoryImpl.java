package com.tistory.dnjsrud.disney.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tistory.dnjsrud.disney.movie.QMovie;
import com.tistory.dnjsrud.disney.poster.QPoster;
import com.tistory.dnjsrud.disney.user.QUser;

import java.util.List;
import java.util.Optional;

import static com.tistory.dnjsrud.disney.movie.QMovie.movie;
import static com.tistory.dnjsrud.disney.poster.QPoster.poster;
import static com.tistory.dnjsrud.disney.review.QReview.review;
import static com.tistory.dnjsrud.disney.user.QUser.user;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countByUserId(Long userId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .join(review.movie, movie).on(review.user.id.eq(userId), movie.isVisible.eq(true))
                .fetchOne();
    }

    @Override
    public Long countByMovieId(Long movieId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.movie.id.eq(movieId))
                .fetchOne();
    }

    @Override
    public List<ReviewDetailDto> findReviewDetailDtoListByMovieId(Long movieId) {
        return queryFactory
                .select(new QReviewDetailDto(review.id, review.star, review.content, user.nickname))
                .from(movie)
                .join(movie.reviews, review).on(movie.id.eq(movieId), movie.isVisible.eq(true))
                .join(review.user, user)
                .fetch();
    }

    @Override
    public Optional<ReviewDetailDto> findReviewDetailDtoByMovieIdAndUserId(Long movieId, Long userId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QReviewDetailDto(review.id, review.star, review.content, user.nickname))
                .from(movie)
                .join(movie.reviews, review).on(movie.id.eq(movieId), review.user.id.eq(userId))
                .join(review.user, user)
                .fetchOne()
        );
    }

    @Override
    public List<ReviewUserDto> findReviewUserDtoByUserId(Long userId) {
        return queryFactory
                .select(new QReviewUserDto(movie.id, poster.fileFullPath, review.star, review.content))
                .from(review)
                .join(review.movie, movie).on(review.user.id.eq(userId), movie.isVisible.eq(true))
                .join(movie.poster, poster)
                .fetch();
    }

}
