package com.example.demo.Controllers;

import com.example.demo.model.Recruteur;
import com.example.demo.service.RecruteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RecruteurController {

    private final RecruteurService recruteurService;

    @GetMapping("/recruteur/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Recruteur recruteur = recruteurService.getRecruteurByEmail(email);
        model.addAttribute("recruteur", recruteur);
        return "recruteur/home"; // Assure-toi que le fichier HTML existe dans templates/recruteur/home.html
    }
}
