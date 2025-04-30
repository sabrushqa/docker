package com.example.demo.Controllers;

import com.example.demo.model.Offre;
import com.example.demo.model.Recruteur;
import com.example.demo.service.OffreService;
import com.example.demo.service.RecruteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruteur/offres")
public class OffreController {

    private final OffreService offreService;
    private final RecruteurService recruteurService;

    @GetMapping("/nouvelle")
    public String afficherFormulaire(Model model) {
        model.addAttribute("offre", new Offre());
        model.addAttribute("secteurs", getSecteurs());
        return "recruteur/formulaire-offre";
    }

    @PostMapping("/nouvelle")
    public String publierOffre(@ModelAttribute Offre offre,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Recruteur recruteur = recruteurService.getRecruteurByEmail(userDetails.getUsername());
        offreService.creerOffre(offre, recruteur);
        return "redirect:/recruteur/home";
    }
    @GetMapping("/mes-offres")
    public String voirMesOffres(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Recruteur recruteur = recruteurService.getRecruteurByEmail(userDetails.getUsername());
        List<Offre> offres = offreService.getOffresParRecruteur(recruteur.getId());
        model.addAttribute("offres", offres);
        return "recruteur/mes-offres";
    }
    @GetMapping("/supprimer/{id}")
    public String supprimerOffre(@PathVariable Long id) {
        offreService.supprimerOffre(id);
        return "redirect:/recruteur/offres/mes-offres";
    }



    private String[] getSecteurs() {
        return new String[]{"Informatique", "Santé", "Finance", "Éducation", "BTP", "Commerce", "RH"};
    }
}
