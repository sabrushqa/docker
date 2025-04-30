package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "offres")
@Getter
@Setter
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 1000)
    private String description;

    private String lieu;

    private String typeContrat; // ex: CDI, CDD, stage...

    private String niveauExperience; // ex: Junior, Confirmé, Senior...

    private String salaire;

    private String secteur; // ex: Informatique, RH, Finance...

    private LocalDate datePublication;

    private LocalDate dateExpiration;

    // ✅ Recruteur lié à l'offre
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruteur_id")
    private Recruteur recruteur;
}
