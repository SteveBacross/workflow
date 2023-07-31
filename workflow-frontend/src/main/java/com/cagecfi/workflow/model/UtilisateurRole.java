package com.cagecfi.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurRole extends IHMModel{

    private Long id;
    private String nom;
    private String prenoms;
    private String username;
    private String name;
    private String password;
    private String passwordOld;
    private String email;
    private String telephone;
    private Boolean actif;

//    private Titre titre;

//    private Long idDepartement;

    private Boolean userWithPhone;

    private Long idAppUser;

//    private Date dateMiseajour;

//    private Date dateLastLogin;

    public String getName() {
        if (this.nom != null && this.prenoms != null) {
            return this.nom + ' ' + this.prenoms;
        } else {
            return this.nom != null ? this.nom : this.prenoms;
        }
    }

    public UtilisateurRole(Long id, String nom, String prenoms, String login) {
        this.id = id;
        this.nom = nom;
        this.prenoms = prenoms;
        this.username = login;
    }

    /*public Utilisateur(F userRole) {
        this((Long)null, userRole.getNom(), userRole.getPrenoms(), userRole.getUsername());
        this.setEmail(userRole.getEmail());
        this.setTelephone(userRole.getTelephone());
        if (userRole.getUserWithPhone() != null && userRole.getUserWithPhone()) {
            this.setUserWithPhone(Boolean.TRUE);
            if (userRole.getTelephone() == null) {
                this.setTelephone(userRole.getUsername());
            }
        } else {
            this.setUserWithPhone(Boolean.FALSE);
            if (userRole.getEmail() == null) {
                this.setEmail(userRole.getUsername());
            }
        }

    }*/

}
