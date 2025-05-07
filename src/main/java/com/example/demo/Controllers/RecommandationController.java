package com.example.demo.Controllers;


import com.example.demo.service.RecommandationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidat")
public class RecommandationController {

    private final RecommandationService recommandationService;

    @Autowired
    public RecommandationController(RecommandationService recommandationService) {
        this.recommandationService = recommandationService;
    }

    // Route POST pour récupérer les recommandations
    @PostMapping("/recommandations")
    public String getRecommandations(@RequestBody String profilText) {
        return recommandationService.obtenirRecommandations(profilText);
    }
}

