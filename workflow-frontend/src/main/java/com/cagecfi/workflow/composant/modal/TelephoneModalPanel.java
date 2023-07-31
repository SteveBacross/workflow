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

public abstract class TelephoneModalPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    private UtilisateurRole utilisateurRole;
    private Boolean flag = false;

    public TelephoneModalPanel(String id, UtilisateurRole ur) {
        super(id);
        this.utilisateurRole = ur;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer container = new WebMarkupContainer("telcontainer");
        container.setOutputMarkupId(true);

//        user = new CompoundPropertyModel<>(new UtilisateurRole());

        Form form = new Form<Void>("formModal", new CompoundPropertyModel(this));

        TextField nomField = new TextField("utilisateurRole.telephone");
        nomField.setOutputMarkupId(true);
        form.add(nomField);

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                UtilisateurRole ur = new UtilisateurRole();
                ur.setId(utilisateurRole.getId());
                ur.setTelephone(utilisateurRole.getTelephone());
                utilisateurHttpClient.updateUtilisateurProfil(ur);
               
                flag = true;
                validated(target, ur);

            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                System.out.println("error updating telephone");
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {

                super.onComponentTag(tag);
                
                tag.put("data-bs-dismiss", "modal");

            }
        });

       /* Button btn = new Button("submit"){
            @Override
            public void onSubmit() {
                super.onSubmit();
                System.out.println("dree");
            }
        };
        btn.setOutputMarkupId(true);
        form.add(btn);*/

        container.add(form);
        add(container);
    }

    public abstract void validated(AjaxRequestTarget target, UtilisateurRole ur);
}
