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
public enum Nature {

    Matériel("Materiel et fournitures"),
    Formation("Formation");

    private final String nature;

    Nature(String libNature) {
        this.nature = libNature;

    }
    
     public String nature(){
        return nature;
    }
}
