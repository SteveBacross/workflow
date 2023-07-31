/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.model;

import com.cagecfi.utilisateur.model.AppUser;
import com.cagecfi.utilisateur.model.BaseUtilisateurRole;
import com.cagecfi.workflow.utilisateurs.entity.Titre;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import lombok.Data;

/**
 *
 * @author USER
 */
@Data
public class UtilisateurRole extends BaseUtilisateurRole<Utilisateur> {

    private Titre titre;
    private Long idDepartement;
    private String passwordOld;
    
    public UtilisateurRole() {
        super();
    }

    public UtilisateurRole(Long id, String nom, String prenoms, String username, String password) {
        super(id, nom, prenoms, username, password);
    }

    public UtilisateurRole(Utilisateur user, AppUser dbApp) {
       super(user, dbApp);
    }

}
