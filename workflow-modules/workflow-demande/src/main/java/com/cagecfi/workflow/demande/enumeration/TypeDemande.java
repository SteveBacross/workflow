/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.enumeration;

import javax.persistence.Enumerated;

/**
 *
 * @author dell
 */
public enum TypeDemande {

    Demande_achat("Demande d'achat"),
    Demande_réparation("Demande de reparation");

    private final String typeDemande;

    private TypeDemande(String type) {

        this.typeDemande = type;
    }

    private String typeDemande() {

        return typeDemande;
    }
    
    public static TypeDemande fromString(String typeDemande) {
        for (TypeDemande b : TypeDemande.values()) {
            if (b.typeDemande.equalsIgnoreCase(typeDemande)) {
                return b;
            }
        }
        return null;
    }

}
