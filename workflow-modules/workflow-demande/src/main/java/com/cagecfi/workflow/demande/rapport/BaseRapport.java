/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.rapport;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.dynamicreports.report.builder.ParameterBuilder;
/**
 *
 * @author dell
 */
public class BaseRapport {

    private JasperReportBuilder builder = DynamicReports.report();

    public BaseRapport() {

    }

    public FieldBuilder<?>[] getFields() {
        return null;
    }

    public VariableBuilder<?>[] getVariables() {
        return null;
    }

   public ParameterBuilder<?>[] getParameters() {
        return null;
    }

    public byte[] getReport(String template, List liste) {
        try {
            JasperDesign design = null;
            URL url = getClass().getClassLoader().getResource(template);
            if (url != null) {
                design = JRXmlLoader.load(url.openStream());
            }
            builder.setDataSource(new JRBeanCollectionDataSource(liste));
            builder.setTemplateDesign(design);
            builder.fields(getFields());
            if (getVariables() != null) {
                builder.variables(getVariables());
            }
            if (getParameters() != null) {
                builder.parameters(getParameters());
            }
        } catch (IOException | DRException | JRException ex) {
            Logger.getLogger(BaseRapport.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return JasperExportManager.exportReportToPdf(builder.toJasperPrint());
        } catch (DRException | JRException ex) {
            Logger.getLogger(BaseRapport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
