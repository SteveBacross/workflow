package com.cagecfi.workflow.views.utilisateur.edituser;

import com.cagecfi.workflow.composant.modal.UtilisateurSelecteurModalPanel;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

public class EditUserPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;

    private String confirmPass;
    private String radioValue;

    public EditUserPanel(String id) {
        super(id);
        utilisateurRole = new UtilisateurRole();
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);

        Form form = new Form("form", new CompoundPropertyModel(this));

        TextField telField = new TextField("utilisateurRole.telephone");
        telField.setLabel(Model.of("telephone"));
        telField.setRequired(true);
        telField.setOutputMarkupId(true);
        form.add(telField);

        TextField nomField = new TextField("utilisateurRole.nom");
        nomField.setLabel(Model.of("nom"));
        nomField.setRequired(true);
        nomField.setOutputMarkupId(true);
        form.add(nomField);

        TextField prenomField = new TextField("utilisateurRole.prenoms");
        prenomField.setLabel(Model.of("prenom"));
        prenomField.setRequired(true);
        prenomField.setOutputMarkupId(true);
        form.add(prenomField);

        TextField emailField = new TextField("utilisateurRole.email");
        emailField.setLabel(Model.of("email"));
        emailField.setRequired(true);
        emailField.setOutputMarkupId(true);
        form.add(emailField);

        PasswordTextField passField = new PasswordTextField("utilisateurRole.password");
        passField.setLabel(Model.of("password"));
        passField.setOutputMarkupId(true);
        form.add(passField);

        PasswordTextField confirmPassField = new PasswordTextField("confirmPass");
        confirmPassField.setLabel(Model.of("confirmer password"));
        confirmPassField.setOutputMarkupId(true);
        form.add(confirmPassField);

        RadioChoice actifChoice = new RadioChoice("radioValue", EnumConstantes.ACTIVATION)
//                .setPrefix("<div class=\"col-md-5\"><div class=\"form-check\"><div class=\"custom-control custom-radio classic-radio-info\">")
                .setPrefix("<div class=\"col-md-4\"><div class=\"form-check\">")
                .setSuffix("</div></div>");
        actifChoice.setLabel(Model.of("Validation"));
        actifChoice.setMarkupId("radio-costom-statut-choix");

        actifChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                String response = getComponent().getDefaultModelObjectAsString();
                if (response.equals("Activer")){
                    utilisateurRole.setActif(true);
                } else {
                    utilisateurRole.setActif(false);
                }
            }
        });
        form.add(actifChoice);

//        IChoiceRenderer choiceRenderer = new ChoiceRenderer("libTitre");

        /*List<String> choices = Arrays.asList("Activer","Desactiver");
        DropDownChoice<String> actifChoice = new DropDownChoice<>("utilisateurRole.actif", choices);
        actifChoice.setLabel(Model.of("niveau"));
        actifChoice.setOutputMarkupId(true);
        form.add(actifChoice);*/

        UtilisateurSelecteurModalPanel utilisateurSelecteur = new UtilisateurSelecteurModalPanel("usrSelecteur") {
            @Override
            public void selected(AjaxRequestTarget target, Utilisateur utilisateur) {
                utilisateurRole.setId(utilisateur.getId());
                utilisateurRole.setNom(utilisateur.getNom());
                utilisateurRole.setPrenoms(utilisateur.getPrenoms());
                utilisateurRole.setTelephone(utilisateur.getTelephone());
                utilisateurRole.setEmail(utilisateur.getEmail());
                UtilisateurRole u = utilisateurHttpClient.getUtilisateurRole(utilisateur.getId());
                if (u.getActif() == true){
                    radioValue = EnumConstantes.ACTIVATION.get(0);
                } else {
                    radioValue = EnumConstantes.ACTIVATION.get(1);
                }
                utilisateurRole.setActif(u.getActif());

                target.add(nomField, prenomField, emailField, telField, actifChoice);
            }

            @Override
            public void selectedDep(AjaxRequestTarget target, Departement departement) {

            }
        };
        utilisateurSelecteur.setOutputMarkupId(true);
        add(utilisateurSelecteur);

        Button button = new Button("submit"){
            @Override
            public void onSubmit() {
                super.onSubmit();

                if (utilisateurRole.getPassword() != null ){
                    if (confirmPass == null){
                        feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                        error("Veuillez confirmer le mot de pass");
                    } else if (!utilisateurRole.getPassword().equals(confirmPass)) {
                        feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                        error("Erreur Les mots de pass ne sont pas identiques");
                    } else {
                        UtilisateurRole ur = new UtilisateurRole();
                        ur = utilisateurHttpClient.updateUtilisateur(utilisateurRole);
                        IHMModel ihmModel = new IHMModel();
                        ihmModel.setLevel(ur.getLevel());
                        ihmModel.setBackendMessage(ur.getBackendMessage());
                        feedbackPanel.add(new AttributeAppender("class", "alert alert-success"));
                        success(ihmModel.getBackendMessage());
                    }
                }
            }

            @Override
            public void onError() {
                super.onError();
            }

        };
        button.setOutputMarkupId(true);
        form.add(button);

        container.add(form);
        container.add(feedbackPanel);
        add(container);
    }
}
