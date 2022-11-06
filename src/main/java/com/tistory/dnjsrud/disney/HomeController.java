package com.tistory.dnjsrud.disney;

import com.tistory.dnjsrud.disney.movie.MovieListDto;
import com.tistory.dnjsrud.disney.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;

    @GetMapping("/")
    public String index() {
        return "redirect:/disney";
    }

    @GetMapping("/disney")
    public String home(Model model) {
        List<MovieListDto> movieListTop = movieService.findMovieListDtoTop(10);

        for (MovieListDto movieListDto : movieListTop) {
            movieListDto.setStar(Float.parseFloat(String.format("%.2f", movieListDto.getStar())));
        }

        model.addAttribute("movieListTop", movieListTop);

        return "disney";
    }

    @GetMapping("/error/denied")
    public String errDenied(Model model) {
        model.addAttribute("msg", "잘못된 접근입니다.");
        return "err/denied";
    }
}
