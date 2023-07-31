/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.views.validation;

import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Demande;
import com.cagecfi.workflow.model.DetailDemande;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.service.impl.DemandeHttpClient;
import com.cagecfi.workflow.service.impl.DetailDemandeHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import com.cagecfi.workflow.views.demande.DemandePanel;
import com.cagecfi.workflow.views.validation.ValidationDmdFinalPanel;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author dell
 */
public class ValidationDirectionPanel extends Panel {

    @SpringBean
    private DemandeHttpClient demandeHttpClient;
    @SpringBean
    private DetailDemandeHttpClient detailDemandeHttpClient;
    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private Demande demande;
    private DetailDemande detailDemande;

    private List<Demande> demandes;
    private List<DetailDemande> detailDemandes = new LinkedList();

    private List<Demande> listParcours = new LinkedList();

//    private List list = Collections.emptyList();
    private List list = new LinkedList();
    private String nomDemandeur;
    private Utilisateur demandeur = new Utilisateur();
    private boolean vide = true;

    private SecureWebSession session = (SecureWebSession) getSession();

    public ValidationDirectionPanel(String id, WebMarkupContainer container) {
        super(id);
        addComponents(container);
    }

    private void addComponents(WebMarkupContainer container) {

        if (session != null) {
            session.getAttribute("SESSION_USER_ID");
        }
        WebMarkupContainer containerGlobal = new WebMarkupContainer("containerGlobal");
        containerGlobal.setOutputMarkupId(true);
        add(containerGlobal);

        FeedbackPanel feedbackPanel = new FeedbackPanel("alert");
        feedbackPanel.setOutputMarkupId(true);
        containerGlobal.add(feedbackPanel);

        WebMarkupContainer emptyList = new WebMarkupContainer("emptyList");

        //Recuperation des demandes par direction
        List<Demande> listTest = new LinkedList();
        try {
            if (session.getAttribute("SESSION_USER_DEPARTMENT_ID") != null) {
                listTest = demandeHttpClient.listDemandeDirection((Long) session.getAttribute("SESSION_USER_DEPARTMENT_ID"));
            }
        } catch (IOException ex) {
            Logger.getLogger(ValidationDirectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Test du contenu de la liste
        if (listTest.isEmpty()) {
            emptyList.setVisible(true);
        } else {
            emptyList.setVisible(false);
        }

        ListView listViewNew = new ListView<Demande>("listViewNew", listTest) {
            @Override
            protected void populateItem(ListItem<Demande> li) {
                try {
                    Utilisateur demandeur = utilisateurHttpClient.getUtilisateurById(li.getModelObject().getIdUtilisateur());
                    nomDemandeur = demandeur.getNom() + " " + demandeur.getPrenoms();

                    li.add(new Label("priorite", new PropertyModel(li.getModelObject(), "priorite")));
                    li.add(new Label("dateDemandeValue", new PropertyModel(li.getModelObject(), "dateDemandeValue")));
                    li.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                    li.add(new Label("nomDemandeur", nomDemandeur));

                    Link collapsebtn = new Link("collapsebtn") {
                        @Override
                        public void onClick() {
                        }

                        @Override
                        protected void onComponentTag(ComponentTag tag) {
                            super.onComponentTag(tag); //To change body of generated methods, choose Tools | Templates.
                            tag.put("href", "#cardCollpase" + (li.getIndex() + 1));
                            tag.put("aria-controls", "cardCollpase" + (li.getIndex() + 1));
                        }
                    };
                    li.add(collapsebtn);

                    WebMarkupContainer collapse = new WebMarkupContainer("collapse");
                    li.add(collapse.setMarkupId("cardCollpase" + (li.getIndex() + 1)));

//                    collapse.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                    collapse.add(new Label("nature", new PropertyModel(li.getModelObject(), "nature")));
                    collapse.add(new Label("dateLivraisonSouhaiteValue", new PropertyModel(li.getModelObject(), "dateLivraisonSouhaiteValue")));
                    collapse.add(new Label("typeDemande", li.getModelObject().getTypeDemande() != null ? li.getModelObject().getTypeDemande() : "Demande formation"));
//                    collapse.add(new Label("typeDemande", new PropertyModel(li.getModelObject(), "typeDemande")));
                    collapse.add(new Label("status", new PropertyModel(li.getModelObject(), "status")));
                    collapse.add(new Label("etape", new PropertyModel(li.getModelObject(), "etape")));
                    collapse.add(new Label("lieu", new PropertyModel(li.getModelObject(), "lieu")));

                    collapse.add(new AjaxLink<Void>("valider") {
                        @Override
                        @SneakyThrows
                        public void onClick(AjaxRequestTarget art) {
                            demandeHttpClient.validation(li.getModelObject().getId());
                            feedbackPanel.add(new AttributeModifier("class", "alert alert-success"));
                            success("Demanande validée");
//                            art.add(container, feedbackPanel);
//                            container.addOrReplace(new ValidationDirectionPanel(EnumConstantes.CONTENT_ID, container));
                            art.add(containerGlobal);
                        }

                    });
                    collapse.add(new AjaxLink<Void>("refuser") {
                        @Override
                        @SneakyThrows
                        public void onClick(AjaxRequestTarget art) {
                            demandeHttpClient.refus(li.getModelObject().getId());
                            feedbackPanel.add(new AttributeModifier("class", "alert alert-danger"));
                            error("Demanande non validée");
//                            art.add(container, feedbackPanel);
//                            container.addOrReplace(new ValidationDirectionPanel(EnumConstantes.CONTENT_ID, container));

                            art.add(containerGlobal);
                        }
                    });

                    detailDemandes = detailDemandeHttpClient.detailsByDemande(li.getModelObject().getId());

                    collapse.add(new ListView<DetailDemande>("detailListView", detailDemandes) {
                        @Override
                        protected void populateItem(ListItem<DetailDemande> li) {
                            li.add(new Label("quantite", new PropertyModel(li.getModelObject(), "quantite")));
                            li.add(new Label("description", new PropertyModel(li.getModelObject(), "description")));
                            li.add(new Label("ligneBudgetaire", new PropertyModel(li.getModelObject(), "ligneBudgetaire")));
                            li.add(new Label("idDemande", new PropertyModel(li.getModelObject(), "idDemande")));

                        }
                    });
                    li.setOutputMarkupPlaceholderTag(true);
                } catch (IOException ex) {
                    Logger.getLogger(ValidationDirectionPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        listViewNew.setOutputMarkupId(true);
        containerGlobal.add(listViewNew);
        containerGlobal.add(emptyList);

    }

}
