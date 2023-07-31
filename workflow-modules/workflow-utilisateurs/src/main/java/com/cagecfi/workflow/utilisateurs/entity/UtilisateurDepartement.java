/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.utilisateurs.entity;

import com.cagecfi.workflow.departement.entity.Departement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dell
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UTILISATEUR_DEPARTEMENTS")
public class UtilisateurDepartement implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEPARTEMENT")
    private Departement departement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UTILISATEUR")
    private Utilisateur utilisateur;
    
    @Column(name = "DATE_DEBUT")
    private Date dateDebut;
    
    @Column(name = "DATE_FIN")
    private Date dateFin;

    public UtilisateurDepartement(Departement departement, Utilisateur utilisateur) {
        this.departement = departement;
        this.utilisateur = utilisateur;
    }
}
