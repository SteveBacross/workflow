/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.entity;

/**
 *
 * @author USER
 */
public enum Titre {
    DIRECTEUR("directeur"), 
    CHEF("chef"), 
    ASSISTANT("assistant"),
    EMPLOYE("employe");

    private final String libTitre;
    
    Titre(String lib){
        this.libTitre = lib;
    }
    
    public String getLibTitre(){
        return libTitre;
    }

}
