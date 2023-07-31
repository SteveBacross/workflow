/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.utilisateurs.repository;

import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import com.cagecfi.workflow.utilisateurs.entity.UtilisateurDepartement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author dell
 */
public interface UtilisateurDepartementRepository extends JpaRepository<UtilisateurDepartement, Long> {

    @Query("SELECT ud FROM UtilisateurDepartement ud JOIN FETCH ud.utilisateur" +
            " JOIN FETCH ud.departement d join fetch d.niveau WHERE ud.utilisateur.id = :idUtilisateur")
    Optional<UtilisateurDepartement> findByIdUtilisateurFull(Long idUtilisateur);

    @Query("SELECT ud FROM UtilisateurDepartement ud JOIN FETCH ud.utilisateur " +
            "JOIN FETCH ud.departement d JOIN FETCH d.niveau WHERE d.id = :id")
    List<UtilisateurDepartement> findByIdDepartementFull(Long id);

    /*@Query("DELETE FROM UtilisateurDepartement where UtilisateurDepartement.id = :idUd")
    void deleteUDep(Iterable<? extends Long> idUd);*/

    @Query("SELECT ud FROM UtilisateurDepartement ud JOIN FETCH ud.utilisateur JOIN FETCH ud.departement")
    List<UtilisateurDepartement> findUdAllFull();

}
