/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.rapport;

import java.util.Map;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.ParameterBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;

/**
 *
 * @author dell
 */
public class DemandeRapportEtat extends BaseRapport{

    public Map map;

    public DemandeRapportEtat(Map map) {
        this.map = map;
    }

    @Override
    public ParameterBuilder<?>[] getParameters() {
        ParameterBuilder<?> mesParametres[] = new ParameterBuilder<?>[3];
        mesParametres[0] = DynamicReports.parameter("dateDebut", map.get("dateDebut"));
        mesParametres[1] = DynamicReports.parameter("dateFin", map.get("dateFin"));
        mesParametres[2] = DynamicReports.parameter("dateOfDay", map.get("dateOfDay"));
        
        return mesParametres;
    }

    @Override
    public FieldBuilder<?>[] getFields() {
        FieldBuilder<?> mesField[] = new FieldBuilder<?>[5];
        mesField[0] = DynamicReports.field("tailleTotale", DataTypes.integerType());
        mesField[1] = DynamicReports.field("tailleValide", DataTypes.integerType());
        mesField[2] = DynamicReports.field("tailleRefuse", DataTypes.integerType());
        mesField[3] = DynamicReports.field("tailleProgress", DataTypes.integerType());
         mesField[4] = DynamicReports.field("tailleNew", DataTypes.integerType());
      
        return mesField;
    }

}
