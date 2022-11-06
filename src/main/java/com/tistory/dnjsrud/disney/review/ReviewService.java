package com.tistory.dnjsrud.disney.review;

import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.movie.MovieRepository;
import com.tistory.dnjsrud.disney.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 등록 -> 영화 평점 수정
     * @param form
     * @return
     */
    @Transactional
    public Long createReview(ReviewCreateForm form) {
        User user = userRepository.findById(form.getUserId()).orElse(null);
        Movie movie = movieRepository.findById(form.getMovieId()).orElse(null);
        if (user != null && movie != null) {
            Review review = Review.createReview(form.getStar(), form.getContent(), movie.isVisible(), user, movie);
            reviewRepository.save(review);
            
            // 영화 평점 수정
            int userCnt = Math.toIntExact(reviewRepository.countByMovieId(form.getMovieId()));
            float avg = ((movie.getStar() * (userCnt - 1)) + review.getStar()) / userCnt;
            movie.changeStar(avg);

            return review.getId();
        }
        return null;
    }

    // 리뷰 수정
    @Transactional
    public Long modifyReview(ReviewModifyForm form) {
        Review review = reviewRepository.findById(form.getReviewId()).orElse(null);
        if(review != null) {
            review.changeContent(form.getContent());

            // 영화 평점 수정
            Movie movie = movieRepository.findById(form.getMovieId()).orElse(null);
            int userCnt = Math.toIntExact(reviewRepository.countByMovieId(form.getMovieId()));
            float avg = (((movie.getStar() * userCnt) - review.getStar()) + form.getStar()) / userCnt;
            movie.changeStar(avg);
            review.changeStar(form.getStar());

            return review.getId();
        }
        return null;
    }

    // 리뷰 전체 조회
    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    // 해당 영화 리뷰 전체 조회
    public Page<ReviewDetailDto> findReviewDetailDtoList(Pageable pageable, Long movieId) {
        return reviewRepository.findReviewDetailDtoListByMovieId(pageable, movieId);
    }

    // 해당 영화 리뷰 전체 조회(로그인 유저 제외)
    public Page<ReviewDetailDto> findReviewDetailDtoListWithoutUser(Pageable pageable, Long movieId, Long userId) {
        return reviewRepository.findReviewDetailDtoListByMovieIdNotUserId(pageable, movieId, userId);
    }

    // 해당 영화 특정 유저 리뷰 조회
    public ReviewDetailDto findReviewDetailDto(Long movieId, Long userId) {
        return reviewRepository.findReviewDetailDtoByMovieIdAndUserId(movieId, userId).orElse(null);
    }

    // 해당 유저 리뷰 전체 조회
    public Page<ReviewUserDto> findReviewUserDto(Pageable pageable, Long userId) {
        return reviewRepository.findReviewUserDtoByUserId(pageable, userId);
    }

    // 리뷰 삭제
    @Transactional
    public Long deleteReview(Long userId, Long movieId) {
        return reviewRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

}
