/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.utils;

import com.cagecfi.utilisateur.model.AppRole;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PermissionRole {

    public static List<AppRole> getRoles() {

        List<AppRole> roles = new ArrayList<>();
        //Dash
        roles.add(new AppRole(null, "client", "Client"));
        //parametrages
        roles.add(new AppRole(null, "user", "Utilisateur"));
        roles.add(new AppRole(null, "admin", "Administrateur"));

        return roles;

    }

}
