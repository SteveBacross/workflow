package com.cagecfi.workflow.model;

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
