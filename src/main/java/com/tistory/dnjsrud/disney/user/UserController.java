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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final MessageSource ms;

    @GetMapping("/loginForm")
    public String login(@RequestParam(required = false) String exceptionMsg, RedirectAttributes redirectAttributes) {
        // 로그인 실패시 CustomFailureHandler -> 1 -> 2. /loginErr 로 이동
        if(exceptionMsg != null) {
            // 1. CustomFailureHandler 에서 넘어 온 경우
            if(exceptionMsg.equals("invalid")) {
                redirectAttributes.addFlashAttribute("errorMsg", "invalid");
            } else if(exceptionMsg.equals("requestAdm")) {
                redirectAttributes.addFlashAttribute("errorMsg", "requestAdm");
            }
            return "redirect:/user/loginErr";
        }

        return "user/login";
    }

    @GetMapping("/loginErr")
    public String loginErr(HttpServletRequest request, Model model) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        String errorMsg;
        if(flashMap!=null) {
            errorMsg = (String) flashMap.get("errorMsg");
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
        return "redirect:/user/loginForm";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request,
                         @PageableDefault(page = 0, size = 5) Pageable pageable,
                         @AuthenticationPrincipal SecurityUser securityUser) {
        Long userId = securityUser.getId();
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
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal SecurityUser securityUser) {
        Long userId = securityUser.getId();
        Page<ReviewUserDto> result = reviewService.findReviewUserDto(pageable, userId);

        redirectAttributes.addFlashAttribute("reviewList", result.getContent());


        return "redirect:/user/myPage";
    }
}