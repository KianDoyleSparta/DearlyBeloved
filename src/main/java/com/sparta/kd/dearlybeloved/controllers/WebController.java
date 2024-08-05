package com.sparta.kd.dearlybeloved.controllers;

import com.sparta.kd.dearlybeloved.services.DropBoxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final DropBoxService dropBoxService;

    public WebController(DropBoxService dropBoxService) {
        this.dropBoxService = dropBoxService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        model.addAttribute("images", dropBoxService.getImages("/test"));
        return "home";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/book")
    public String getBookPage() {
        return "book";
    }

    @GetMapping("/partners")
    public String getPartnersPage() {
        return "partners";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @PostMapping("/admin")
    public String postAdminPage() {
        return "admin";
    }
}
