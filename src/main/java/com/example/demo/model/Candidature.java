package com.example.demo.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures")
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

    // ✅ Constructeur par défaut
    public Candidature() {
        this.dateCandidature = LocalDateTime.now();
        this.statut = "En attente";
    }

    // ✅ Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public LocalDateTime getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(LocalDateTime dateCandidature) {
        this.dateCandidature = dateCandidature;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }






    public Double getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(Double matchingScore) {
        this.matchingScore = matchingScore;
    }
}

