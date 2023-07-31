/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.demande.entity;

import com.cagecfi.workflow.demande.enumeration.Etat;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author GEOFFREY MO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DETAIL_DEMANDES")
public class DetailDemande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_DEMANDE")
    private Long idDemande;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "QUANTITE")
    private Integer quantite;

    @Column(name = "LIGNE_BUDGETAIRE")
    private String ligneBudgetaire;
    
    @Column(name="ETAT")
    @Enumerated(EnumType.STRING)
    private Etat etat = Etat.Non_traité;
}
