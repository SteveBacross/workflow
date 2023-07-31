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
public enum Status {
    
    Nouveau("Nouveau"),
    En_Cours("En Cours"),
    Validé("Validé"),
    Refusé("Refusé");
    
    private final String status;
    
    Status(String libStatus){
        
        this.status = libStatus;
    }
    
    private String status(){
        return status;
    }
}
