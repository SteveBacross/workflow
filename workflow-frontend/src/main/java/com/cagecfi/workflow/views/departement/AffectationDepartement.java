package com.cagecfi.workflow.views.departement;

import com.cagecfi.workflow.composant.modal.DepartementModalPanel;
import com.cagecfi.workflow.composant.modal.UtilisateurSelecteurModalPanel;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.model.UtilisateurDepartement;
import com.cagecfi.workflow.model.UtilisateurDepartementMod;
import com.cagecfi.workflow.service.impl.UtilisateurDepartementHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

public class AffectationDepartement extends Panel {

    @SpringBean
    private UtilisateurDepartementHttpClient udHttpClient;

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurDepartementMod ud = new UtilisateurDepartementMod();
    private UtilisateurDepartement utilisateurDepartement;
    private Departement departement;
    private Utilisateur utilisateur;
    private List<String> palettePers;
    private List<Utilisateur> usrsInDepList;


    public AffectationDepartement(String id) {
        super(id);
//        ud = new UtilisateurDepartementMod();
        ud.setUtilisateurList(udHttpClient.getAllUsrsByDep(2L));
        utilisateurDepartement = new UtilisateurDepartement();
        departement = new Departement();
        utilisateur = new Utilisateur();
        usrsInDepList = new LinkedList<>();
        palettePers = new LinkedList<>();
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer container = new WebMarkupContainer("affectationContainer");
        container.setOutputMarkupId(true);

        WebMarkupContainer affContainer = new WebMarkupContainer("affcontainer");
        affContainer.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);

        Form form = new Form<Void>("affectationForm", new CompoundPropertyModel(this));

        Label deptField = new Label("departement.libelle");
        deptField.setOutputMarkupId(true);
        form.add(deptField);

        Label userField = new Label("utilisateur.nom");
        userField.setOutputMarkupId(true);
        form.add(userField);

        /*UtilisateurSelecteurModalPanel utilisateurSelecteur = new UtilisateurSelecteurModalPanel("usrSelecteur") {
            @Override
            public void selected(AjaxRequestTarget target, Utilisateur usr) {
                utilisateurDepartement.setIdUtilisateur(usr.getId());
                utilisateur.setNom(usr.getNom());
                target.add(userField);
            }

            @Override
            public void selectedDep(AjaxRequestTarget target, Departement depart) {
                utilisateurDepartement.setIdDepartement(depart.getId());
                departement = depart;
                target.add(deptField);
            }
        };
        utilisateurSelecteur.setOutputMarkupId(true);
        add(utilisateurSelecteur);*/


        //        List<Utilisateur> listUsrs = utilisateurHttpClient.getAllUserByDeps();
        List<Utilisateur> listUsrs = udHttpClient.getAllUserWithoutDeps();
        IChoiceRenderer persRenderer = new ChoiceRenderer("nom");

        Palette palettePersonne = new Palette("ud.utilisateurList",
//                new ListModel<Utilisateur>(ud.getUtilisateurList()),
                new ListModel<Utilisateur>(ud.getUtilisateurList()),
                new CollectionModel<Utilisateur>(listUsrs),
                persRenderer, 15, false);

        palettePersonne.setVisible(false).setOutputMarkupId(true);
//        palettePersonne.setVisible(false);
        affContainer.add(palettePersonne);
        form.add(affContainer);

        DepartementModalPanel departementModalPanel = new DepartementModalPanel("departementSelecteur") {
            @Override
            public void selected(AjaxRequestTarget target, UtilisateurDepartementMod udm) {
//                utilisateurDepartement.setIdDepartement(department.getId());

               /* departement.setLibelle(department.getLibelle());
                target.add(deptField);*/

//                ud = udm;
                palettePersonne.setVisible(true);
                target.add(affContainer, palettePersonne);
            }
        };
        departementModalPanel.setOutputMarkupId(true);
        add(departementModalPanel);



        Button btn = new Button("saveBtn") {
            @Override
            public void onSubmit() {
                super.onSubmit();
//                udHttpClient.createUD(utilisateurDepartement);
            }

            @Override
            public void onError() {
                super.onError();
                System.out.println("Failed creating bean...");
            }
        };
        btn.setOutputMarkupId(true);
        form.add(btn);

        container.add(form);
        container.add(feedbackPanel);
        add(container);

    }

}
