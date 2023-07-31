/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.views.demande;

import com.cagecfi.workflow.model.CloneDetail;
import com.cagecfi.workflow.model.Demande;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.DetailDemande;
import com.cagecfi.workflow.model.Nature;
import com.cagecfi.workflow.model.Priorite;
import com.cagecfi.workflow.model.TypeDemande;
import com.cagecfi.workflow.model.User;
import com.cagecfi.workflow.service.impl.DemandeHttpClient;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.DetailDemandeHttpClient;
import com.cagecfi.workflow.service.impl.UserService;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author dell
 */
public class AjoutDemandePanel extends Panel {

    private User user;
    @SpringBean
    private DemandeHttpClient demandeHttpClient;
    @SpringBean
    private DetailDemandeHttpClient detailDemandeHttpClient;
    @SpringBean
    private UserService userService;
    @SpringBean
    private DepartementHttpClient departementHttpClient;

    private Demande demande;
    private List<DetailDemande> detailDemandes;
    private DetailDemande detailDemande;
    private String typeDemande;
    private String priorite;
    private String nature;

    private int quantite;
    private String description;
    private String ligneBudgetaire;

    private List<CloneDetail> cloneDetails;

    private List<Departement> listDepartements = new LinkedList();

    private SecureWebSession session = (SecureWebSession) getSession();

    private WebMarkupContainer containerTypeDemande = new WebMarkupContainer("containerTypeDemande");
    private Nature condition;

    public AjoutDemandePanel(String id, WebMarkupContainer container) {
        super(id);
        Demande demande = new Demande();
        DetailDemande detailDemande = new DetailDemande();
        detailDemandes = new LinkedList();
        detailDemandes.add(new DetailDemande());
        cloneDetails = new LinkedList();
        cloneDetails.add(new CloneDetail());
        addComponents(container);

    }

