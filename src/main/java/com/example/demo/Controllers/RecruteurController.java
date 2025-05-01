package com.example.demo.Controllers;

import com.example.demo.model.Candidature;
import com.example.demo.model.Recruteur;
import com.example.demo.service.CandidatureService;
import com.example.demo.service.EntretienService;
import com.example.demo.service.RecruteurService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecruteurController {

    private final RecruteurService recruteurService;
    private final CandidatureService candidatureService;
    private final EntretienService entretienService;


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


    @PostMapping("/recruteur/candidatures/modifier/{id}")
    public String modifierCandidature(@PathVariable Long id,
                                      @RequestParam("statut") String statut,
                                      @RequestParam(value = "matchingScore", required = false) Double matchingScore) throws MessagingException {

        Candidature candidature = candidatureService.getById(id); // ✅ Variable bien définie ici
        if (candidature != null) {
            candidature.setStatut(statut);
            candidature.setMatchingScore(matchingScore);

            if ("Acceptée".equalsIgnoreCase(statut)) {
                // ✅ Récupération de l'email depuis le User lié au candidat
                String email = candidature.getCandidat().getUser().getEmail();

                // ✅ Appel correct avec les deux arguments requis
                entretienService.envoyerLienZoomPourEntretien(candidature, email);
            }
        }

        return "redirect:/recruteur/candidatures";
    }




}
