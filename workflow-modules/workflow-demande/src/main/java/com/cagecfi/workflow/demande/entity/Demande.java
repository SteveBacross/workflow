/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.demande.entity;

import com.cagecfi.workflow.demande.enumeration.Priorite;
import com.cagecfi.workflow.demande.enumeration.Status;
import com.cagecfi.workflow.demande.enumeration.TypeDemande;
import com.cagecfi.workflow.demande.enumeration.Etape;
import com.cagecfi.workflow.demande.enumeration.Nature;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 *
 * @author GEOFFREY MO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DEMANDES")
public class Demande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMERO_DEMANDE")
    private Integer numeroDemande;

    @Column(name = "NATURE")
    @Enumerated(EnumType.STRING)
    private Nature nature;

    @Column(name = "TYPE_DEMANDE")
    @Enumerated(EnumType.STRING)
    private TypeDemande typeDemande;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "PRIORITE")
    @Enumerated(EnumType.STRING)
    private Priorite priorite;

    @Column(name = "ETAPE")
    @Enumerated(EnumType.STRING)
    private Etape etape;

    @Column(name = "LIEU")
    private String lieu;

    @Column(name = "DATE_LIVRAISON_SOUHAITE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraisonSouhaite;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    @Column(name = "DATE_DEMANDE")
    private Date dateDemande;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @UpdateTimestamp
    @Column(name = "DATE_MISE_A_JOUR")
    private Date dateMiseAJour;

    @Column(name = "ID_UTILISATEUR")
    private Long idUtilisateur;

    @Column(name = "ID_DEPARTEMENT")
    private Long idDepartement;

}
