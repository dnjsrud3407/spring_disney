package com.tistory.dnjsrud.disney;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
