/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.model;

import java.io.Serializable;
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
public class DetailDemande implements Serializable{
    
    private Long id;
    private Long idDemande;
    private String description;
    private String ligneBudgetaire;
    private Integer quantite;
    private String etat;
    
}
