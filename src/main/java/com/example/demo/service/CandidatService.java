package com.example.demo.service;

import com.example.demo.Repository.CandidatRepository;
import com.example.demo.model.Candidat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidatService {

    private final CandidatRepository candidatRepository;

    @Transactional  // ✅ important
    public Candidat getCandidatByEmail(String email) {
        return candidatRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidat introuvable avec l'email : " + email));
    }
}