    private void addComponents(WebMarkupContainer container) {

        if (session != null) {
            session.getAttribute("SESSION_USER_ID");
            session.getAttribute("SESSION_USER_DEPARTMENT_ID");
        }

        WebMarkupContainer globalContainer = new WebMarkupContainer("globalContainer");
        globalContainer.setOutputMarkupId(true);
        add(globalContainer);

        FeedbackPanel feedbackPanel = new FeedbackPanel("alert");
        feedbackPanel.setOutputMarkupId(true);

//        WebMarkupContainer containerTypeDemande = new WebMarkupContainer("containerTypeDemande");
        containerTypeDemande.setOutputMarkupId(true);

        WebMarkupContainer forClone = new WebMarkupContainer("forClone");
        forClone.setOutputMarkupId(true);

        Form<Void> form = new Form("form", new CompoundPropertyModel(this));

        //ListView de la ligne à dupliquer
        ListView listViewClone = new ListView<DetailDemande>("cloneDetail", detailDemandes) {
            @Override
            protected void populateItem(ListItem<DetailDemande> li) {

                TextField description = new TextField("description", new PropertyModel(li.getModelObject(), "description"));
                description.setOutputMarkupId(true);
                description.setLabel(Model.of("description"));
                li.add(description);

//                TextField ligneBudget = new TextField("ligneBudgetaire", new PropertyModel(li.getModelObject(), "ligneBudgetaire"));
//                ligneBudget.setLabel(Model.of("ligneBudgetaire"));
//                ligneBudget.setOutputMarkupId(true);
//                li.add(ligneBudget);

                NumberTextField quantite = new NumberTextField("quantite", new PropertyModel(li.getModelObject(), "quantite"));
                quantite.setLabel(Model.of("quantite"));
                quantite.setOutputMarkupId(true);
                li.add(quantite);

                //Buotton d'ajout de ligne supplementaire
                AjaxButton addIntervalleLink = new AjaxButton("cloneButton") {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        detailDemandes.add(new DetailDemande());
                        target.add(forClone);
                    }

                    @Override
                    public void onError(AjaxRequestTarget target) {
                        detailDemandes.add(new DetailDemande());
                        target.add(forClone);
                    }
                };

                //Boutton de suppresion de ligne supplementaire 
                AjaxLink removeIntervalleLink = new AjaxLink<Void>("removeIntervalleLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        detailDemandes.remove(li.getIndex());
                        target.add(forClone);
                    }
                };
                li.add(removeIntervalleLink);
                li.add(addIntervalleLink.setEnabled(false));
                if (li.getIndex() == 0) {
                    description.setRequired(true).setLabel(Model.of("description"));
                    quantite.setRequired(true).setLabel(Model.of("Quantite / Nombre à former"));
                    addIntervalleLink.setEnabled(true);
                    removeIntervalleLink.setEnabled(false).add(new AttributeModifier("class", "btn btn-danger disabled"));
                }
            }

        };
        listViewClone.setReuseItems(true);
        listViewClone.setOutputMarkupId(true);
        forClone.add(listViewClone);

        //Selecteur Direction
        listDepartements = departementHttpClient.getAllDep();
        final IChoiceRenderer<Departement> nomDirection = new ChoiceRenderer<>("libelle");
        DropDownChoice<Departement> departementDDC = new DropDownChoice<Departement>("demande.departement", listDepartements, nomDirection);
        departementDDC.setRequired(true);
        departementDDC.setLabel(Model.of(" Direction "));
        departementDDC.setOutputMarkupId(true);
        departementDDC.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                System.out.println("********** ok *********");
            }
        });

        //Selecteur Nature
        List<Nature> natures = Arrays.asList(Nature.values());
        IChoiceRenderer RND = new ChoiceRenderer("value");
        DropDownChoice<Nature> natureDDC = new DropDownChoice<Nature>("demande.nature", natures, RND);
        natureDDC.setRequired(true);
        natureDDC.setLabel(Model.of(" Nature "));
        natureDDC.setOutputMarkupId(true);

        //Selecteur Type de demande
        List<TypeDemande> typeDemandes = Arrays.asList(TypeDemande.values());
        IChoiceRenderer RENDERER = new ChoiceRenderer("value");
        DropDownChoice<TypeDemande> typeDemandeDDC = new DropDownChoice<TypeDemande>("demande.typeDemande", typeDemandes, RENDERER);
        typeDemandeDDC.setOutputMarkupId(true);
        typeDemandeDDC.setEnabled(false);

        //Selecteur Priorité
        List<Priorite> priorites = Arrays.asList(Priorite.values());
        IChoiceRenderer RDR = new ChoiceRenderer("value");
        DropDownChoice<Priorite> prioriteDDC = new DropDownChoice<Priorite>("demande.priorite", priorites, RDR);
        prioriteDDC.setRequired(true);
        prioriteDDC.setLabel(Model.of(" Priorite "));
        prioriteDDC.setOutputMarkupId(true);

        TextField lieu = new TextField("demande.lieu");
        lieu.setRequired(true);
        lieu.setLabel(Model.of("lieu"));
        lieu.setOutputMarkupId(true);

        DateTextField dateLivraisonSouhaiteValue = new DateTextField("demande.dateLivraisonSouhaiteValue", "yyyy-MM-dd");
        dateLivraisonSouhaiteValue.setOutputMarkupId(true);
        dateLivraisonSouhaiteValue.setRequired(true);
        dateLivraisonSouhaiteValue.setLabel(Model.of(" Date "));

        //Controle selction formation ou fournitures
        natureDDC.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget art) {

                if (condition.Formation == getComponent().getDefaultModelObject()) {
                    typeDemandeDDC.setEnabled(false);

                } else {
                    typeDemandeDDC.setEnabled(true);
                    typeDemandeDDC.setRequired(true);
                    typeDemandeDDC.setLabel(Model.of(" Type de Demande "));
                }
                art.add(containerTypeDemande, typeDemandeDDC, forClone);
            }

        });

        form.add(new AjaxButton("submit") {
            public void onSubmit(AjaxRequestTarget art) {
                if (demande.getDateLivraisonSouhaiteValue().before(new Date())) {
                    feedbackPanel.add(new AttributeModifier("class", "alert alert-warning"));
                    error("La date de livraison / début de formation doit être supérieure à la date du jour !");
                } else {
                    try {
                        demande.setIdUtilisateur((Long) session.getAttribute("SESSION_USER_ID"));
                        Demande response = addDemande(demande);
                        if (response != null) {
                            for (DetailDemande detailDemande : detailDemandes) {
                                if (detailDemande != null) {
                                    detailDemande.setIdDemande(response.getId());
                                }
                            }
                            List<DetailDemande> rsp = addDetailDemande(detailDemandes);
                            if (rsp.isEmpty()) {
                                feedbackPanel.add(new AttributeModifier("class", "alert alert-danger"));
                                error("Demande non ajoutée !");
                                art.add(globalContainer, feedbackPanel);
                            } else {
                                feedbackPanel.add(new AttributeModifier("class", "alert alert-success"));
                                success("Demande ajoutée !");
                                art.add(globalContainer, feedbackPanel);
                            }
//                            art.add(globalContainer, feedbackPanel);
                        }
//                        art.add(globalContainer, feedbackPanel);
                    } catch (Exception e) {
                         
                    }
                }
                art.add(globalContainer, feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                target.add(globalContainer, feedbackPanel);
            }

        });

        containerTypeDemande.add(typeDemandeDDC);
        form.add(departementDDC);
        form.add(prioriteDDC);
        form.add(natureDDC);
        form.add(lieu);
        form.add(dateLivraisonSouhaiteValue);
        form.add(containerTypeDemande);
        form.add(forClone);
        globalContainer.add(form);
        globalContainer.add(feedbackPanel);
    }

    private Demande addDemande(Demande demande) throws IOException {
        Demande response = demandeHttpClient.saveDemande(demande);
        if (response != null) {
            demande = new Demande();

        } else {
            demande = new Demande();
        }
        return response;
    }

    private List<DetailDemande> addDetailDemande(List<DetailDemande> detailDemandes) throws IOException {
        List<DetailDemande> response = detailDemandeHttpClient.saveDetailDemandes(detailDemandes);
        if (!response.isEmpty()) {

//            feedbackPanel.add(new AttributeModifier("class", "alert alert-success"));
//            success("Demande ajoutée !");
            return response;

        } else {
//            feedbackPanel.add(new AttributeModifier("class", "alert alert-danger"));
//            error("Demanande non ajoutée");
            detailDemandes = new LinkedList();
            return response;
        }
    }

}
