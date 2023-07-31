package com.cagecfi.workflow.utilisateurs.model;

import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UtilisateurDepartementMod implements Serializable {

    private Long id;
    private Departement departement;
    private List<Utilisateur> utilisateurList;

}
