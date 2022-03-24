package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.Genre;
import com.tistory.dnjsrud.disney.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    /**
     * 장르 등록
     * @param genre
     * @return genreId
     */
    public Long createGenre(Genre genre) {
        // 장르 이름 중복 체크
        validateDuplicateGenre(genre.getGenreName());
        genreRepository.save(genre);
        return genre.getId();
    }

    private void validateDuplicateGenre(String genreName) {
        List<Genre> findGenre = genreRepository.findByGenreName(genreName);
        if (!findGenre.isEmpty()) {
            throw new IllegalStateException("이비 존재하는 장르명입니다.");
        }
    }

    public List<Genre> findGenres() {
        return genreRepository.findAll();
    }

    public Genre findGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }
}
