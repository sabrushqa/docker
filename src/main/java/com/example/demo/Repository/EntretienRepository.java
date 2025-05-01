package com.example.demo.repository;

import com.example.demo.model.Candidature;
import com.example.demo.model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntretienRepository extends JpaRepository<Entretien, Long> {
    Optional<Entretien> findByCandidature(Candidature candidature);
}
