package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.global.MyPage;
import com.tistory.dnjsrud.disney.review.ReviewService;
import com.tistory.dnjsrud.disney.review.ReviewUserDto;
import com.tistory.dnjsrud.disney.validate.ValidationSequence;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
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
            } else if(exceptionMsg.equals("oauth2Duplicate")) {
                redirectAttributes.addFlashAttribute("errorMsg", "oauth2Duplicate");
            }
            else if(exceptionMsg.equals("requestAdm")) {
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
    public String joinPost(@Validated(ValidationSequence.class) UserJoinForm userJoinForm, BindingResult result) {
        log.info("form : {}", userJoinForm);

        // 비밀번호, 비밀번호 확인이 일치하지 않을 경우
        if(!userJoinForm.getPassword().equals(userJoinForm.getPasswordConfirm())) {
            String confirmPassword = ms.getMessage("user.confirmPassword", null, null);
            result.addError(new FieldError("userJoinForm", "passwordConfirm", confirmPassword));
        }

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

    @GetMapping("/modifyNickname")
    public String modifyNickname(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        Long userId = securityUser.getId();
        ModifyNicknameForm modifyNicknameForm = userService.findNickname(userId);
        model.addAttribute("modifyNicknameForm", modifyNicknameForm);

        return "user/modifyNickname";
    }

    @PostMapping("/modifyNickname")
    public String modifyNickname(@Validated(ValidationSequence.class) ModifyNicknameForm modifyNicknameForm, BindingResult result,
                                 @AuthenticationPrincipal SecurityUser securityUser) {
        // 1. modifyNicknameForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "user/modifyNickname";
        }

        // 2. 중복검사 실패할 경우
        Long userId = securityUser.getId();
        try {
            userService.modifyNickname(userId, modifyNicknameForm);

        } catch (IllegalStateException e) {
            String nicknameDuplicate = ms.getMessage("user.nicknameDuplicate", null, null);
            result.addError(new FieldError("modifyNicknameForm", "nickname", nicknameDuplicate));
        }

        if(result.hasErrors()) {
            return "user/modifyNickname";
        }

        return "redirect:/user/myPage";
    }

    @GetMapping("/modifyPw")
    public String modifyPasswordConfirm(Model model) {
        model.addAttribute("modifyPasswordConfirmForm", new ModifyPasswordConfirmForm());

        return "user/modifyPasswordConfirm";
    }

    @PostMapping("/modifyPw")
    public String modifyPasswordConfirm(@Validated(ValidationSequence.class) ModifyPasswordConfirmForm modifyPasswordConfirmForm, BindingResult result,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        // 1. modifyPasswordForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "user/modifyPasswordConfirm";
        }

        // 현재 비밀번호와 일치하는지 확인
        try {
            String userPassword = securityUser.getPassword();
            userService.confirmPassword(modifyPasswordConfirmForm.getPassword(), userPassword);
        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("modifyPasswordConfirmForm", "password", e.getMessage()));
            return "/user/modifyPasswordConfirm";
        }

        // 비밀번호 일치한다면
        return "redirect:/user/modifyPassword";
    }

    @GetMapping("/modifyPassword")
    public String modifyPassword(Model model) {
        model.addAttribute("modifyPasswordForm", new ModifyPasswordForm());
        return "user/modifyPassword";
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(@Validated(ValidationSequence.class) ModifyPasswordForm modifyPasswordForm, BindingResult result,
                                 @AuthenticationPrincipal SecurityUser securityUser) {
        // 비밀번호, 비밀번호 확인이 일치하지 않을 경우
        if(!modifyPasswordForm.getPassword().equals(modifyPasswordForm.getPasswordConfirm())) {
            String confirmPassword = ms.getMessage("user.confirmPassword", null, null);
            result.addError(new FieldError("modifyPasswordForm", "passwordConfirm", confirmPassword));
        }

        // 1. modifyPasswordForm 중 Validation이 안 지켜졌을 경우
        if(result.hasErrors()) {
            return "user/modifyPassword";
        }

        // 비밀번호 변경
        userService.changePassword(securityUser.getId(), modifyPasswordForm.getPassword());

        return "redirect:/user/myPage";
    }
}