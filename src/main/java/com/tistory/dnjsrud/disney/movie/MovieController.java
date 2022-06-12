package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.global.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;

    /**
     * User 단 영화 조회하기
     */
    @GetMapping("/list")
    public String movieList(@ModelAttribute MovieSearchCondition condition, Model model,
                            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<MovieListDto> result;
        if(condition.getTitle() != null || condition.getGenreId() != null) {     // 검색 조건 있을 경우
            result = movieService.searchMovieListDto(pageable, condition);
            log.info("genreId : ", condition.getGenreId());
        } else {
            result = movieService.findMovieListDto(pageable);
        }

        List<Genre> genreList = genreService.findGenres();
        model.addAttribute("genreList", genreList);

        model.addAttribute("condition", condition);
        model.addAttribute("movieList", result.getContent());

        MyPage page = new MyPage(result);
        model.addAttribute("page", page);

        return "movie/list";
    }

    @GetMapping("/{movieId}")
    @ResponseBody
    public String movieDetail(@PathVariable Long movieId, Model model) {
        MovieDetailDto movie = movieService.findMovieDetailDto(movieId);


        return "movie/detail";
    }
}
