package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByGenreName(String genreName);
}
