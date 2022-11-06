package com.tistory.dnjsrud.disney.movie;

import com.tistory.dnjsrud.disney.genre.Genre;
import com.tistory.dnjsrud.disney.genre.GenreService;
import com.tistory.dnjsrud.disney.global.MyPage;
import com.tistory.dnjsrud.disney.review.ReviewCreateForm;
import com.tistory.dnjsrud.disney.review.ReviewDetailDto;
import com.tistory.dnjsrud.disney.review.ReviewService;
import com.tistory.dnjsrud.disney.user.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final ReviewService reviewService;

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

        // 평점 소수점 둘째 자리까지 표기
        for (MovieListDto movieListDto : result) {
            movieListDto.setStar(Float.parseFloat(String.format("%.2f", movieListDto.getStar())));
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
    public String movieDetail(@PathVariable Long movieId, HttpServletRequest request, Model model,
                              @PageableDefault(page = 0, size = 5) Pageable pageable,
                              @AuthenticationPrincipal SecurityUser securityUser) {
        MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
        // 평점 소수점 둘째 자리까지 표기
        movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if(flashMap!=null) {
            pageable = (Pageable) flashMap.get("pageable");
        }

        Page<ReviewDetailDto> result;
        if(securityUser != null) {
            Long userId = securityUser.getId();
            // 영화 리뷰 조회
            result = reviewService.findReviewDetailDtoListWithoutUser(pageable, movieId, userId);

            // 현재 유저가 작성한 리뷰
            ReviewDetailDto myReview = reviewService.findReviewDetailDto(movieId, userId);
            if(myReview != null) {
                model.addAttribute("myReview", myReview);
                model.addAttribute("totalCount", result.getTotalElements() + 1);

                model.addAttribute("movie", movie);
                model.addAttribute("reviewList", result.getContent());

                MyPage page = new MyPage(result);
                model.addAttribute("page", page);

                return "movie/detailReview";
            }
        }

        result = reviewService.findReviewDetailDtoList(pageable, movieId);

        model.addAttribute("reviewCreateForm", new ReviewCreateForm());
        model.addAttribute("totalCount", result.getTotalElements());

        model.addAttribute("movie", movie);
        model.addAttribute("reviewList", result.getContent());

        MyPage page = new MyPage(result);
        model.addAttribute("page", page);

        return "movie/detailReviewNone";
    }
}
