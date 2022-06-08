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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String movieList(Model model,
                            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<MovieListDto> result = movieService.findMovieListDto(pageable);
        MyPage page = new MyPage(result);
        log.info("page : {}" + page);
        List<Genre> genreList = genreService.findGenres();
        model.addAttribute("genreList", genreList);
        model.addAttribute("movieList", result.getContent());
        model.addAttribute("page", page);

        return "movie/list";
    }
}
