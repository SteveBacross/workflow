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
public enum Priorite {
    
    Faible("Faible"),
    Moyen("Moyen"),
    Bloquant("Bloquant");
    
    private final String priorite;
    
    Priorite(String libPriorite){
        this.priorite = libPriorite;
    }
    
    public String priorite(){
        return priorite;
    }
    
}
