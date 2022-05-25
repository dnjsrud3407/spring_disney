package com.tistory.dnjsrud.disney.admin;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreCreateForm;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.movie.MovieCreateForm;
import com.tistory.dnjsrud.disney.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;

    private final GenreService genreService;

    private final MessageSource ms;

    @GetMapping("/movie/create")
    public String createMovie(@ModelAttribute MovieCreateForm movieCreateForm, Model model) {
        List<Genre> genres = genreService.findGenres();
        model.addAttribute("genres", genres);
        return "admin/movie/createMovie";
    }

    @PostMapping("/movie/create")
    public String createMovie(@Valid MovieCreateForm movieCreateForm, BindingResult result, Model model) throws IOException {
        log.info("movieCreateForm : {}", movieCreateForm);

        // =========================================================
        // 1. Validation movieCreateForm
        //   -1. file 값이 없을 경우
        if(movieCreateForm.getFile().isEmpty()) {
            String fileEmpty = ms.getMessage("movie.fileEmpty", null, null);
            result.addError(new FieldError("movieCreateForm", "file", fileEmpty));
        }

        //   -2. movieCreateForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            // releaseDate가 dateTimeFormat pattern이 안 맞는 경우
            if(movieCreateForm.getReleaseDate() == null && result.getFieldError("releaseDate").getRejectedValue() != null) {
                String releaseDateNotFormat = ms.getMessage("movie.releaseDateNotFormat", null, null);
                result.addError(new FieldError("movieCreateForm", "releaseDate", releaseDateNotFormat));
            }

            List<Genre> genres = genreService.findGenres();
            model.addAttribute("genres", genres);
            return "admin/movie/createMovie";
        }
        // =========================================================

        // 2. 중복검사 실패할 경우
        try {
            movieService.createMovie(movieCreateForm);
        } catch (IllegalStateException e) {
            String titleDuplicate = ms.getMessage("movie.titleDuplicate", null, null);

            if(e.getMessage().equals(titleDuplicate)) {
                result.addError(new FieldError("movieCreateForm", "title", titleDuplicate));
            }
        }

        if(result.hasErrors()) {
            List<Genre> genres = genreService.findGenres();
            model.addAttribute("genres", genres);
            return "admin/movie/createMovie";
        }

        return "redirect:/";
    }

    @GetMapping("/genre/create")
    public String createGenre(Model model) {
        model.addAttribute("genreCreateForm", new GenreCreateForm());
        return "admin/genre/createGenre";
    }

    @PostMapping("/genre/create")
    public String createGenre(@Valid GenreCreateForm genreCreateForm, BindingResult result) {
        // 1. genreCreateForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "admin/genre/createGenre";
        }

        try {
            genreService.createGenre(genreCreateForm);
        } catch (IllegalStateException e) {
            String genreNameDuplicate = ms.getMessage("genre.genreNameDuplicate", null, null);

            if(e.getMessage().equals(genreNameDuplicate)) {
                result.addError(new FieldError("genreCreateForm", "genreName", genreNameDuplicate));
            }
        }

        // 2. 중복검사 실패할 경우
        if(result.hasErrors()) {
            return "admin/genre/createGenre";
        }

        return "redirect:/";
    }
}
