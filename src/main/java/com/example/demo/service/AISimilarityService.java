package com.example.demo.service;

import com.example.demo.model.SimilarityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class AISimilarityService {

    private final RestTemplate restTemplate;

    // L'URL de l'API IA peut être configurée dans application.properties
    @Value("${ai.similarity.api.url:http://localhost:5000/calculate-similarity}")
    private String aiApiUrl;

    // Initialisation de RestTemplate via constructeur
    public AISimilarityService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Calcule la similarité entre deux textes en utilisant l'API externe.
     * En cas d'échec, une méthode de secours est utilisée.
     *
     * @param text1 Premier texte à comparer
     * @param text2 Second texte à comparer
     * @return Score de similarité entre 0 et 1
     */
    public double getSimilarity(String text1, String text2) {
        // Vérifier si les textes sont nuls ou vides
        if (text1 == null || text2 == null || text1.trim().isEmpty() || text2.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Créer les en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Échapper correctement les guillemets dans les textes JSON
            String escapedText1 = escapeJsonString(text1);
            String escapedText2 = escapeJsonString(text2);

            // Créer le payload JSON
            String jsonPayload = String.format("{\"text1\": \"%s\", \"text2\": \"%s\"}", escapedText1, escapedText2);

            // Créer l'entité HTTP avec le payload et les en-têtes
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // Effectuer l'appel POST à l'API Flask avec un timeout
            ResponseEntity<SimilarityResponse> response = restTemplate.exchange(
                    aiApiUrl, HttpMethod.POST, entity, SimilarityResponse.class);

            // Vérifier si la réponse contient un corps et renvoyer le score de similarité
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getSimilarity(); // Retourne la similarité
            } else {
                System.out.println("L'API de similarité a renvoyé une réponse invalide");
                // Utiliser la méthode de secours
                return calculateFallbackSimilarity(text1, text2);
            }
        } catch (RestClientException e) {
            System.out.println("Erreur lors de l'appel à l'API de similarité: " + e.getMessage());
            // Utiliser la méthode de secours en cas d'erreur d'API
            return calculateFallbackSimilarity(text1, text2);
        } catch (Exception e) {
            // Log des erreurs dans la console
            System.out.println("Exception inattendue dans getSimilarity: " + e.getMessage());
            e.printStackTrace();
            // Utiliser la méthode de secours
            return calculateFallbackSimilarity(text1, text2);
        }
    }

    /**
     * Échapper les caractères spéciaux dans une chaîne JSON
     *
     * @param input Chaîne d'entrée
     * @return Chaîne échappée
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }

        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Méthode de secours pour calculer la similarité entre deux textes
     * quand l'API externe n'est pas disponible.
     *
     * @param text1 Premier texte
     * @param text2 Second texte
     * @return Score de similarité entre 0 et 1
     */
    private double calculateFallbackSimilarity(String text1, String text2) {
        // Convertir les textes en minuscules et les diviser en mots
        String[] words1 = text1.toLowerCase().split("\\W+");
        String[] words2 = text2.toLowerCase().split("\\W+");

        // Créer des ensembles pour les mots uniques
        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));

        // Calculer l'intersection et l'union
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        // Si l'union est vide, retourner 0
        if (union.isEmpty()) {
            return 0.0;
        }

        // Calculer le coefficient de Jaccard: intersection / union
        return (double) intersection.size() / union.size();
    }
}