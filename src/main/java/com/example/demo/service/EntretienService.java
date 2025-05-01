package com.example.demo.service;

import com.example.demo.model.Candidature;
import com.example.demo.model.Entretien;
import com.example.demo.repository.EntretienRepository;
import com.example.demo.utils.ZoomApiUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntretienService {

    private final EntretienRepository entretienRepository;
    private final ZoomApiUtil zoomApiUtil;
    private final EmailService emailService;

    public void envoyerLienZoomPourEntretien(Candidature candidature) throws MessagingException {
        Optional<Entretien> optionalEntretien = entretienRepository.findByCandidature(candidature);

        Entretien entretien;

        if (optionalEntretien.isPresent()) {
            entretien = optionalEntretien.get();
            System.out.println("Entretien existant trouvé.");
        } else {
            System.out.println("Aucun entretien trouvé, création d’un nouvel entretien.");
            entretien = new Entretien();
            entretien.setCandidature(candidature);
            entretien.setDateEntretien(LocalDateTime.now().plusDays(1)); // ou une date personnalisée
            entretien.setDureeMinutes(30); // par exemple
        }

        LocalDateTime entretienDateTime = entretien.getDateEntretien();
        ZonedDateTime entretienZonedDateTime = entretienDateTime.atZone(ZoneId.of("Europe/Paris"));
        var candidat = candidature.getCandidat();
        var candidateName = candidat.getNom();
        var candidateEmail = candidat.getEmail();
        var recruiterName = "Recruteur";
        var position = candidat.getSpecialite();

        // Appel à l'API Zoom
        var zoomLink = zoomApiUtil.createZoomMeeting(entretienZonedDateTime);
        System.out.println("#####################################################");
        if (zoomLink == null) return;

        var formattedDate = entretienDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        var formattedTime = entretienDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        emailService.sendInterviewConfirmation(
                candidateEmail, candidateName, recruiterName, position,
                formattedDate, formattedTime, zoomLink
        );

        entretien.setLienZoom(zoomLink);
        entretienRepository.save(entretien);
    }

}
