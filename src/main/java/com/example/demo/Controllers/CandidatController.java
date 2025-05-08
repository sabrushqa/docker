package com.example.demo.Controllers;

import com.example.demo.model.Candidat;
import com.example.demo.model.Offre;
import com.example.demo.model.Profil;
import com.example.demo.service.CandidatService;
import com.example.demo.service.OffreService;
import com.example.demo.service.ProfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CandidatController {

    private final CandidatService candidatService;
    private final OffreService offreService;
    private final ProfilService profilService;
    private final RestTemplate restTemplate; // RestTemplate injecté

    // Page d'accueil du candidat
    @GetMapping("/candidat/home")
    public String home(Authentication authentication, Model model) {
        String email = authentication.getName();

        // Récupérer le candidat via le service
        Candidat candidat = candidatService.getCandidatByEmail(email);

        // Récupérer le profil du candidat
        Profil profil = profilService.getProfilByCandidatId(candidat.getId())
                .orElseThrow(() -> new RuntimeException("Profil non trouvé pour le candidat"));

        // Récupérer quelques offres pertinentes pour les recommandations
        List<Offre> offresPertinentes = offreService.getToutesLesOffres().stream()
                .limit(10) // Limiter à 10 offres pour les recommandations
                .collect(Collectors.toList());

        // Recommandations basées sur l'API FastAPI
        List<String> recommandations = getRecommandationsFromAPI(profil, offresPertinentes);

        // Ajouter les données au modèle pour la vue
        model.addAttribute("candidat", candidat);
        model.addAttribute("profil", profil);
        model.addAttribute("recommandations", recommandations);

        return "candidat/home";
    }

    // Méthode pour récupérer les recommandations depuis l'API FastAPI
    private List<String> getRecommandationsFromAPI(Profil profil, List<Offre> offres) {
        try {
            // URL de l'API FastAPI
            String apiUrl = "http://localhost:8000/recommandations";

            // Création de la requête
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Extraire les données du profil pour l'API
            String bio = profil.getBio() != null ? profil.getBio() : "";

            List<String> experiences = profil.getExperiences().stream()
                    .map(e -> e.getDescription())
                    .collect(Collectors.toList());

            List<String> formations = profil.getFormations().stream()
                    .map(f -> f.getDescription())
                    .collect(Collectors.toList());

            List<String> langues = profil.getLangues().stream()
                    .map(l -> l.getNom())
                    .collect(Collectors.toList());

            List<String> offresTexts = offres.stream()
                    .map(o -> o.getTitre() + " " + o.getDescription())
                    .collect(Collectors.toList());

            // Création du corps de la requête en JSON qui correspond à la structure attendue par FastAPI
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("bio", bio);
            requestBody.put("experiences", experiences);
            requestBody.put("formations", formations);
            requestBody.put("langues", langues);
            requestBody.put("offres", offresTexts);

            // Envoi de la requête POST
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

            // Traitement de la réponse (si OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                // Réponse JSON contenant les recommandations
                List<String> recommandationsList = (List<String>) response.getBody().get("recommandations");
                return recommandationsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of("Aucune recommandation disponible.");
    }

    // Affichage de toutes les offres
    @GetMapping("/candidat/offres")
    public String afficherToutesLesOffres(Model model) {
        List<Offre> offres = offreService.getToutesLesOffres();
        model.addAttribute("offres", offres);
        return "candidat/toutes-offres";
    }

    // Recherche d'offres avec filtres
    @GetMapping("/candidat/offres/recherche")
    public String rechercherOffres(@RequestParam(required = false) String secteur,
                                   @RequestParam(required = false) String typeContrat,
                                   @RequestParam(required = false) String lieu,
                                   @RequestParam(required = false) String entreprise,
                                   Model model) {

        secteur = clean(secteur);
        typeContrat = clean(typeContrat);
        lieu = clean(lieu);
        entreprise = clean(entreprise);

        List<Offre> offres = offreService.rechercherOffres(secteur, typeContrat, lieu, entreprise);
        model.addAttribute("offres", offres);

        return "candidat/toutes-offres"; // Utilise la même page pour afficher les résultats
    }

    // Méthode utilitaire pour nettoyer les paramètres
    private String clean(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }
}