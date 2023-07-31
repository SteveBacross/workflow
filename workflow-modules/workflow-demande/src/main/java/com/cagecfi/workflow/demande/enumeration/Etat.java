/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.enumeration;

/**
 *
 * @author dell
 */
public enum Etat {

    Approuvé("Approuvé"),
    Rejetté("Rejetté"),
    Non_traité("Non_traité");

    private final String etat;

    Etat(String libEtat) {
        this.etat = libEtat;

    }

    public String etat() {
        return etat;
    }
}
