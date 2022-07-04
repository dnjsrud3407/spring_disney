package com.tistory.dnjsrud.disney.review;

import com.tistory.dnjsrud.disney.global.MyPage;
import com.tistory.dnjsrud.disney.movie.MovieDetailDto;
import com.tistory.dnjsrud.disney.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;

    @PostMapping("/create/{movieId}")
    public String createReview(@Validated @ModelAttribute ReviewCreateForm reviewCreateForm, BindingResult result,
                               @PathVariable Long movieId, Model model, RedirectAttributes redirectAttributes,
                               @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Long userId = 66L;
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, userId);
        reviewCreateForm.setUserId(userId);
        if(reviewDetailDto == null) {
            log.info("ReviewCreateForm : {}", reviewCreateForm);
            MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
            movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));

            Page<ReviewDetailDto> reviewList = reviewService.findReviewDetailDtoListWithoutUser(pageable, movieId, userId);
            MyPage page = new MyPage(reviewList);

            // ReviewCreateForm 중 Validation이 안 지켜졌을 경우
            if(result.hasErrors()) {
                model.addAttribute("movie", movie);
                model.addAttribute("reviewList", reviewList.getContent());
                model.addAttribute("page", page);
                model.addAttribute("totalCount", reviewList.getTotalElements());

                return "movie/detail";
            }

            Long reviewId = reviewService.createReview(reviewCreateForm);
            redirectAttributes.addFlashAttribute("pageable", pageable);

            return "redirect:/movie/{movieId}";
        }

        return "redirect:/movie/{movieId}";
    }

    @GetMapping("/modify/{movieId}")
    public String modifyReview(@PathVariable Long movieId, Model model,
                               @PageableDefault(page = 0, size = 5) Pageable pageable) {
        MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
        // 평점 소수점 둘째 자리까지 표기
        movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));

        // 현재 유저가 작성한 리뷰
        Long userId = 66L;
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, userId);
        ReviewModifyForm reviewModifyForm = new ReviewModifyForm();
        reviewModifyForm.setReviewId(reviewDetailDto.getId());
        reviewModifyForm.setStar(reviewDetailDto.getStar());
        reviewModifyForm.setContent(reviewDetailDto.getContent());
        model.addAttribute("reviewModifyForm", reviewModifyForm);

        // 영화 리뷰 조회
        Page<ReviewDetailDto> result = reviewService.findReviewDetailDtoListWithoutUser(pageable, movieId, userId);

        model.addAttribute("movie", movie);
        model.addAttribute("reviewList", result.getContent());

        MyPage page = new MyPage(result);
        model.addAttribute("page", page);
        model.addAttribute("totalCount", result.getTotalElements());
        return "movie/modifyReview";
    }

    @PostMapping("/modify/{movieId}")
    public String modifyReview(@Validated @ModelAttribute ReviewModifyForm reviewModifyForm, BindingResult result,
                               @PathVariable Long movieId, Model model, RedirectAttributes redirectAttributes,
                               @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Long userId = 66L;
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, userId);
        reviewModifyForm.setReviewId(reviewDetailDto.getId());
        reviewModifyForm.setUserId(userId);
        if(reviewDetailDto != null) {
            MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
            // 평점 소수점 둘째 자리까지 표기
            movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));
            Page<ReviewDetailDto> reviewList = reviewService.findReviewDetailDtoListWithoutUser(pageable, movieId, userId);
            MyPage page = new MyPage(reviewList);

            if(result.hasErrors()) {
                model.addAttribute("movie", movie);
                model.addAttribute("reviewList", reviewList.getContent());

                model.addAttribute("page", page);
                model.addAttribute("totalCount", reviewList.getTotalElements());

                return "movie/modifyReview";
            }

            // 리뷰 수정
            reviewService.modifyReview(reviewModifyForm);

            redirectAttributes.addFlashAttribute("pageable", pageable);

            return "redirect:/movie/{movieId}";
        }
        return "redirect:/movie/{movieId}";
    }

    @PostMapping("/delete/{movieId}")
    public String delete(@PathVariable Long movieId, RedirectAttributes redirectAttributes,
                         @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Long userId = 66L;
        ReviewDetailDto review = reviewService.findReviewDetailDto(movieId, userId);
        if(review != null) {
            reviewService.deleteReview(userId, movieId);
        }

        redirectAttributes.addFlashAttribute("pageable", pageable);

        return "redirect:/movie/{movieId}";
    }
}
