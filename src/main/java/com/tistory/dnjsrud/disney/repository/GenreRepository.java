package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
