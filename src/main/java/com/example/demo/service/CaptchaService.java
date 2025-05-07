package com.example.demo.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

@Service
public class CaptchaService {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SECRET_KEY = "6LdVOC8rAAAAAEvjYnPJEegjkfhEkFdS0AUjqIAy"; // Ta clé secrète

    public boolean validateCaptcha(String captchaResponse) {
        String url = CAPTCHA_URL + "?secret=" + SECRET_KEY + "&response=" + captchaResponse;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Vérifie si la réponse est valide
        return response.getBody().contains("\"success\": true");
    }
}

