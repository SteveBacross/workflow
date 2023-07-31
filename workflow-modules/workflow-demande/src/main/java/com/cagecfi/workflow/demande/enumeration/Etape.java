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
public enum Etape {
    
    Responsable("Responsable"),
    Intermediaire("Intermediaire"),
    Direction_Generale("Direction Generale"),
    Fin_de_traitement("Fin de traitement");
    
    private final String etape;
    
    Etape (String libEtape){
        this.etape = libEtape;
    }
    
    public String etape(){
        return etape;
    }
}
