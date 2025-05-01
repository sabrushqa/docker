package com.example.demo.service;

import com.example.demo.model.Candidat;
import com.example.demo.model.Candidature;
import com.example.demo.Repository.CandidatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;

    // Récupérer toutes les candidatures d'un recruteur via son email

    // Récupérer une candidature par son ID
    public Candidature getById(Long id) {
        return candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature introuvable avec l'ID : " + id));
    }

    // Sauvegarder une candidature (si tu en as besoin)
    public Candidature save(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }



    public List<Candidature> findByCandidat(Candidat candidat) {
        return candidatureRepository.findByCandidat(candidat);
    }

}
