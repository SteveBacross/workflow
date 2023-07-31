package com.cagecfi.workflow.composant.modal;

import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class NomModalPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;

    CompoundPropertyModel<UtilisateurRole> user;

    public NomModalPanel(String id, UtilisateurRole ur) {
        super(id);
        this.utilisateurRole = ur;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer container = new WebMarkupContainer("nomcontainer");
        container.setOutputMarkupId(true);

        user = new CompoundPropertyModel<>(new UtilisateurRole());

        Form form = new Form<Void>("formModal", new CompoundPropertyModel(this));

        TextField nomField = new TextField("utilisateurRole.nom");
        nomField.setOutputMarkupId(true);
        form.add(nomField);

       /* Button btn = new Button("submit"){
            @Override
            public void onSubmit() {
                super.onSubmit();
                System.out.println("dree");
                NomModalPanel.this.validated(AjaxRequestTarget target);
            }


        };
        btn.setOutputMarkupId(true);
        form.add(btn);*/

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                SecureWebSession session = (SecureWebSession) SecureWebSession.get();
                utilisateurRole.setId((Long) session.getAttribute("SESSION_USER_ID"));

                UtilisateurRole ur = new UtilisateurRole();
                ur.setId(utilisateurRole.getId());
                ur.setNom(utilisateurRole.getNom());
                utilisateurHttpClient.updateUtilisateurProfil(ur);
                validated(target, ur);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                System.out.println("error updating nom");
            }
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }
        });

        /*Button btn = new Button("yes"){
            @Override
            public void onSubmit() {
                super.onSubmit();
                SecureWebSession session = (SecureWebSession) SecureWebSession.get();
                utilisateurRole.setId((Long) session.getAttribute("SESSION_USER_ID"));

                utilisateurHttpClient.updateUtilisateurProfil(utilisateurRole);
                
            }

            @Override
            public void onError() {
                super.onError();
            }
//            @Override
//            protected void onComponentTag(ComponentTag tag) {
//                super.onComponentTag(tag);
//                tag.put("data-bs-dismiss", "modal");
//            }
        };
        btn.setOutputMarkupId(true);
        form.add(btn);*/

        /*AjaxLink yesAjaxLink = new AjaxLink<Void>("yes") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                SecureWebSession session = (SecureWebSession) SecureWebSession.get();
                utilisateurRole.setId((Long) session.getAttribute("SESSION_USER_ID"));

                utilisateurHttpClient.updateUtilisateurProfil(utilisateurRole);
                validated(target);

            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-bs-dismiss", "modal");
            }

            @Override
            public MarkupContainer setDefaultModel(IModel<?> model) {
                return super.setDefaultModel(model);
            }
        };
        yesAjaxLink.setOutputMarkupId(true);
        form.add(yesAjaxLink);*/

        /*add(new AjaxLink("no") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                YesNoPanel.this.noClicked(target);
            }
            @Override
            public MarkupContainer setDefaultModel(IModel model) {
                return super.setDefaultModel( model );
            }

        });*/

        container.add(form);
        add(container);
    }

//    public abstract void dismissed(AjaxRequestTarget target);

    public abstract void validated(AjaxRequestTarget target, UtilisateurRole ur);

}
