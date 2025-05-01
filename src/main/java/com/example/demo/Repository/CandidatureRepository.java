package com.example.demo.Repository;

import com.example.demo.model.Candidature;
import com.example.demo.model.Candidat;
import com.example.demo.model.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByCandidat(Candidat candidat);
    List<Candidature> findByOffre_Recruteur(Recruteur recruteur);

}


