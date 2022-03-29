package com.tistory.dnjsrud.disney.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);  // 임시로 적음,,, 나중에 like 로 바꾸자 List<Movie>로도
}
