/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.repository;

import com.cagecfi.utilisateur.repository.BaseUtilisateurRepository;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author GEOFFREY MO
 */
public interface UtilisateurRepository extends BaseUtilisateurRepository<Utilisateur>{
    
    Optional<Utilisateur> findByEmail(String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.nom LIKE %:nom% OR u.telephone LIKE %:tel% OR u.email LIKE %:email%")
    List<Utilisateur> findUtilisateurByParams(@Param("nom") String nom,
                                              @Param("tel") String tel,
                                              @Param("email") String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.nom LIKE %:nom% ")
    List<Utilisateur> findUtilisateurByParamstry(@Param("nom") String nom);

    @Query("SELECT u FROM Utilisateur u WHERE u.id NOT IN :usrWithDep")
    List<Utilisateur> findAllUsrWithNoDep(List<Long> usrWithDep);

    @Query("SELECT u FROM Utilisateur u WHERE u.id IN :usrWithDep")
    List<Utilisateur> findAllUsrByDep(List<Long> usrWithDep);
}
