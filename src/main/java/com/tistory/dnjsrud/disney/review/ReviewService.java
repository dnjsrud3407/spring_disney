package com.tistory.dnjsrud.disney.review;

import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.review.Review;
import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.movie.MovieRepository;
import com.tistory.dnjsrud.disney.review.ReviewRepository;
import com.tistory.dnjsrud.disney.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
     * 리뷰 등록
     * @param star
     * @param content
     * @param userId
     * @param movieId
     * @return
     */
    @Transactional
    public Long createReview(float star, String content, Long userId, Long movieId) {
        User user = userRepository.findById(userId).orElse(null);
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (user != null && movie != null) {
            Review review = Review.createReview(star, content, user, movie);
            reviewRepository.save(review);
            return review.getId();
        }
        return null;
    }

    // 리뷰 전체 조회
    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    // 리뷰 수정
    @Transactional
    public void changeReview(float star, String content, Long userId, Long movieId) {
        User user = userRepository.findById(userId).orElse(null);
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (user != null && movie != null) {
            Review review = reviewRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
            if (review != null) {
                review.changeReview(star, content);
            }
        }
    }

    // 리뷰 삭제
    @Transactional
    public Long deleteReview(Long userId, Long movieId) {
        return reviewRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    // 해당 영화 리뷰 조회
    public List<Review> findReviewByMovie(Long movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    // 해당 유저 리뷰 조회
    public List<Review> findReviewByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
