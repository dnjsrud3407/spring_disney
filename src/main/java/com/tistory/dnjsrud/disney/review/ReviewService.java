package com.tistory.dnjsrud.disney.review;

import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.movie.MovieRepository;
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
     * 리뷰 등록 -> 영화 평점 수정
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
            Review review = Review.createReview(star, content, movie.isVisible(), user, movie);
            reviewRepository.save(review);
            
            // 영화 평점 수정
            float avg = (movie.getStar() + review.getStar()) / (reviewRepository.countByMovieId(movieId));
            movie.changeStar(avg);

            return review.getId();
        }
        return null;
    }

    // 리뷰 전체 조회
    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    // 해당 영화 리뷰 전체 조회
    public List<ReviewDetailDto> findReviewDetailDtoList(Long movieId) {
        return reviewRepository.findReviewDetailDtoListByMovieId(movieId);
    }

    // 해당 영화 특정 유저 리뷰 조회
    public ReviewDetailDto findReviewDetailDto(Long movieId, Long userId) {
        return reviewRepository.findReviewDetailDtoByMovieIdAndUserId(movieId, userId).orElse(null);
    }

    // 해당 유저 리뷰 전체 조회
    public List<ReviewUserDto> findReviewUserDto(Long userId) {
        return reviewRepository.findReviewUserDtoByUserId(userId);
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

}
