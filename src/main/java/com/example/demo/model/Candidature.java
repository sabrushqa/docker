package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures")
@Data
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relation vers l'offre concernée
    @ManyToOne(optional = false)
    @JoinColumn(name = "offre_id")
    private Offre offre;

    // 🔗 Relation vers le candidat ayant postulé
    @ManyToOne(optional = false)
    @JoinColumn(name = "candidat_id")
    private Candidat candidat;

    // 📅 Date et heure de la candidature
    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateCandidature;

    // 📌 Statut de la candidature : En attente, Acceptée, Refusée...
    @Column(nullable = false)
    private String statut;

    // 📝 Lettre de motivation
    @Column(length = 5000)
    private String lettreMotivation;

    // 📎 URL ou chemin du CV
    private String cvUrl;

    // 🤖 Score IA de correspondance entre candidat et offre (calcul automatique)
    private Double matchingScore;
    private String cv;

    // 📅 Date et heure de l'entretien
    private LocalDateTime dateEntretien;

    // 📎 Lien Zoom pour l'entretien
    private String lienZoom;

    public Candidature() {
        this.dateCandidature = LocalDateTime.now();  // Définit la date actuelle
    }

}
