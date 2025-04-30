package com.example.demo.service;

import com.example.demo.Repository.CandidatRepository;
import com.example.demo.Repository.RecruteurRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Candidat;
import com.example.demo.model.Recruteur;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private RecruteurRepository recruteurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerCandidat(Candidat candidat) {
        validatePasswords(candidat.getPassword(), candidat.getConfirmPassword());

        String email = candidat.getUser().getEmail();
        checkEmailAvailability(email);


        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(candidat.getPassword()));
        user.setRole(Role.CANDIDAT);

        userRepository.save(user);


        candidat.setUser(user);
        candidatRepository.save(candidat);
    }


    public void registerRecruteur(Recruteur recruteur) {
        validatePasswords(recruteur.getPassword(), recruteur.getConfirmPassword());

        String email = recruteur.getUser().getEmail();
        checkEmailAvailability(email);


        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(recruteur.getPassword()));
        user.setRole(Role.RECRUTEUR);

        userRepository.save(user);

        recruteur.setUser(user);
        recruteurRepository.save(recruteur);
    }


    private void validatePasswords(String password, String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }
    }


    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email.");
        }
    }
}
