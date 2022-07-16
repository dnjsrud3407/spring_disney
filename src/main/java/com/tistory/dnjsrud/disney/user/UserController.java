package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.global.MyPage;
import com.tistory.dnjsrud.disney.review.ReviewService;
import com.tistory.dnjsrud.disney.review.ReviewUserDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final MessageSource ms;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String exceptionMsg, RedirectAttributes redirectAttributes,
                        @RequestParam(required = false) String errorMsg, Model model) {
        // 로그인 실패시 CustomFailureHandler -> 1 -> 2
        if(exceptionMsg != null) {
            // 1. CustomFailureHandler 에서 넘어 온 경우
            if(exceptionMsg.equals("invalid")) {
                redirectAttributes.addAttribute("errorMsg", "invalid");
            } else if(exceptionMsg.equals("requestAdm")) {
                redirectAttributes.addAttribute("errorMsg", "requestAdm");
            }
            return "redirect:/user/login";
        }

        // 2. redirect:/user/login 로 넘어 온 경우
        if(errorMsg != null) {
            model.addAttribute("msg", ms.getMessage(errorMsg, null, null));
        }

        return "user/login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("userJoinForm", new UserJoinForm());
        return "user/join";
    }

    @PostMapping("/join")
    public String joinPost(@Valid UserJoinForm userJoinForm, BindingResult result) {
        log.info("form : {}", userJoinForm);

        // 1. userJoinForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "user/join";
        }

        // 2. 중복검사 실패할 경우
        try {
            userService.join(userJoinForm);
        } catch (IllegalStateException e) {
            String loginIdDuplicate = ms.getMessage("user.loginIdDuplicate", null, null);
            String nicknameDuplicate = ms.getMessage("user.nicknameDuplicate", null, null);
            String emailDuplicate = ms.getMessage("user.emailDuplicate", null, null);

            if(e.getMessage().equals(loginIdDuplicate)) {
                result.addError(new FieldError("userJoinForm", "loginId", loginIdDuplicate));
            } else if (e.getMessage().equals(nicknameDuplicate)) {
                result.addError(new FieldError("userJoinForm", "nickname", nicknameDuplicate));
            } else if (e.getMessage().equals(emailDuplicate)) {
                result.addError(new FieldError("userJoinForm", "email", emailDuplicate));
            }
        }

        if(result.hasErrors()) {
            return "user/join";
        }
        return "redirect:/";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request,
                         @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Long userId = 66L;
        UserInfoDto userInfoDto = userService.findUserInfoDto(userId);
        model.addAttribute("userInfoDto", userInfoDto);

        Page<ReviewUserDto> result = reviewService.findReviewUserDto(pageable, userId);
        if (result != null) {
            model.addAttribute("reviewList", result.getContent());

            MyPage page = new MyPage(result);
            model.addAttribute("page", page);
        }

        return "user/myPage";
    }

    @GetMapping("/reviewList")
    public String reviewList(@PageableDefault(page = 0, size = 5) Pageable pageable,
                             RedirectAttributes redirectAttributes) {
        Long userId = 66L;
        Page<ReviewUserDto> result = reviewService.findReviewUserDto(pageable, userId);

        redirectAttributes.addFlashAttribute("reviewList", result.getContent());


        return "redirect:/user/myPage";
    }
}