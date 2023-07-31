package com.cagecfi.workflow.views.utilisateur.adduser;

import com.cagecfi.workflow.model.*;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import com.cagecfi.workflow.views.utilisateur.ValidForm.ValidationForm;
import org.apache.wicket.Component;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

public class AddUserPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateur;

//    private Departement department;

    private String confirmPass;

//    CompoundPropertyModel<UtilisateurRole> usrModel;

    public AddUserPanel(String id) {
        super(id);
        utilisateur = new UtilisateurRole();
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer usrContainer = new WebMarkupContainer("usrContainer");
        usrContainer.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackUser");
        feedbackPanel.setOutputMarkupId(true);

        Form form = new Form<Void>("form", new CompoundPropertyModel(this)){

            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    utilisateur.setEmail(utilisateur.getUsername());
                    if (!utilisateur.getPassword().equals(confirmPass)){
                        feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                        error("les mots de pass ne sont pas identiques");
                    } else {
                        UtilisateurRole usr;
//                    utilisateur.setPassword("0123456789");
                        usr = utilisateurHttpClient.createUtilisateur(utilisateur);
                        if (usr != null){
                            IHMModel ihmModel = new IHMModel();
                            ihmModel.setLevel(usr.getLevel());
                            ihmModel.setBackendMessage(usr.getBackendMessage());
                            feedbackPanel.add(new AttributeAppender("class", "alert alert-success"));
                            success(ihmModel.getBackendMessage());

                            utilisateur = new UtilisateurRole();
                        }
                    }


                } catch (Exception e){
                    feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
                    error("erreur lors de l'enregistrement");
                }

            }

            @Override
            protected void onError() {
                super.onError();
                feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
            }
        };

        TextField userNameField = new TextField("utilisateur.nom");
        userNameField.setLabel(Model.of("nom"));
        userNameField.setRequired(true);
        userNameField.setOutputMarkupId(true);
        form.add(userNameField);

        TextField userPrenomField = new TextField("utilisateur.prenoms");
        userPrenomField.setLabel(Model.of("prenoms"));
        userPrenomField.setRequired(true);
        userPrenomField.setOutputMarkupId(true);
        form.add(userPrenomField);

        TextField telField = new TextField("utilisateur.telephone");
        telField.setLabel(Model.of("telephone"));
        telField.setRequired(true);
        telField.setOutputMarkupId(true);
        form.add(telField);

        TextField emailField = new TextField("utilisateur.username");
        emailField.setLabel(Model.of("username"));
        emailField.setRequired(true);
        emailField.setOutputMarkupId(true);
        form.add(emailField);

        PasswordTextField passwordField = new PasswordTextField("utilisateur.password");
        passwordField.setLabel(Model.of("password"));
        passwordField.setRequired(true);
        passwordField.setOutputMarkupId(true);
        form.add(passwordField);

        PasswordTextField confirmPassField = new PasswordTextField("confirmPass");
        confirmPassField.setLabel(Model.of("confirm password"));
        confirmPassField.setRequired(true);
        confirmPassField.setOutputMarkupId(true);
        form.add(confirmPassField);

       /* IChoiceRenderer choiceRenderer = new ChoiceRenderer("libTitre");
        List<Titre> choices = Arrays.asList(Titre.values());
        DropDownChoice<Titre> titreChoice = new DropDownChoice<>("utilisateur.titre", choices, choiceRenderer);
        titreChoice.setRequired(true).setLabel(Model.of("niveau"));
        titreChoice.setOutputMarkupId(true);
        form.add(titreChoice);*/

        /*IChoiceRenderer depChoiceRenderer = new ChoiceRenderer("libelle");
        List<Departement> depChoices = departementHttpClient.getAllDep();
        DropDownChoice<Departement> depChoice = new DropDownChoice<>("department", new Model<>(), depChoices, depChoiceRenderer);
        depChoice.setLabel(Model.of("departement"));
        depChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                
                utilisateur.setIdDepartement(depChoice.getModelObject().getId());
            }
        });
        depChoice.setOutputMarkupId(true);
        form.add(depChoice);*/

        usrContainer.add(form);
        usrContainer.add(feedbackPanel);
        add(usrContainer);

    }


    /*public AddUserPanel(String id) {
        super(id);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedBack");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //addComponents();
        usrModel = new CompoundPropertyModel<>(new UtilisateurRole());
        // queue(addForm());
        System.out.println("ici loooo");
        *//*queue(new Form<Void>("form"){
            @Override
            protected void onSubmit() {
                super.onSubmit();
                System.out.println("this is it...");
            }
        });*//*
        queue(new ValidationForm<>("form", usrModel));
        queue(userNomField());
        queue(userPrenomField());
        queue(telField());
        queue(emailField());
        queue(choiceField());
        queue(choiceDepField());
        queue(submitButton());
    }*/

    /*private TextField userNomField(){
        TextField userNameField = new TextField("nom");
        userNameField.setLabel(Model.of("nom"));
        userNameField.setRequired(true);
        userNameField.setOutputMarkupId(true);
        return userNameField;
    }

    private TextField userPrenomField(){
        TextField userPrenomField = new TextField("prenoms");
        userPrenomField.setLabel(Model.of("prenoms"));
        userPrenomField.setRequired(true);
        userPrenomField.setOutputMarkupId(true);
        return userPrenomField;
    }

    private TextField telField(){
        TextField telField = new TextField("telephone");
        telField.setLabel(Model.of("telephone"));
        telField.setRequired(true);
        telField.setOutputMarkupId(true);
        return telField;
    }

    private TextField emailField(){
        TextField emailField = new TextField("username");
        emailField.setLabel(Model.of("email"));
        emailField.setRequired(true);
        emailField.setOutputMarkupId(true);
        return emailField;
    }

    private DropDownChoice<Titre> choiceField(){
        IChoiceRenderer choiceRenderer = new ChoiceRenderer("libTitre");
        List<Titre> choices = Arrays.asList(Titre.values());
        DropDownChoice<Titre> titreChoice = new DropDownChoice<Titre>("titre", choices, choiceRenderer);
        titreChoice.setRequired(true).setLabel(Model.of("niveau"));
        titreChoice.setOutputMarkupId(true);
        return titreChoice;

    }

    private DropDownChoice<Departement> choiceDepField(){
        IChoiceRenderer choiceRenderer = new ChoiceRenderer("libelle");
        List<Departement> choices = departementHttpClient.getAllDep();
        DropDownChoice<Departement> depChoice = new DropDownChoice<Departement>("department", new Model<Departement>(), choices, choiceRenderer);
        depChoice.setLabel(Model.of("departement"));
        depChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                utilisateurRole.setIdDepartement(depChoice.getModelObject().getId());
            }
        });
        depChoice.setOutputMarkupId(true);
        return depChoice;

    }*/


 /*   private LabeledFormBorder<String> usernameField() {
        return new LabeledFormBorder<String>(getString("username"), new UsernameTextField("username")){

            @Override
            public boolean isEnabled() {
                return isCreatePage();
            }

        };
    }

    private LabeledFormBorder<Object> lastnameField() {
        return new LabeledFormBorder<>(getString("lastname"), new RequiredTextField<>("lastname"));
    }*/

    /*private Form addForm() {

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackWf");
        feedbackPanel.setOutputMarkupId(true);

        usrModel = new CompoundPropertyModel<>(new UtilisateurRole());

        Form form = new Form<Void>("form", new CompoundPropertyModel(usrModel)){
            @Override
            protected void onSubmit(){
                 System.out.println("essaiseddd....");
            }
        };
        
        return form;
    }

    private Component submitButton() {
        return new Button("submit"){
            @Override
            public void onSubmit() {
                System.out.println("voila tout");
                utilisateurHttpClient.createUtilisateur(utilisateurRole);
                *//*service.save(customerModel.getObject());
                webSocketMessageBroadcaster.sendToAll(new CustomerChangedEvent(customerModel.getObject()));
                if(pageReferenceId != null){
                    setResponsePage(new PageReference(pageReferenceId).getPage());
                } else {
                    setResponsePage(CustomerListPage.class);
                }*//*
            }
        };
    }*/


}
