package com.example.demo.Repository;

import com.example.demo.model.Offre;
import com.example.demo.model.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByRecruteurId(Long recruteurId);
    List<Offre> findByRecruteur(Recruteur recruteur);
    List<Offre> findTop5ByRecruteurOrderByDatePublicationDesc(Recruteur recruteur);
    @Query("""
    SELECT o FROM Offre o
    WHERE (:secteur IS NULL OR o.secteur = :secteur)
    AND (:typeContrat IS NULL OR o.typeContrat = :typeContrat)
    AND (:lieu IS NULL OR o.lieu = :lieu)
    AND (:entreprise IS NULL OR o.recruteur.entreprise = :entreprise)
    ORDER BY o.datePublication DESC
    """)
    List<Offre> rechercher(
            @Param("secteur") String secteur,
            @Param("typeContrat") String typeContrat,
            @Param("lieu") String lieu,
            @Param("entreprise") String entreprise
    );
}
