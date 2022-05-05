package com.tistory.dnjsrud.disney.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        if(result.hasErrors()) {
            return "user/join";
        }

        try {
            userService.join(userJoinForm);
        } catch (IllegalStateException e) {
//            result.addError(new FieldError("userJoinForm", ));
        }
        return "redirect:/";
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {
        model.addAttribute("user", new UserInfoDto());
        return "user/myPage";
    }
}