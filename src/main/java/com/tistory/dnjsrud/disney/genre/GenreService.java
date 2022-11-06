package com.tistory.dnjsrud.disney.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final MessageSource ms;

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
        validateDuplicateGenreModify(form.getGenreId(), form.getGenreName());

        Genre genre = genreRepository.findById(form.getGenreId()).orElse(null);
        if(genre != null) {
            genre.changeGenreName(form.getGenreName());
            return genre.getId();
        }

        return null;
    }

    /**
     * 장르 생성 시 중복 체크
     * - 장르 이름만 체크한다
     * @param genreName
     */
    private void validateDuplicateGenre(String genreName) {
        Optional<Genre> findGenre = genreRepository.findByGenreName(genreName);
        if (findGenre.isPresent()) {
            throw new IllegalStateException(ms.getMessage("genre.genreNameDuplicate", null, null));
        }
    }

    /**
     * 장르 수정 시 중복 체크
     * - 장르 이름만 체크한다
     * @param genreName
     */
    private void validateDuplicateGenreModify(Long id, String genreName) {
        Optional<Genre> findGenre = genreRepository.findByIdNotAndGenreName(id, genreName);
        if (findGenre.isPresent()) {
            throw new IllegalStateException(ms.getMessage("genre.genreNameDuplicate", null, null));
        }
    }

    // 장르 전체 조회
    public List<Genre> findGenres() {
        return genreRepository.findAll();
    }

    /**
     * Admin 단 - 장르 전체 조회
     * @return
     */
    public Page<Genre> findGenres(Pageable pageable) {
        return genreRepository.findAllByOrderByIdDesc(pageable);
    }

    // 장르 정보 조회
    public Genre findGenre(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

}
