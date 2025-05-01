package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public void sendInterviewConfirmation(String to, String candidateName, String recruiterName,
                                          String position, String date, String time, String zoomLink) throws MessagingException {
        String subject = "Confirmation d'entretien pour le poste de " + position;

        String htmlBody =
                "<html>" +
                        "<body>" +
                        "<h2>Confirmation d'entretien</h2>" +
                        "<p>Bonjour " + candidateName + ",</p>" +
                        "<p>Nous sommes heureux de vous informer que votre candidature pour le poste de <strong>" + position + "</strong> a été retenue.</p>" +
                        "<p>" + recruiterName + " souhaite vous rencontrer pour un entretien le <strong>" + date + "</strong> à <strong>" + time + "</strong>.</p>" +
                        "<p>Veuillez utiliser le lien Zoom suivant pour rejoindre la réunion:</p>" +
                        "<p><a href='" + zoomLink + "'>" + zoomLink + "</a></p>" +
                        "<p>Si vous avez des questions ou si vous devez modifier l'heure de l'entretien, n'hésitez pas à nous contacter.</p>" +
                        "<p>Cordialement,<br>L'équipe de recrutement</p>" +
                        "</body>" +
                        "</html>";

        sendEmail(to, subject, htmlBody);
    }
}