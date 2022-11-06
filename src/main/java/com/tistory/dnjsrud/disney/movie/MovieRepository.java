package com.tistory.dnjsrud.disney.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {
    Optional<Movie> findByTitle(String title);
    Optional<Movie> findByIdNotAndTitle(Long id, String title);
}
