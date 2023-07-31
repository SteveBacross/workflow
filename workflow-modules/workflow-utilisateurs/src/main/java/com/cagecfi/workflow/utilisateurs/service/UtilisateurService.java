/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.service;

import com.cagecfi.utilisateur.feign.BaseAppUserRestClient;
import com.cagecfi.utilisateur.model.AppUser;
import com.cagecfi.utilisateur.model.BaseUtilisateurRole;
import com.cagecfi.utilisateur.repository.BaseUtilisateurRepository;
import com.cagecfi.utilisateur.service.BaseUtilisateurService;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import com.cagecfi.workflow.utilisateurs.feign.AppUserRestClient;
import com.cagecfi.workflow.utilisateurs.model.UtilisateurRole;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class UtilisateurService extends BaseUtilisateurService<Utilisateur, UtilisateurRole>{

    public UtilisateurService(UtilisateurRepository utilisateurRepository, AppUserRestClient appUserRestClient) {
        super(utilisateurRepository, appUserRestClient);
    }

    @Override
    public UtilisateurRole initUtilisateurRole() {
        UtilisateurRole user = new UtilisateurRole(null, "Admin", "user", "user1", "1234");
        user.setEmail("user1");
        user.setTelephone("user1");
        user.setUserWithPhone(false);
        user.setActif(true);
        return user;
    }

    @Override
    public UtilisateurRole toUtilisateurRole(Utilisateur utilisateur, AppUser appUser) {
        return new UtilisateurRole(utilisateur, appUser);
    }

  

    @Override
    public Utilisateur toUtilisateur(UtilisateurRole userRole) {
        return new Utilisateur(userRole);
    }


}
