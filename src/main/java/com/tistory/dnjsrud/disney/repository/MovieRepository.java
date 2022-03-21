package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
