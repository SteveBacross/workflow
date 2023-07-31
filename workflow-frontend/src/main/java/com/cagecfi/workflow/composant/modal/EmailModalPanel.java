package com.cagecfi.workflow.composant.modal;

import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class EmailModalPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;

    CompoundPropertyModel<UtilisateurRole> user;

    public EmailModalPanel(String id, UtilisateurRole ur) {
        super(id);
        this.utilisateurRole = ur;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer container = new WebMarkupContainer("emailcontainer");
        container.setOutputMarkupId(true);

        user = new CompoundPropertyModel<>(new UtilisateurRole());

        Form form = new Form<Void>("formModal", new CompoundPropertyModel(this));

        TextField emailField = new TextField("utilisateurRole.email");
        emailField.setOutputMarkupId(true);
        form.add(emailField);

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                UtilisateurRole ur = new UtilisateurRole();
                ur.setId(utilisateurRole.getId());
                ur.setEmail(utilisateurRole.getEmail());
                utilisateurHttpClient.updateUtilisateurProfil(ur);
                validated(target, ur);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
            }
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }
        });

        container.add(form);
        add(container);
    }

    public abstract void validated(AjaxRequestTarget target, UtilisateurRole ur);
}
