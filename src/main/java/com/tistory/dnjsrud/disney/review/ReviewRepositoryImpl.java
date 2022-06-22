package com.tistory.dnjsrud.disney.review;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
                .join(review.movie, movie).on(review.user.id.eq(userId), movie.visible.eq(true))
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
    public Page<ReviewDetailDto> findReviewDetailDtoListByMovieId(Pageable pageable, Long movieId) {
        List<ReviewDetailDto> content = queryFactory
                .select(new QReviewDetailDto(review.id, review.star, review.content, user.nickname))
                .from(movie)
                .join(movie.reviews, review).on(movie.id.eq(movieId), movie.visible.eq(true))
                .join(review.user, user)
                .orderBy(review.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(movie)
                .join(movie.reviews, review).on(movie.id.eq(movieId), movie.visible.eq(true))
                .join(review.user, user);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ReviewDetailDto> findReviewDetailDtoListByMovieIdNotUserId(Long movieId, Long userId) {
        return queryFactory
                .select(new QReviewDetailDto(review.id, review.star, review.content, user.nickname))
                .from(movie)
                .join(movie.reviews, review).on(movie.id.eq(movieId), movie.visible.eq(true))
                .join(review.user, user).on(user.id.ne(userId))
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
                .select(new QReviewUserDto(movie.id, poster.storedFileName, review.star, review.content))
                .from(review)
                .join(review.movie, movie).on(review.user.id.eq(userId), movie.visible.eq(true))
                .join(movie.poster, poster)
                .fetch();
    }

}
