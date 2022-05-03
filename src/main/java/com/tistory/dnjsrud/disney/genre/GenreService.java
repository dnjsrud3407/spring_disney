package com.tistory.dnjsrud.disney.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    /**
     * 장르 등록
     * @param form
     * @return genreId
     */
    @Transactional
    public Long createGenre(GenreCreateForm form) {
        // 장르 이름 중복 체크
        validateDuplicateGenre(form.getGenreName());

        Genre genre = new Genre(form.getGenreName());
        genreRepository.save(genre);
        return genre.getId();
    }

    // 장르 수정
    @Transactional
    public Long modifyGenre(GenreModifyForm form) {
        // 장르 이름 중복 체크
        validateDuplicateGenre(form.getGenreName());

        Genre genre = genreRepository.findById(form.getGenreId()).orElse(null);
        if(genre != null) {
            genre.changeGenreName(form.getGenreName());
            return genre.getId();
        }

        return null;
    }

    private void validateDuplicateGenre(String genreName) {
        Optional<Genre> findGenre = genreRepository.findByGenreName(genreName);
        if (findGenre.isPresent()) {
            throw new IllegalStateException("이비 존재하는 장르명입니다.");
        }
    }

    // 장르 정체 조회
    public List<Genre> findGenres() {
        return genreRepository.findAll();
    }

    // 장르 정보 조회
    public Genre findGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

}
