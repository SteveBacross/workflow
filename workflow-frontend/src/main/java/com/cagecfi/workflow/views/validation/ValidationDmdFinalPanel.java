/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.views.validation;

import com.cagecfi.workflow.composant.DataTableScript;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Demande;
import com.cagecfi.workflow.model.DetailDemande;
import com.cagecfi.workflow.service.impl.DemandeHttpClient;
import com.cagecfi.workflow.service.impl.DetailDemandeHttpClient;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author dell
 */
public class ValidationDmdFinalPanel extends Panel {

    @SpringBean
    private DemandeHttpClient demandeHttpClient;

    @SpringBean
    private DetailDemandeHttpClient detailDemandeHttpClient;

    private Demande demande;
    private DetailDemande detailDemande;

    private List<Demande> demandes;
    private List<DetailDemande> detailDemandes;

    private Long idDemande = 0L;

    public ValidationDmdFinalPanel(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer containerModal = new WebMarkupContainer("containerModal");
        containerModal.setOutputMarkupId(true);
        add(containerModal);

        WebMarkupContainer containerList = new WebMarkupContainer("containerList");
        containerList.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("alert");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        ListView detailListView = new ListView<DetailDemande>("detailListView", detailDemandes) {
            @Override
            protected void populateItem(ListItem<DetailDemande> li) {
                li.add(new Label("quantite", new PropertyModel(li.getModelObject(), "quantite")));
                li.add(new Label("description", new PropertyModel(li.getModelObject(), "description")));
                li.add(new Label("ligneBudgetaire", new PropertyModel(li.getModelObject(), "ligneBudgetaire")));
                li.add(new Label("idDemande", new PropertyModel(li.getModelObject(), "idDemande")));
                li.add(new Label("etat", new PropertyModel(li.getModelObject(), "etat")));
                idDemande = li.getModelObject().getIdDemande();

            }
        };
        detailListView.setOutputMarkupId(true);
        containerModal.add(detailListView);

        LoadableDetachableModel detachableModel = new LoadableDetachableModel() {
            @Override
            protected Object load() {
                List list = Collections.emptyList();
                try {
                    list = demandeHttpClient.listDmdFinale();

                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                return list;
            }
        };

        ListView listView = new ListView<Demande>("listView", detachableModel) {
            @Override
            protected void populateItem(ListItem<Demande> li) {

                li.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                li.add(new Label("numeroDemande", new PropertyModel(li.getModelObject(), "numeroDemande")));
                li.add(new Label("typeDemande", li.getModelObject().getTypeDemande() != null ? li.getModelObject().getTypeDemande() : "Demande formation"));
//                li.add(new Label("typeDemande", new PropertyModel(li.getModelObject(), "typeDemande")));
                li.add(new Label("dateLivraisonSouhaiteValue", new PropertyModel(li.getModelObject(), "dateLivraisonSouhaiteValue")));
                li.add(new Label("dateMiseAJourValue", new PropertyModel(li.getModelObject(), "dateMiseAJourValue")));
                li.add(new Label("status", new PropertyModel(li.getModelObject(), "status")));
                li.add(new Label("nature", new PropertyModel(li.getModelObject(), "nature")));
                li.add(new Label("priorite", new PropertyModel(li.getModelObject(), "priorite")));
                li.add(new Label("lieu", new PropertyModel(li.getModelObject(), "lieu")));
                li.add(new Label("departement", new PropertyModel(li.getModelObject(), "departement.libelle")));
                li.add(new Label("dateDemandeValue", new PropertyModel(li.getModelObject(), "dateDemandeValue")));
                li.add(new Label("idUtilisateur", new PropertyModel(li.getModelObject(), "idUtilisateur")));
                li.add(new Label("etape", new PropertyModel(li.getModelObject(), "etape")));

                li.add(new AjaxLink<Void>("clickLink") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        detailDemandes = detailDemandeHttpClient.getDetailsAcceptByDemande(li.getModelObject().getId());
                        detailListView.setList(detailDemandes);
                        art.add(containerModal);
                    }

                });
                li.setOutputMarkupPlaceholderTag(true);
            }

        };
        listView.setOutputMarkupId(true);

        DataTableScript dts = new DataTableScript("dts", "scroll-horizontal-datatable");
        add(dts);
        dts.add(containerList);
        containerList.add(listView);

        add(new AjaxLink<Void>("valider") {
            @Override
            @SneakyThrows
            public void onClick(AjaxRequestTarget art) {
                try {
                    demandeHttpClient.validation(idDemande);
                    feedbackPanel.add(new AttributeModifier("class", "alert alert-success"));
                    success("Demande validée !");
                    art.add(containerList, feedbackPanel);

                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }
        });

        add(new AjaxLink<Void>("refuser") {
            @Override
            @SneakyThrows
            public void onClick(AjaxRequestTarget art) {
                try {
                    demandeHttpClient.refus(idDemande);
                    feedbackPanel.add(new AttributeModifier("class", "alert alert-danger"));
                    info("Demanande rejéttée");
                    art.add(containerList, feedbackPanel);
                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }
        });

    }

}
