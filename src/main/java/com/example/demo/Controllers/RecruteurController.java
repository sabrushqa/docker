package com.example.demo.Controllers;

import com.example.demo.model.Candidature;
import com.example.demo.model.Recruteur;
import com.example.demo.service.CandidatureService;
import com.example.demo.service.RecruteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecruteurController {

    private final RecruteurService recruteurService;
    private final CandidatureService candidatureService;

    @GetMapping("/recruteur/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Recruteur recruteur = recruteurService.getRecruteurByEmail(email);
        model.addAttribute("recruteur", recruteur);
        return "recruteur/home";
    }

    @GetMapping("/recruteur/candidatures")
    public String voirCandidaturesDeMesOffres(Model model,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Recruteur recruteur = recruteurService.getRecruteurByEmail(email);

        List<Candidature> candidatures = candidatureService.getCandidaturesByRecruteur(recruteur);
        model.addAttribute("candidatures", candidatures);

        return "recruteur/candidatures_offres";
    }
}
