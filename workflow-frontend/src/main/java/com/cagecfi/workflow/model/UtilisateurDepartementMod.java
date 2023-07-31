package com.cagecfi.workflow.model;

import java.util.LinkedList;
import lombok.Data;

import java.util.List;

@Data
public class UtilisateurDepartementMod extends IHMModel{

    private Long id;
    private Departement departement;
    private List<Utilisateur> utilisateurList = new LinkedList<>();

}
