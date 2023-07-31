/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author dell
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demande implements Serializable {

    private Long id;
    private String numeroDemande;
    private TypeDemande typeDemande;
    private Date dateLivraisonSouhaiteValue;
    private Date dateDemandeValue;
    private Date dateMiseAJourValue;
    private String status;
    private Priorite priorite;
    private Nature nature;
    private String lieu;
    private Departement departement;
    private Long idUtilisateur;
    private String etape;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public String getDateLivraisonSouhaite() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (this.dateLivraisonSouhaiteValue != null
                && sdf1 != null) {
            return sdf1.format(dateLivraisonSouhaiteValue);
        }
        return null;
    }

    public String getDateDemandeValue() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (this.dateDemandeValue != null
                && sdf1 != null) {
            return sdf1.format(dateDemandeValue);
        }
        return null;
    }

    public String getDateMiseAJourValue() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (this.dateMiseAJourValue != null
                && sdf1 != null) {
            return sdf1.format(dateMiseAJourValue);
        }
        return null;
    }
    
    public Long getIdDepartement() {
        if (this.departement != null) {
            return this.departement.getId();
        }
        return null;
    }
    
}
