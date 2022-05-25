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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void validateDuplicateMovie(MovieCreateForm form) {
        Optional<Movie> findMovie = movieRepository.findByTitle(form.getTitle());
        if (findMovie.isPresent()) {
            throw new IllegalStateException(ms.getMessage("movie.titleDuplicate", null, null));
        }
    }


    // 영화 수정
    public Long modifyMovie(MovieModifyForm form) {
        Movie movie = movieRepository.findById(form.getMovieId()).orElse(null);
        if(movie != null) {
            movie.changeTitle(form.getTitle());
            movie.changeReleaseDate(form.getReleaseDate());
            movie.changeContent(form.getContent());
            movie.changeVisible(form.isVisible());

            // 영화포스터 변경
            changePoster(movie, form.getPoster());

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
     * User단 - 영화 전체 조회(숨김 처리 안된 영화 조회)
     * @return List<MovieListDto> MovieListDto : movieId, title, star, genreList
     */
    public List<MovieListDto> findMovieListDto() {
        // Page 처리 필요
        List<MovieListDto> movieListDto = movieRepository.findMovieListDto();
        for (MovieListDto movieDto : movieListDto) {
            List<String> genreList = movieRepository.findGenreNameByMovieId(movieDto.getId());
            movieDto.changeGenreList(genreList);
        }
        return movieListDto;
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
     * 영화 숨기기 처리
     * @param movieId
     * @param visible
     */
    @Transactional
    public void changeVisible(Long movieId, boolean visible) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if(movie != null) {
            movie.changeVisible(visible);
        }
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
     * @param poster
     */
    @Transactional
    public void changePoster(Movie movie, Poster poster) {
        // 포스터 삭제
        posterRepository.deleteById(movie.getPoster().getId());

        // 포스터 생성
        posterRepository.save(poster);

        // 영화포스터 변경
        movie.changePoster(poster);
    }
}
