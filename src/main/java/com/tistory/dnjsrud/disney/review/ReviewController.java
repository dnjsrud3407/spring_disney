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
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, 66L);
        reviewCreateForm.setUserId(66L);
        if(reviewDetailDto == null) {
            log.info("ReviewCreateForm : {}", reviewCreateForm);
            MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
            movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));

            Page<ReviewDetailDto> reviewList = reviewService.findReviewDetailDtoList(pageable, movieId);
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
            ReviewDetailDto myReview = reviewService.findReviewDetailDto(movieId, 66L);
            redirectAttributes.addFlashAttribute("myReview", myReview);
            redirectAttributes.addFlashAttribute("movie", movie);
            redirectAttributes.addFlashAttribute("reviewList", reviewList.getContent());
            redirectAttributes.addFlashAttribute("page", page);
            redirectAttributes.addFlashAttribute("totalCount", reviewList.getTotalElements());

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
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, 66L);
        ReviewModifyForm reviewModifyForm = new ReviewModifyForm();
        reviewModifyForm.setReviewId(reviewDetailDto.getId());
        reviewModifyForm.setStar(reviewDetailDto.getStar());
        reviewModifyForm.setContent(reviewDetailDto.getContent());
        model.addAttribute("reviewModifyForm", reviewModifyForm);

        // 영화 리뷰 조회
        Page<ReviewDetailDto> result = reviewService.findReviewDetailDtoList(pageable, movieId);

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
        log.info("reviewModifyForm : {}", reviewModifyForm);
        ReviewDetailDto reviewDetailDto = reviewService.findReviewDetailDto(movieId, 66L);
        reviewModifyForm.setReviewId(reviewDetailDto.getId());
        reviewModifyForm.setUserId(66L);
        if(reviewDetailDto != null) {
            MovieDetailDto movie = movieService.findMovieDetailDto(movieId);
            // 평점 소수점 둘째 자리까지 표기
            movie.setStar(Float.parseFloat(String.format("%.2f", movie.getStar())));
            Page<ReviewDetailDto> reviewList = reviewService.findReviewDetailDtoList(pageable, movieId);
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

            ReviewDetailDto myReview = reviewService.findReviewDetailDto(movieId, 66L);
            redirectAttributes.addFlashAttribute("myReview", myReview);
            redirectAttributes.addFlashAttribute("movie", movie);
            redirectAttributes.addFlashAttribute("reviewList", reviewList.getContent());
            redirectAttributes.addFlashAttribute("page", page);
            redirectAttributes.addFlashAttribute("totalCount", reviewList.getTotalElements());

            return "redirect:/movie/{movieId}";
        }
        return "redirect:/movie/{movieId}";
    }


}
