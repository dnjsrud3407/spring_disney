package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenre;
import com.tistory.dnjsrud.disney.genre.GenreRepository;
import com.tistory.dnjsrud.disney.moviegenre.MovieGenreRepository;
import com.tistory.dnjsrud.disney.poster.Poster;
import com.tistory.dnjsrud.disney.poster.PosterRepository;
import com.tistory.dnjsrud.disney.review.Review;
import com.tistory.dnjsrud.disney.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final ReviewRepository reviewRepository;
    private final PosterRepository posterRepository;

    /**
     * 영화 등록 - 영화장르 생성 후 영화 등록
     * @param form
     * @return movieId
     */
    @Transactional
    public Long createMovie(MovieCreateForm form) {
        // 포스터 저장
        Poster poster = form.getPoster();
        if(poster != null) {
            posterRepository.save(poster);
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
     * @param isVisible
     */
    @Transactional
    public void changeVisible(Long movieId, boolean isVisible) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if(movie != null) {
            movie.changeVisible(isVisible);
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
