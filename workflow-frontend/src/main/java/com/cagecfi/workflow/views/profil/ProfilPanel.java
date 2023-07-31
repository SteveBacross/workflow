/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.views.profil;

import com.cagecfi.workflow.composant.modal.*;
import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author USER
 */
public class ProfilPanel extends Panel{

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;
//    CompoundPropertyModel<UtilisateurRole> user;

    public ProfilPanel(String id) {
        super(id);
        SecureWebSession session = (SecureWebSession) SecureWebSession.get();
        this.utilisateurRole = utilisateurHttpClient.getUtilisateurRole((Long) session.getAttribute("SESSION_USER_ID")) ;
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer profilcontainer = new WebMarkupContainer("profilcontainer");
        profilcontainer.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);

        Form form = new Form("formprofil", new CompoundPropertyModel(this));

        Label nomLabel = new Label("utilisateurRole.nom");
        nomLabel.setOutputMarkupId(true);
        form.add(nomLabel);

        Label prenomLabel = new Label("utilisateurRole.prenoms");
        prenomLabel.setOutputMarkupId(true);
        form.add(prenomLabel);

        Label telephoneLabel = new Label("utilisateurRole.telephone");
        telephoneLabel.setOutputMarkupId(true);
        form.add(telephoneLabel);

        Label usernameLabel = new Label("utilisateurRole.username");
        usernameLabel.setOutputMarkupId(true);
        form.add(usernameLabel);

        Label emailLabel = new Label("utilisateurRole.email");
        emailLabel.setOutputMarkupId(true);
        form.add(emailLabel);

        /*AjaxLink<Void> nomLink = new AjaxLink<Void>("nomLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {

            }
        };
        form.add(nomLink);*/

        NomModalPanel nomModal = new NomModalPanel("nomModal", utilisateurRole) {
            @Override
            public void validated(AjaxRequestTarget target, UtilisateurRole ur) {
                utilisateurRole.setNom(ur.getNom());
                target.add(nomLabel);
//                target.add(form);
            }
        };
        nomModal.setOutputMarkupId(true);
        add(nomModal);

        PrenomModalPanel prenomModal = new PrenomModalPanel("prenomModal", utilisateurRole) {
            @Override
            public void validated(AjaxRequestTarget target, UtilisateurRole ur) {
                utilisateurRole.setPrenoms(ur.getPrenoms());
                target.add(prenomLabel);
            }
        };
        prenomModal.setOutputMarkupId(true);
        add(prenomModal);

        EmailModalPanel emailModal = new EmailModalPanel("emailModal", utilisateurRole) {
            @Override
            public void validated(AjaxRequestTarget target, UtilisateurRole ur) {
                utilisateurRole.setEmail(ur.getEmail());
                target.add(emailLabel);
            }
        };
        emailModal.setOutputMarkupId(true);
        add(emailModal);

        UsernameModalPanel usrnameModal = new UsernameModalPanel("usrnameModal", utilisateurRole) {
            @Override
            public void validated(AjaxRequestTarget target, UtilisateurRole ur) {
                utilisateurRole.setUsername(ur.getUsername());
                target.add(usernameLabel);
            }
        };
        usrnameModal.setOutputMarkupId(true);
        add(usrnameModal);

        TelephoneModalPanel telModal = new TelephoneModalPanel("telModal", utilisateurRole) {
            @Override
            public void validated(AjaxRequestTarget target, UtilisateurRole ur) {
                utilisateurRole.setTelephone(ur.getTelephone());
                target.add(telephoneLabel);
            }
        };
        telModal.setOutputMarkupId(true);
        add(telModal);

        PasswordModalPanel passwModal = new PasswordModalPanel("passwModal", utilisateurRole) {
            @Override
            public void validate(AjaxRequestTarget target) {

            }
        };
        passwModal.setOutputMarkupId(true);
        add(passwModal);

        profilcontainer.add(form);
        add(profilcontainer);
    }

    
}
