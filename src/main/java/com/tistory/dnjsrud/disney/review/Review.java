package com.tistory.dnjsrud.disney.review;

import com.sun.istack.NotNull;
import com.tistory.dnjsrud.disney.global.BaseEntity;
import com.tistory.dnjsrud.disney.movie.Movie;
import com.tistory.dnjsrud.disney.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @NotNull
    private float star;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Review(float star, String content) {
        this.star = star;
        this.content = content;
    }

    //== 연관관계 메서드 ==//
    public void setUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getReviews().add(this);
    }

    //== 생성 메서드 ==//
    public static Review createReview(float star, String content, User user, Movie movie) {
        Review review = new Review(star, content);
        review.setUser(user);
        review.setMovie(movie);
        return review;
    }

    //== 비즈니스 로직 ==//
    public void changeReview(float star, String content) {
        this.star = star;
        this.content = content;
    }

}
