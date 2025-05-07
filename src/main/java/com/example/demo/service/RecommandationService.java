package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecommandationService {

    private final RestTemplate restTemplate;

    @Autowired
    public RecommandationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Exemple de méthode pour appeler une API FastAPI
    public String obtenirRecommandations(String profilText) {
        String apiUrl = "http://127.0.0.1:8000/recommandations";  // L'URL de l'API FastAPI
        // Construire la requête et envoyer via POST
        String jsonPayload = "{\"profil\": \"" + profilText + "\"}";

        // Utiliser restTemplate pour appeler l'API
        String response = restTemplate.postForObject(apiUrl, jsonPayload, String.class);
        return response;
    }
}

