package com.example.demo.Controllers;

import com.example.demo.Repository.CandidatRepository;
import com.example.demo.model.*;
import com.example.demo.service.ProfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
@RequestMapping("/candidat/profil")
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @Autowired
    private CandidatRepository candidatRepository;

    @GetMapping
    public String afficherProfil(Model model, Principal principal) {
        Candidat candidat = candidatRepository.findByUserEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        Profil profil = candidat.getProfil();
        if (profil == null) {
            profil = new Profil();
            profil.setCandidat(candidat);
            candidat.setProfil(profil);
            profilService.sauvegarderProfil(profil);
        }

        model.addAttribute("profil", profil);
        model.addAttribute("nouvelleExperience", new Experience());
        model.addAttribute("nouvelleFormation", new Formation());
        model.addAttribute("nouvelleLangue", new Langue());

        return "candidat/profil";
    }

    @PostMapping("/photo")
    public String uploadPhoto(@RequestParam("photo") MultipartFile file, Principal principal, RedirectAttributes redirect) {
        if (file.isEmpty()) {
            redirect.addFlashAttribute("error", "Aucune photo sélectionnée.");
            return "redirect:/candidat/profil";
        }

        try {
            String uploadDir = "src/main/resources/static/uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            Profil profil = profilService.getProfilByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Profil non trouvé"));
            profil.setPhoto(fileName);
            profilService.sauvegarderProfil(profil);

            redirect.addFlashAttribute("success", "Photo mise à jour !");
        } catch (IOException e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Erreur lors de l'envoi de la photo.");
        }

        return "redirect:/candidat/profil";
    }

    // Expérience
    @PostMapping("/experience/ajouter")
    public String ajouterExperience(@ModelAttribute Experience experience, Principal principal, RedirectAttributes redirect) {
        profilService.ajouterExperience(experience, principal.getName());
        redirect.addFlashAttribute("success", "Expérience ajoutée.");
        return "redirect:/candidat/profil";
    }

    @PostMapping("/experience/supprimer/{id}")
    public String supprimerExperience(@PathVariable Long id, RedirectAttributes redirect) {
        profilService.supprimerExperience(id);
        redirect.addFlashAttribute("success", "Expérience supprimée.");
        return "redirect:/candidat/profil";
    }

    // Formation
    @PostMapping("/formation/ajouter")
    public String ajouterFormation(@ModelAttribute Formation formation, Principal principal, RedirectAttributes redirect) {
        profilService.ajouterFormation(formation, principal.getName());
        redirect.addFlashAttribute("success", "Formation ajoutée.");
        return "redirect:/candidat/profil";
    }

    @PostMapping("/formation/supprimer/{id}")
    public String supprimerFormation(@PathVariable Long id, RedirectAttributes redirect) {
        profilService.supprimerFormation(id);
        redirect.addFlashAttribute("success", "Formation supprimée.");
        return "redirect:/candidat/profil";
    }

    // Langue
    @PostMapping("/langue/ajouter")
    public String ajouterLangue(@ModelAttribute Langue langue, Principal principal, RedirectAttributes redirect) {
        profilService.ajouterLangue(langue, principal.getName());
        redirect.addFlashAttribute("success", "Langue ajoutée.");
        return "redirect:/candidat/profil";
    }

    @PostMapping("/langue/supprimer/{id}")
    public String supprimerLangue(@PathVariable Long id, RedirectAttributes redirect) {
        profilService.supprimerLangue(id);
        redirect.addFlashAttribute("success", "Langue supprimée.");
        return "redirect:/candidat/profil";
    }
}
