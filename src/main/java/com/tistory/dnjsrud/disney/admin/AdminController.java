package com.tistory.dnjsrud.disney.admin;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreCreateForm;
import com.tistory.dnjsrud.disney.genre.GenreModifyForm;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.global.MyPage;
import com.tistory.dnjsrud.disney.movie.*;
import com.tistory.dnjsrud.disney.validate.ValidationSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public String createMovie(Model model) {
        List<Genre> genres = genreService.findGenres();
        model.addAttribute("genres", genres);
        model.addAttribute("movieCreateForm", new MovieCreateForm());
        return "admin/movie/createMovie";
    }

    @PostMapping("/movie/create")
    public String createMovie(@Validated(ValidationSequence.class) MovieCreateForm movieCreateForm, BindingResult result, Model model) throws IOException {
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

    /**
     * 영화 목록
     * @param condition
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/movie/list")
    public String movieList(@ModelAttribute MovieSearchCondition condition, Model model,
                            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<MovieAdminListDto> result;
        if(condition.getTitle() != null || condition.getGenreId() != null) {
            result = movieService.findMovieAdminListDto(pageable, condition);
        } else {
            result = movieService.findMovieAdminListDto(pageable);
        }

        // 평점 소수점 둘째 자리까지 표기
        for (MovieAdminListDto movieAdminListDto : result) {
            movieAdminListDto.setStar(Float.parseFloat(String.format("%.2f", movieAdminListDto.getStar())));
        }
        model.addAttribute("movieAdminListDto", result.getContent());

        long totalCount = result.getTotalElements();
        model.addAttribute("totalCount", totalCount);

        List<Genre> genreList = genreService.findGenres();
        model.addAttribute("genreList", genreList);

        model.addAttribute("condition", condition);

        MyPage page = new MyPage(result);
        model.addAttribute("page", page);

        return "admin/movie/list";
    }

    @GetMapping("/movie/modify/{id}")
    public String modifyMovie(@PathVariable Long id, Model model) {
        Movie movie = movieService.findMovie(id);
        MovieModifyForm movieModifyForm = new MovieModifyForm();
        movieModifyForm.setMovieId(id);
        movieModifyForm.setTitle(movie.getTitle());
        movieModifyForm.setReleaseDate(movie.getReleaseDate());
        movieModifyForm.setContent(movie.getContent());
        movieModifyForm.changeGenreIds(movie.getMovieGenres());
        movieModifyForm.setOriginalFileName(movie.getPoster().getOriginalFileName());
        movieModifyForm.setVisible(movie.isVisible());

        List<Genre> genres = genreService.findGenres();
        model.addAttribute("genres", genres);
        model.addAttribute("movieModifyForm", movieModifyForm);

        return "admin/movie/modifyMovie";
    }

    @PostMapping("/movie/modify/{id}")
    public String modifyMoviePost(@Validated(ValidationSequence.class) MovieModifyForm movieModifyForm, BindingResult result, Model model) {
        log.info("form : {}", movieModifyForm);

        // 1. movieModifyForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            // releaseDate가 dateTimeFormat pattern이 안 맞는 경우
            if(movieModifyForm.getReleaseDate() == null && result.getFieldError("releaseDate").getRejectedValue() != null) {
                String releaseDateNotFormat = ms.getMessage("movie.releaseDateNotFormat", null, null);
                result.addError(new FieldError("movieModifyForm", "releaseDate", releaseDateNotFormat));
            }

            List<Genre> genres = genreService.findGenres();
            model.addAttribute("genres", genres);
            return "admin/movie/modifyMovie";
        }

        try {
            movieService.modifyMovie(movieModifyForm);
        } catch (IllegalStateException e) {
            // 중복검사 실패할 경우
            String titleDuplicate = ms.getMessage("movie.titleDuplicate", null, null);
            if(e.getMessage().equals(titleDuplicate)) {
                result.addError(new FieldError("movieModifyForm", "title", titleDuplicate));
            }
        } catch (IOException e) {
            result.addError(new FieldError("movieModifyForm", "file", "파일을 찾을 수 없습니다."));
        }

        if(result.hasErrors()) {
            List<Genre> genres = genreService.findGenres();
            model.addAttribute("genres", genres);
            return "admin/movie/modifyMovie";
        }

        return "redirect:/admin/movie/list";
    }


    @GetMapping("/genre/create")
    public String createGenre(Model model) {
        model.addAttribute("genreCreateForm", new GenreCreateForm());
        return "admin/genre/createGenre";
    }

    @PostMapping("/genre/create")
    public String createGenrePost(@Validated(ValidationSequence.class) GenreCreateForm genreCreateForm, BindingResult result) {
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

    @GetMapping("/genre/list")
    public String genreList(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
        Page<Genre> result = genreService.findGenres(pageable);

        MyPage page = new MyPage(result);
        model.addAttribute("genreList", result.getContent());
        model.addAttribute("page", page);

        return "admin/genre/list";
    }

    @GetMapping("/genre/modify/{id}")
    public String modifyGenre(@PathVariable Long id, Model model) {
        Genre genre = genreService.findGenre(id);

        if(genre != null) {
            GenreModifyForm genreModifyForm = new GenreModifyForm(genre.getId(), genre.getGenreName());
            model.addAttribute("genreModifyForm", genreModifyForm);
        }

        return "admin/genre/modifyGenre";
    }

    @PostMapping("/genre/modify/{id}")
    public String modifyGenrePost(@Validated(ValidationSequence.class) GenreModifyForm genreModifyForm, BindingResult result) {
        // form Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "admin/genre/modifyGenre";
        }

        try {
            genreService.modifyGenre(genreModifyForm);
        } catch (IllegalStateException e) {
            String genreNameDuplicate = ms.getMessage("genre.genreNameDuplicate", null, null);

            if(e.getMessage().equals(genreNameDuplicate)) {
                result.addError(new FieldError("genreModifyForm", "genreName", genreNameDuplicate));
            }
        }

        // 장르명 중복검사 실패한 경우
        if(result.hasErrors()) {
            return "admin/genre/modifyGenre";
        }
        
        return "redirect:/admin/genre/list";
    }
}
