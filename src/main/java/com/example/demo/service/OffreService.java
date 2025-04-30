package com.example.demo.service;

import com.example.demo.model.Offre;
import com.example.demo.model.Recruteur;
import com.example.demo.Repository.OffreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;

    @Transactional
    public Offre creerOffre(Offre offre, Recruteur recruteur) {
        offre.setDatePublication(LocalDate.now());
        offre.setRecruteur(recruteur);
        return offreRepository.save(offre);
    }
    public List<Offre> getToutesLesOffres() {
        return offreRepository.findAll();
    }
    public List<Offre> rechercherOffres(String secteur, String typeContrat, String lieu, String entreprise) {
        return offreRepository.rechercher(secteur, typeContrat, lieu, entreprise);
    }



    public List<Offre> getOffresParRecruteur(Long recruteurId) {
        return offreRepository.findByRecruteurId(recruteurId);
    }
    public void supprimerOffre(Long id) {
        offreRepository.deleteById(id);
    }


}
