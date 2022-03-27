package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.domain.Movie;
import com.tistory.dnjsrud.disney.domain.Review;
import com.tistory.dnjsrud.disney.domain.User;
import com.tistory.dnjsrud.disney.repository.MovieRepository;
import com.tistory.dnjsrud.disney.repository.ReviewRepository;
import com.tistory.dnjsrud.disney.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void beforeAll() {
        Genre genre = new Genre("공포", true);
        Genre genre2 = new Genre("연애", true);
        genreService.createGenre(genre);
        genreService.createGenre(genre2);

        ArrayList<Long> genreIds = new ArrayList<>();
        genreIds.add(1L);
        genreIds.add(2L);
        Long createMovieId = movieService.createMovie("movie1", LocalDateTime.now(), "movie1 내용입니다.", true, genreIds);

        User user = new User("dnjsrud3407", "1234", "wk", "dnjsrud3407@naver.com");
        userService.join(user);
    }

    @Test
    void createReview() {
        // Long createReview(float star, String content, Long userId, Long movieId)
        User user = userRepository.findByLoginId("dnjsrud3407").get(0);
        Movie movie = movieRepository.findByTitle("movie1").orElse(null);
        reviewService.createReview(4.5f, "졸잼", user.getId(), movie.getId());

        em.flush();
        em.clear();
        Review findReview = reviewRepository.findReviewByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
        assertThat(findReview.getContent()).isEqualTo("졸잼");
        User findUser = userService.findUser(user.getId());
        assertThat(findUser.getReviews().size()).isEqualTo(1);
    }

    @Test
    void changeReview() {
    }

    @Test
    void deleteReview() {
    }

    @Test
    void findReviewByMovie() {
    }

    @Test
    void findReviewByUser() {
    }
}