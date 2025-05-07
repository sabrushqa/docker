package com.example.demo.Controllers;

import com.example.demo.Repository.*;
import com.example.demo.model.*;
import com.example.demo.service.CandidatureService;
import com.example.demo.service.EntretienService;
import com.example.demo.service.RecruteurService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecruteurController {

    private final RecruteurService recruteurService;
    private final CandidatureService candidatureService;
    private final EntretienService entretienService;

    private final RecruteurRepository recruteurRepository;
    private final OffreRepository offreRepository;
    private final CandidatureRepository candidatureRepository;
    private final EntretienRepository entretienRepository;
private final UserRepository userRepository;


    @GetMapping("/recruteur/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Recruteur recruteur = recruteurService.getRecruteurByEmail(email);
        model.addAttribute("recruteur",recruteur);

        // Récupération des données à afficher
        List<Offre> offres = offreRepository.findByRecruteur(recruteur);
        List<Candidature> candidatures = candidatureRepository.findByOffre_Recruteur(recruteur);
        List<Entretien> entretiens = entretienRepository.findByCandidature_Offre_Recruteur(recruteur);

        // Calcul des statistiques
        long totalOffres = offres.size();
        long totalCandidatures = candidatures.size();
        long totalAcceptées = candidatures.stream().filter(c -> "Acceptée".equalsIgnoreCase(c.getStatut())).count();
        long totalRefusées = candidatures.stream().filter(c -> "Refusée".equalsIgnoreCase(c.getStatut())).count();
        long totalValidées = candidatures.stream().filter(c -> "Validée".equalsIgnoreCase(c.getStatut())).count();

        // 5 dernières candidatures, offres et entretiens
        List<Candidature> dernieresCandidatures = candidatureRepository.findTop5ByOffre_RecruteurOrderByDateCandidatureDesc(recruteur);
        List<Offre> dernieresOffres = offreRepository.findTop5ByRecruteurOrderByDatePublicationDesc(recruteur);
        List<Entretien> derniersEntretiens = entretienRepository.findTop5ByCandidature_Offre_RecruteurOrderByDateEntretienDesc(recruteur);

        // Ajouter au modèle
        model.addAttribute("recruteur", recruteur);
        model.addAttribute("totalOffres", totalOffres);
        model.addAttribute("totalCandidatures", totalCandidatures);
        model.addAttribute("totalAcceptées", totalAcceptées);
        model.addAttribute("totalRefusées", totalRefusées);
        model.addAttribute("totalValidées", totalValidées);
        model.addAttribute("dernieresCandidatures", dernieresCandidatures);
        model.addAttribute("dernieresOffres", dernieresOffres);
        model.addAttribute("derniersEntretiens", derniersEntretiens);

        return "recruteur/home"; // Retourne la vue home du recruteur
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
