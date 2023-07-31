/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.model;

import lombok.Data;

/**
 *
 * @author USER
 */
@Data
public class UtilisateurDepartement extends IHMModel {

    private Long id;
    private Departement departement;
    private Utilisateur utilisateur;

}
