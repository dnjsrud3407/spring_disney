package com.tistory.dnjsrud.disney.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MessageSource ms;

    @GetMapping("/login")
    public String login() {
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
    public String myPage(Model model) {
        model.addAttribute("user", new UserInfoDto());
        return "user/myPage";
    }
}