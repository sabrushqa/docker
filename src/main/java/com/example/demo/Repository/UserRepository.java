package com.example.demo.Repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Méthode de base
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Chargement explicite de la relation candidat
    @EntityGraph(attributePaths = {"candidat"})
    Optional<User> findWithCandidatByEmail(String email);

    // Chargement explicite de la relation recruteur
    @EntityGraph(attributePaths = {"recruteur"})
    Optional<User> findWithRecruteurByEmail(String email);

    // Chargement des deux relations
    @EntityGraph(attributePaths = {"candidat", "recruteur"})
    Optional<User> findWithRolesByEmail(String email);
}
