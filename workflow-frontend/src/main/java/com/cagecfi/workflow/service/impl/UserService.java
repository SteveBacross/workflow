/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author USER
 */
@Service
public class UserService implements UserDetailsService{

    @Autowired
    AppUserHttpClient userHttpClient;

    private String token;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        User utilisateur = null;
        try {
            user = userHttpClient.findByUsername(username);
            utilisateur = userHttpClient.profile(token);
            List<String> strings = userHttpClient.roleList(utilisateur.getId(), token);
            if (strings != null) {
                user.getRolesString().addAll(strings);
            }
            user.getRolesString().add("SIGNED_IN");

            /**
             *  Faire une requête pour récuperer la liste des groupes auxquels l'utilisateur appartient
             *  Ensuite récuperer la liste des roles associés à ces groupes
             *  Et rajouter ses listes de roles dans la liste des roles de l'utilisateur.
             */
        } catch (IOException ex){
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (user != null){
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }

    public void setToken(String token) {
        this.token = token;
    }

}
