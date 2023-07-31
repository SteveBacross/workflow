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
public enum Priorite {

    Faible("Faible"),
    Moyen("Moyen"),
    Bloquant("Bloquant");
    
    private final String value;
    
    private Priorite(String value) {
        this.value = value;
    }
    
     public String value() {
        return value;
    }
     
        public static Priorite fromValue(String v) {
        for (Priorite c : Priorite.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
