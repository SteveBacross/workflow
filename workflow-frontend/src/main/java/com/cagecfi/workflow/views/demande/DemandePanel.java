package com.cagecfi.workflow.views.demande;

import com.cagecfi.workflow.composant.DataTableScript;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Demande;
import com.cagecfi.workflow.model.DetailDemande;
import com.cagecfi.workflow.service.impl.DemandeHttpClient;
import com.cagecfi.workflow.service.impl.DetailDemandeHttpClient;
import com.cagecfi.workflow.views.validation.ValidationDmdFinalPanel;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class DemandePanel extends Panel {

    @SpringBean
    private DemandeHttpClient demandeHttpClient;
    @SpringBean
    private DetailDemandeHttpClient detailDemandeHttpClient;

    private Demande demande;
    private DetailDemande detailDemande;

    private List<Demande> demandes;
    private List<DetailDemande> detailDemandes = new LinkedList();

    private SecureWebSession session = (SecureWebSession) getSession();

    public DemandePanel(String id, WebMarkupContainer container) {
        super(id);
        addComponents(container);
    }

    private void addComponents(WebMarkupContainer container) {
        if (session != null) {
            session.getAttribute("SESSION_USER_ID");
            session.getAttribute("SESSION_USER_DEPARTMENT_ID");

        }

        WebMarkupContainer containerNew = new WebMarkupContainer("containerNew");
        containerNew.setOutputMarkupId(true);
        add(containerNew);

        WebMarkupContainer containerModal = new WebMarkupContainer("containerModal");
        containerModal.setOutputMarkupId(true);
        add(containerModal);

        FeedbackPanel feedbackPanel = new FeedbackPanel("alert");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //Datatable
        DataTableScript dts = new DataTableScript("dts", "scroll-horizontal-datatable");
        DataTableScript dts1 = new DataTableScript("dts1", "scroll-horizontal-datatable1");
        DataTableScript dts2 = new DataTableScript("dts2", "scroll-horizontal-datatable2");

        //Boutton pour ajouter une nouvelle demande
        add(new AjaxLink<Void>("addDemande") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                container.addOrReplace(new AjoutDemandePanel(EnumConstantes.CONTENT_ID, container));
                art.add(container);
            }
        });

        //Demandes en cours
        LoadableDetachableModel detachableModelProgress = new LoadableDetachableModel() {
            @Override
            protected Object load() {
                List list = Collections.emptyList();
                try {
                    list = demandeHttpClient.listProgressDemandeDirection((Long) session.getAttribute("SESSION_USER_DEPARTMENT_ID"));

                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                return list;
            }
        };

        //Nouvelles demandes
        LoadableDetachableModel detachableModelNew = new LoadableDetachableModel() {
            @Override
            protected Object load() {
                List list = Collections.emptyList();
                try {
                    list = demandeHttpClient.listNewDemandeDirection((Long) session.getAttribute("SESSION_USER_DEPARTMENT_ID"));

                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                return list;
            }
        };

        //Demandes terminées
        LoadableDetachableModel detachableModelFinish = new LoadableDetachableModel() {
            @Override
            protected Object load() {
                List list = Collections.emptyList();
                try {
                    list = demandeHttpClient.listFinishDemandeDirection((Long) session.getAttribute("SESSION_USER_DEPARTMENT_ID"));
                    System.out.println(session.getAttribute("SESSION_USER_DEPARTMENT_ID"));

                } catch (IOException ex) {
                    Logger.getLogger(ValidationDmdFinalPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                return list;
            }
        };

        //ListView pour les details
        ListView detailListView = new ListView<DetailDemande>("detailListView", detailDemandes) {
            @Override
            protected void populateItem(ListItem<DetailDemande> li) {
                li.add(new Label("quantite", new PropertyModel(li.getModelObject(), "quantite")));
                li.add(new Label("description", new PropertyModel(li.getModelObject(), "description")));
                li.add(new Label("ligneBudgetaire", new PropertyModel(li.getModelObject(), "ligneBudgetaire")));
                li.add(new Label("etat", new PropertyModel(li.getModelObject(), "etat")));
                li.add(new Label("idDemande", new PropertyModel(li.getModelObject(), "idDemande")));

            }
        };
        detailListView.setOutputMarkupId(true);
        containerModal.add(detailListView);

        //ListView en cours
        ListView listViewProgress = new ListView<Demande>("listViewProgress", detachableModelProgress) {
            @Override
            protected void populateItem(ListItem<Demande> li) {
                li.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                li.add(new Label("numeroDemande", new PropertyModel(li.getModelObject(), "numeroDemande")));
                li.add(new Label("nature", new PropertyModel(li.getModelObject(), "nature")));
                li.add(new Label("dateDemandeValue", new PropertyModel(li.getModelObject(), "dateDemandeValue")));
                li.add(new Label("dateLivraisonSouhaiteValue", new PropertyModel(li.getModelObject(), "dateLivraisonSouhaiteValue")));

                li.add(new Label("typeDemande", li.getModelObject().getTypeDemande() != null ? li.getModelObject().getTypeDemande() : "Demande formation"));

//                li.add(new Label("typeDemande", new PropertyModel(li.getModelObject(), "typeDemande")));
                li.add(new Label("status", new PropertyModel(li.getModelObject(), "status")));
                li.add(new Label("etape", new PropertyModel(li.getModelObject(), "etape")));

                li.add(new AjaxLink<Void>("view") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        detailDemandes = detailDemandeHttpClient.detailsByDemande(li.getModelObject().getId());
                        System.out.print(detailDemandes);
                        detailListView.setList(detailDemandes);
                        art.add(containerModal);
                    }

                });
                li.setOutputMarkupPlaceholderTag(true);

//                li.add(new AjaxLink<Void>("delete") {
//                    @Override
//                    @SneakyThrows
//                    public void onClick(AjaxRequestTarget art) {
//                        demandeHttpClient.deleteDemande(li.getModelObject().getId());
//                        feedbackPanel.add(new AttributeModifier("class", "alert alert-success"));
//                        success("Demande suppriméé !");
//                        art.add(container);
//                    }
//
//                });
            }

        };
        listViewProgress.setOutputMarkupId(true);

        //ListView terminées
        ListView listViewFinish = new ListView<Demande>("listViewFinish", detachableModelFinish) {
            @Override
            protected void populateItem(ListItem<Demande> li) {
                li.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                li.add(new Label("numeroDemande", new PropertyModel(li.getModelObject(), "numeroDemande")));
                li.add(new Label("nature", new PropertyModel(li.getModelObject(), "nature")));
                li.add(new Label("dateDemandeValue", new PropertyModel(li.getModelObject(), "dateDemandeValue")));
                li.add(new Label("dateLivraisonSouhaiteValue", new PropertyModel(li.getModelObject(), "dateLivraisonSouhaiteValue")));

                li.add(new Label("typeDemande", li.getModelObject().getTypeDemande() != null ? li.getModelObject().getTypeDemande() : "Demande formation"));

//                li.add(new Label("typeDemande", new PropertyModel(li.getModelObject(), "typeDemande")));
                li.add(new Label("status", new PropertyModel(li.getModelObject(), "status")));
                li.add(new Label("etape", new PropertyModel(li.getModelObject(), "etape")));

                li.add(new AjaxLink<Void>("view") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        detailDemandes = detailDemandeHttpClient.detailsByDemande(li.getModelObject().getId());
                        detailListView.setList(detailDemandes);
                        art.add(containerModal);
                    }
                });
                li.setOutputMarkupPlaceholderTag(true);
            }
        };
        listViewFinish.setOutputMarkupId(true);

        //ListView nouveau
        ListView listViewNew = new ListView<Demande>("listViewNew", detachableModelNew) {
            @Override
            protected void populateItem(ListItem<Demande> li) {
                li.add(new Label("id", new PropertyModel(li.getModelObject(), "id")));
                li.add(new Label("numeroDemande", new PropertyModel(li.getModelObject(), "numeroDemande")));
                li.add(new Label("nature", new PropertyModel(li.getModelObject(), "nature")));
                li.add(new Label("dateDemandeValue", new PropertyModel(li.getModelObject(), "dateDemandeValue")));
                li.add(new Label("dateLivraisonSouhaiteValue", new PropertyModel(li.getModelObject(), "dateLivraisonSouhaiteValue")));

                li.add(new Label("typeDemande", li.getModelObject().getTypeDemande() != null ? li.getModelObject().getTypeDemande() : "Demande formation"));

//                li.add(new Label("typeDemande", new PropertyModel(li.getModelObject(), "typeDemande")));
                li.add(new Label("status", new PropertyModel(li.getModelObject(), "status")));
                li.add(new Label("etape", new PropertyModel(li.getModelObject(), "etape")));

                li.add(new AjaxLink<Void>("view") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        detailDemandes = detailDemandeHttpClient.detailsByDemande(li.getModelObject().getId());
                        System.out.print(detailDemandes);
                        detailListView.setList(detailDemandes);
                        art.add(containerModal);
                    }
                });

                li.add(new AjaxLink<Void>("edit") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        container.addOrReplace(new EditDemandePanel(EnumConstantes.CONTENT_ID, li.getModelObject(), detailDemandes, container));
                        art.add(container);
                    }
                });
                li.setOutputMarkupPlaceholderTag(true);

                li.add(new AjaxLink<Void>("delete") {
                    @Override
                    @SneakyThrows
                    public void onClick(AjaxRequestTarget art) {
                        demandeHttpClient.deleteDemande(li.getModelObject().getId());
                        container.addOrReplace(new DemandePanel(EnumConstantes.CONTENT_ID, container));
                        art.add(container);
                    }
                });
            }
        };
        listViewNew.setOutputMarkupId(true);

        //Datatable
        dts.add(listViewNew);
        dts1.add(listViewProgress);
        dts2.add(listViewFinish);

        containerNew.add(dts);
        add(dts1);
        add(dts2);
    }
}
