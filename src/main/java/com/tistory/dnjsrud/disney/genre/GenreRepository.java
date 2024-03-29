package com.tistory.dnjsrud.disney.genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String genreName);
    Optional<Genre> findByIdNotAndGenreName(Long id, String genreName);
    Page<Genre> findAllByOrderByIdDesc(Pageable pageable);
}
