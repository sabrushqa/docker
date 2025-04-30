package com.example.demo.Repository;

import com.example.demo.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    @Query("SELECT c FROM Candidat c JOIN c.user u WHERE u.email = :email")
    Optional<Candidat> findByUserEmail(@Param("email") String email);

}
