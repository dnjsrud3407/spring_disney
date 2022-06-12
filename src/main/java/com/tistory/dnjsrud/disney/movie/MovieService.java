package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreRepository;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenreRepository;
import com.tistory.dnjsrud.disney.poster.Poster;
import com.tistory.dnjsrud.disney.poster.PosterRepository;
import com.tistory.dnjsrud.disney.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final ReviewRepository reviewRepository;
    private final PosterRepository posterRepository;
    private final MessageSource ms;

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 영화 등록 - 영화장르 생성 후 영화 등록
     * @param form
     * @return movieId
     */
    @Transactional
    public Long createMovie(MovieCreateForm form) throws IOException {
        // 영화 중복 체크
        validateDuplicateMovie(form);

        // 포스터 저장
        Poster poster = null;
        if(form.getFile() != null) {
            poster = Poster.createPoster(form.getFile());
            posterRepository.save(poster);

            String fullPath = fileDir + poster.getStoredFileName();
            form.getFile().transferTo(new File(fullPath));
        }

        // 영화장르 생성
        ArrayList<MovieGenre> movieGenres = new ArrayList<>();
        for (Long genreId : form.getGenreIds()) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if(genre != null) {
                MovieGenre movieGenre = MovieGenre.createMovieGenre(genre);
                movieGenres.add(movieGenre);
            }
        }

        // 영화 생성
        Movie movie = Movie.createMovie(form.getTitle(), form.getReleaseDate(), form.getContent(), form.isVisible(), movieGenres, poster);
        movieRepository.save(movie);

        return movie.getId();
    }

    /**
     * 영화 생성 시 중복 체크
     * - 영화 제목만 체크한다
     * @param form
     */
    private void validateDuplicateMovie(MovieCreateForm form) {
        Optional<Movie> findMovie = movieRepository.findByTitle(form.getTitle());
        if (findMovie.isPresent()) {
            throw new IllegalStateException(ms.getMessage("movie.titleDuplicate", null, null));
        }
    }

    /**
     * 영화 수정 시 중복 체크 
     * - 영화 제목만 체크한다
     * @param form
     */
    private void validateDuplicateMovieModify(MovieModifyForm form) {
        Optional<Movie> findMovie = movieRepository.findByIdNotAndTitle(form.getMovieId(), form.getTitle());
        if (findMovie.isPresent()) {
            throw new IllegalStateException(ms.getMessage("movie.titleDuplicate", null, null));
        }
    }

    // 영화 수정
    @Transactional
    public Long modifyMovie(MovieModifyForm form) throws IOException {
        // 영화 수정 시 중복 체크
        validateDuplicateMovieModify(form);

        Movie movie = movieRepository.findById(form.getMovieId()).orElse(null);
        if(movie != null) {
            movie.changeTitle(form.getTitle());
            movie.changeReleaseDate(form.getReleaseDate());
            movie.changeContent(form.getContent());
            movie.changeVisible(form.isVisible());

            // 영화포스터 변경
            // form 에 포스터 변경할 때만 시행한다
            if(!form.getFile().isEmpty()) {
                changePoster(movie, form.getFile());
            }

            // 영화장르 변경
            changeMovieGenre(movie, form.getGenreIds());
            return movie.getId();
        }
        return null;
    }

    // 영화 전체 조회
    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

    /**
     * Admin 단 - 영화 전체 조회
     * @return List<MovieAdminListDto>
     */
    public List<MovieAdminListDto> findMovieAdminListDto() {
        List<MovieAdminListDto> movieAdminListDto = movieRepository.findMovieAdminListDto();
        for (MovieAdminListDto adminListDto : movieAdminListDto) {
            // 장르 이름 list 구하기
            List<String> genreNameList = movieRepository.findGenreNameByMovieId(adminListDto.getId());
            adminListDto.changeGenreList(genreNameList);

            // 리뷰 Count 구하기
            Long reviewCount = reviewRepository.countByMovieId(adminListDto.getId());
            adminListDto.changeReviewCount(reviewCount);
        }

        return movieAdminListDto;
    }

    /**
     * User단 - 영화 전체 조회(숨김 처리 안된 영화 조회)
     * @return List<MovieListDto> MovieListDto
     */
    public Page<MovieListDto> findMovieListDto(Pageable pageable) {
        // Page 처리 필요
        Page<MovieListDto> result = movieRepository.findMovieListDto(pageable);
        for (MovieListDto movieDto : result.getContent()) {
            // 장르 이름 list 구하기
            List<String> genreList = movieRepository.findGenreNameByMovieId(movieDto.getId());
            movieDto.changeGenreList(genreList);

            // 리뷰 Count 구하기
            Long reviewCount = reviewRepository.countByMovieId(movieDto.getId());
            movieDto.changeReviewCount(reviewCount);
        }
        return result;
    }

    /**
     * User단 - 영화 전체 검색 조회(숨김 처리 안된 영화 조회)
     * @return List<MovieListDto> MovieListDto
     */
    public Page<MovieListDto> searchMovieListDto(Pageable pageable, MovieSearchCondition condition) {
        // Page 처리 필요
        Page<MovieListDto> result = movieRepository.searchMovieListDto(pageable, condition);
        for (MovieListDto movieDto : result.getContent()) {
            // 장르 이름 list 구하기
            List<String> genreList = movieRepository.findGenreNameByMovieId(movieDto.getId());
            movieDto.changeGenreList(genreList);

            // 리뷰 Count 구하기
            Long reviewCount = reviewRepository.countByMovieId(movieDto.getId());
            movieDto.changeReviewCount(reviewCount);
        }
        return result;
    }

    // 영화 정보 조회
    public Movie findMovie(Long movieId) {
        return movieRepository.findById(movieId).orElse(null);
    }

    /**
     * User단 - 영화 정보 조회(숨김 처리 안된 영화 조회)
     * @return MovieDetailDto : movieId, title, star, content, genreList
     */
    public MovieDetailDto findMovieDetailDto(Long movieId) {
        // Page 처리 필요
        MovieDetailDto movieDetailDto = movieRepository.findMovieDetailDtoByMovieId(movieId).orElse(null);
        if(movieDetailDto != null) {
            List<String> genreList = movieRepository.findGenreNameByMovieId(movieId);
            movieDetailDto.changeGenreList(genreList);
        }
        return movieDetailDto;
    }

    /**
     * 영화장르 변경
     * @param movie
     * @param genreIds
     */
    @Transactional
    public void changeMovieGenre(Movie movie, ArrayList<Long> genreIds) {
        // 영화장르 삭제
        movieGenreRepository.deleteByMovieId(movie.getId());

        // 영화장르 생성
        ArrayList<MovieGenre> movieGenres = new ArrayList<>();
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if(genre != null) {
                MovieGenre movieGenre = MovieGenre.createMovieGenre(genre);

                movieGenres.add(movieGenre);
            }
        }

        // 영화장르 변경
        movie.changeMovieGenre(movieGenres);
    }


    /**
     * 영화 포스터 변경
     * @param movie
     * @param file
     */
    @Transactional
    public void changePoster(Movie movie, MultipartFile file) throws IOException {
        // 기존 포스터 찾기
        Poster poster = movie.getPoster();

        // 기존 파일 삭제
        String originalFileFullPath = fileDir + poster.getStoredFileName();
        File originalFile = new File(originalFileFullPath);
        originalFile.delete();

        // 포스터 변경
        poster.changePoster(file);

        // 파일 저장
        String fullPath = fileDir + poster.getStoredFileName();
        file.transferTo(new File(fullPath));
    }
}
