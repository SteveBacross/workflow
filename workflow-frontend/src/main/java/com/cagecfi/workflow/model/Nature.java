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
public enum Nature {

    Matériel("Materiel et fournitures"),
    Formation("Formation");

    private final String value;

    private Nature(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Nature fromValue(String v) {
        for (Nature c : Nature.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
