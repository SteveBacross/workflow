/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.model;

/**
 *
 * @author dell
 */
public enum TypeDemande {

    DEMANDE_ACHAT("Demande d'achat"),
    DEMANDE_REPARATION("Demande de reparation");

    private final String value;

    private TypeDemande(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TypeDemande fromValue(String v) {
        for (TypeDemande c : TypeDemande.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
