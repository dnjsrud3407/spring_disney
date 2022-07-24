package com.tistory.dnjsrud.disney;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/disney";
    }

    @GetMapping("/disney")
    public String home() {
        return "disney";
    }

    @GetMapping("/error/denied")
    public String errDenied(Model model) {
        model.addAttribute("msg", "잘못된 접근입니다.");
        return "err/denied";
    }
}
