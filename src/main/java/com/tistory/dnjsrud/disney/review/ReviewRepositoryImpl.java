package com.tistory.dnjsrud.disney.review;

import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.tistory.dnjsrud.disney.review.QReview.review;

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
                .where(review.user.id.eq(userId), review.isVisible.eq(true))
                .fetchOne();
    }

    @Override
    public Long countByMovieId(Long movieId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.movie.id.eq(movieId), review.isVisible.eq(true))
                .fetchOne();
    }

}
