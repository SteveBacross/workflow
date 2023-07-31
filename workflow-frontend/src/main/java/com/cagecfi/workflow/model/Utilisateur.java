package com.cagecfi.workflow.model;

import lombok.Data;

@Data
public class Utilisateur extends IHMModel{

    private Long id;
    private String nom;
    private String prenoms;
    private String username;
    private String email;
    private String telephone;

}
