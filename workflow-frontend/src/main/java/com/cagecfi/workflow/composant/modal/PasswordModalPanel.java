package com.cagecfi.workflow.composant.modal;

import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class PasswordModalPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;
    private String passwordOld;
    private String passwordNew;
    private String passwordConfirm;

    CompoundPropertyModel<UtilisateurRole> user;

    public PasswordModalPanel(String id, UtilisateurRole ur) {
        super(id);
        this.utilisateurRole = ur;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer container = new WebMarkupContainer("passwordcontainer");
        container.setOutputMarkupId(true);

        WebMarkupContainer passactionblock = new WebMarkupContainer("passactionblock");

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackModal");
        feedbackPanel.setOutputMarkupId(true);

        user = new CompoundPropertyModel<>(new UtilisateurRole());

        Form form = new Form<Void>("formModal", new CompoundPropertyModel(this));

        PasswordTextField oldPassField = new PasswordTextField("passwordOld");
        oldPassField.setOutputMarkupId(true);
        form.add(oldPassField);

        PasswordTextField newPassField = new PasswordTextField("passwordNew");
        newPassField.setOutputMarkupId(true);
        newPassField.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                System.out.println(newPassField.getModelObject());
                passwordNew = newPassField.getModelObject().toString();
            }
        });
        form.add(newPassField);

        Label label = new Label("right");
        label.setVisible(false);
        form.add(label.setOutputMarkupId(true));

        PasswordTextField confirmPassField = new PasswordTextField("passwordConfirm");
        confirmPassField.setOutputMarkupId(true);
        confirmPassField.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (passwordNew != null && confirmPassField.getModelObject() != null) {
                    if (passwordNew.equals(confirmPassField.getModelObject().toString())) {
                        label.setVisible(true);
                        target.add(container);
                    }
                }
            }
        });

        form.add(confirmPassField);

        form.add(new EqualPasswordInputValidator(newPassField, confirmPassField));

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                UtilisateurRole ur = new UtilisateurRole();

                if (!passwordNew.equals(passwordConfirm)) {
                    feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                    error("les mots de pass ne sont pas identiques");
                } else {
                    ur.setId(utilisateurRole.getId());
                    ur.setPasswordOld(passwordOld);
                    ur.setPassword(passwordNew);
                    utilisateurHttpClient.updateUtilisateurProfil(ur);

                    passactionblock.add(new AttributeAppender("class", "modal fade"));
                    passactionblock.add(new AttributeAppender("style", "display:none;"));
                    target.add(container);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                error("erryry styr");
                System.out.println("error updating password ...");
                target.add(feedbackPanel);
            }

            /*@Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }*/
        });
        
        passactionblock.add(form);
        passactionblock.add(feedbackPanel);
        container.add(passactionblock);
        add(container);
    }

    public abstract void validate(AjaxRequestTarget target);
}
