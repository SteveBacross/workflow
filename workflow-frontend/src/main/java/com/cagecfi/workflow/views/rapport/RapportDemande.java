/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.views.rapport;

import com.cagecfi.workflow.service.impl.RapportDemandeHttpClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 *
 * @author dell
 */
public class RapportDemande extends Panel {

    private Date dateFin;
    private Date dateDebut;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @SpringBean
    private RapportDemandeHttpClient rapportDemandeHttpClient;

    public RapportDemande(String id, WebMarkupContainer container) {
        super(id);
        addComponents(container);
    }

    private void addComponents(WebMarkupContainer container) {

        WebMarkupContainer containerGlobal = new WebMarkupContainer("containerGlobal");
        containerGlobal.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("alert");
        feedbackPanel.setOutputMarkupId(true);

        DateTextField dateDebutTXT = new DateTextField("dateDebut", "yyyy-MM-dd");
        dateDebutTXT.setRequired(true);
        dateDebutTXT.setLabel(Model.of("Date de Début"));

        DateTextField dateFinTXT = new DateTextField("dateFin", "yyyy-MM-dd");
        dateFinTXT.setRequired(true);
        dateFinTXT.setLabel(Model.of("Date de Fin"));

        Form form = new Form("form", new CompoundPropertyModel(this)) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                if (dateFin.before(dateDebut) || dateDebut.equals(dateFin)) {
                    feedbackPanel.add(new AttributeModifier("class", "alert alert-warning"));
                    error("La date début doit etre inférieure à la date de fin !");
                } else {
                    try {
                        File file = rapportDemandeHttpClient.getReport(dateDebut, dateFin);
                        if (file != null) {
                            IResourceStream stream = new AbstractResourceStream() {
                                @Override
                                public InputStream getInputStream() throws ResourceStreamNotFoundException {
                                    try {
                                        return new FileInputStream(file);
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(RapportDemande.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return null;
                                }

                                @Override
                                public void close() throws IOException {
                                }
                            };
                            if (stream != null) {
                                getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(stream, "rapport-demande.pdf"));
                            }
                        }
                        feedbackPanel.add(new AttributeModifier("class", "alert alert-danger"));
                        error("Aucune donnée ");
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(RapportDemande.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RapportDemande.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        form.setOutputMarkupId(true);

        form.add(dateDebutTXT);
        form.add(dateFinTXT);
        add(containerGlobal);
        containerGlobal.add(form);
        containerGlobal.add(feedbackPanel);
    }

}
