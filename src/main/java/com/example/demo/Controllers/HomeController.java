package com.example.demo.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Affiche la page index.html
    @GetMapping("/")
    public String showHomePage() {
        return "index"; // Fichier situé dans src/main/resources/templates/index.html
    }

    // Affiche une autre page comme cv-preview.html
    @GetMapping("/cv")
    public String showCV() {
        return "fragments/cv-preview"; // attention : fichier complet, pas un fragment
    }


}

