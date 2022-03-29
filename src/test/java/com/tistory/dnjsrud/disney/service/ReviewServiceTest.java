package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.review.Review;
import com.tistory.dnjsrud.disney.review.ReviewService;
import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.movie.MovieRepository;
import com.tistory.dnjsrud.disney.movie.MovieService;
import com.tistory.dnjsrud.disney.review.ReviewRepository;
import com.tistory.dnjsrud.disney.user.UserRepository;
import com.tistory.dnjsrud.disney.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        Review findReview = reviewRepository.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
        assertThat(findReview.getContent()).isEqualTo("졸잼");
        User findUser = userService.findUser(user.getId());
        assertThat(findUser.getReviews().size()).isEqualTo(1);
    }

    @Test
    void changeReview() {
        User user = userRepository.findByLoginId("dnjsrud3407").get(0);
        Movie movie = movieRepository.findByTitle("movie1").orElse(null);
        reviewService.createReview(4.5f, "졸잼", user.getId(), movie.getId());

        em.flush();
        em.clear();
        Review findReview = reviewRepository.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
        findReview.changeReview(3.5f, "그저 그랬어요");

        em.flush();
        em.clear();

        findReview = reviewRepository.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
        assertThat(findReview.getStar()).isEqualTo(3.5f);
        assertThat(findReview.getContent()).isEqualTo("그저 그랬어요");
    }

    @Test
    void deleteReview() {
        User user = userRepository.findByLoginId("dnjsrud3407").get(0);
        Movie movie = movieRepository.findByTitle("movie1").orElse(null);
        reviewService.createReview(4.5f, "졸잼", user.getId(), movie.getId());

        List<Review> list = em.createQuery("select r from Review r", Review.class).getResultList();
        assertThat(list.size()).isEqualTo(1);

        reviewService.deleteReview(user.getId(), movie.getId());
        em.flush();
        em.clear();

        user = userRepository.findByLoginId("dnjsrud3407").get(0);
        assertThat(user.getReviews().size()).isEqualTo(0);
        list = em.createQuery("select r from Review r", Review.class).getResultList();
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void findReviewByMovie() {
        User user = userRepository.findByLoginId("dnjsrud3407").get(0);
        Movie movie = movieRepository.findByTitle("movie1").orElse(null);
        reviewService.createReview(4.5f, "졸잼", user.getId(), movie.getId());

        List<Review> list = reviewService.findReviewByMovie(movie.getId());
        assertThat(list.get(0).getContent()).isEqualTo("졸잼");
    }

    @Test
    void findReviewByUser() {
        User user = userRepository.findByLoginId("dnjsrud3407").get(0);
        Movie movie = movieRepository.findByTitle("movie1").orElse(null);
        reviewService.createReview(4.5f, "졸잼", user.getId(), movie.getId());

        List<Review> list = reviewService.findReviewByUser(user.getId());
        assertThat(list.get(0).getContent()).isEqualTo("졸잼");
    }
}