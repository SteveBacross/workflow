/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.entity;

import com.cagecfi.utilisateur.entity.BaseUtilisateur;
import com.cagecfi.utilisateur.model.BaseUtilisateurRole;
import com.cagecfi.workflow.utilisateurs.model.UtilisateurRole;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Data
@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "UTILISATEURS")
public class Utilisateur extends BaseUtilisateur<BaseUtilisateurRole>{

    @Column(name = "TITRE")
    @Enumerated(EnumType.STRING)
    private Titre titre;


    public Utilisateur(UtilisateurRole utilisateurRole) {
        super(utilisateurRole);
    }

}
