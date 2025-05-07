package com.example.demo.Controllers;

import com.example.demo.service.CaptchaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * Affiche la page de connexion et transmet un indicateur d'erreur si nécessaire.
     *
     * @param error   présent si une précédente tentative de connexion a échoué
     * @param logout  présent si l'utilisateur s'est déconnecté avec succès
     * @param model   modèle Thymeleaf pour passer les attributs à la vue
     * @return le nom de la vue de connexion (login.html)
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }
        return "login";
    }

    /**
     * Redirige l'utilisateur connecté vers sa page d'accueil selon son rôle.
     *
     * @param auth objet d'authentification Spring Security
     * @return redirection vers la page appropriée
     */
    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) {
            return "redirect:/login";
        }
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_CANDIDAT".equals(role)) {
                return "redirect:/candidat/home";
            } else if ("ROLE_RECRUTEUR".equals(role)) {
                return "redirect:/recruteur/home";
            }
        }
        return "redirect:/login?error";
    }

    /**
     * Vérifie la réponse reCAPTCHA avant d'autoriser la soumission du formulaire de connexion.
     *
     * @param gRecaptchaResponse la réponse CAPTCHA envoyée par le frontend
     * @param model le modèle Thymeleaf pour ajouter des messages d'erreur
     * @return la vue de redirection ou de retour sur le formulaire de connexion
     */
    @PostMapping("/login")
    public String loginWithCaptcha(
            @RequestParam("g-recaptcha-response") String gRecaptchaResponse,
            Authentication auth, Model model) {

        // Vérifier le CAPTCHA
        boolean isCaptchaValid = captchaService.validateCaptcha(gRecaptchaResponse);
        if (!isCaptchaValid) {
            model.addAttribute("captchaError", "Le CAPTCHA est invalide. Veuillez réessayer.");
            return "login";  // Retourner à la page de connexion avec un message d'erreur
        }

        // Si le CAPTCHA est valide, procéder normalement avec l'authentification
        return redirectAfterLogin(auth);
    }
}
