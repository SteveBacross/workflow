package com.cagecfi.workflow.composant.modal;

import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import lombok.SneakyThrows;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

public abstract class UtilisateurSelecteurModalPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;

    @SpringBean
    private DepartementHttpClient departementHttpClient;

    private Utilisateur utilisateur;
    private List<Utilisateur> listUtilisateurs;

    public UtilisateurSelecteurModalPanel(String id) {
        super(id);
        utilisateur = new Utilisateur();
        listUtilisateurs = new LinkedList<>();
        addComponents();
    }

//    public UtilisateurSelecteurModalPanel(String id, ) {
//        super(id);
//        utilisateur = new Utilisateur();
//        listUtilisateurs = new LinkedList<>();
//        addComponents();
//    }

    private void addComponents() {

        WebMarkupContainer container = new WebMarkupContainer("usrcontainer");
        container.setOutputMarkupId(true);

        WebMarkupContainer tabContainer = new WebMarkupContainer("tabContainer");
        tabContainer.setOutputMarkupId(true);

        Form form = new Form("formModal", new CompoundPropertyModel(this));

        TextField nomField = new TextField("utilisateur.nom");
        nomField.setOutputMarkupId(true);
        form.add(nomField);

        TextField telField = new TextField("utilisateur.telephone");
        telField.setOutputMarkupId(true);
        form.add(telField);

        TextField emailField = new TextField("utilisateur.email");
        emailField.setOutputMarkupId(true);
        form.add(emailField);

        form.add(new AjaxButton("searching") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                if (utilisateur.getNom() == null){
                    utilisateur.setNom(" ");
                }
                if (utilisateur.getTelephone() == null){
                    utilisateur.setTelephone(" ");
                }
                if (utilisateur.getEmail() == null){
                    utilisateur.setEmail(" ");
                }
                List<Utilisateur> usrs = utilisateurHttpClient.getUserByParams(utilisateur.getNom(),utilisateur.getTelephone(), utilisateur.getEmail());
                listUtilisateurs = usrs;
                target.add(tabContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
            }
        });

        /*Button btn = new Button("searching"){
            @Override
            public void onSubmit() {
                super.onSubmit();
                List<Utilisateur> usrs = utilisateurHttpClient.getUserByParams(utilisateur.getNom(),utilisateur.getTelephone(), utilisateur.getEmail());
                listUtilisateurs = usrs;
            }

            @Override
            public void onError() {
                super.onError();
                System.out.println("errueur");
            }
        };
        btn.setOutputMarkupId(true);
        form.add(btn);*/

        LoadableDetachableModel detachableModel = new LoadableDetachableModel() {
            @SneakyThrows
            @Override
            protected Object load() {
                return listUtilisateurs;
            }
        };
        ListView usrListView = new ListView<Utilisateur>("rows", detachableModel) {

            @Override
            protected void populateItem(ListItem<Utilisateur> item) {
                item.add(new Label("nom", new PropertyModel(item.getModelObject(), "nom")));
                item.add(new Label("prenom", new PropertyModel(item.getModelObject(),"prenoms")));
                item.add(new Label("tel", new PropertyModel(item.getModelObject(),"telephone")));
                item.add(new Label("email", new PropertyModel(item.getModelObject(), "email")));

                item.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        selected(target, item.getModelObject());
                        Departement dep = new Departement();
                        dep = departementHttpClient.getOneDepartmentByUser(item.getModelObject().getId());
                        if (dep != null){
                            selectedDep(target, dep);
                        }
                    }
                });
                item.setOutputMarkupPlaceholderTag(true);
            }
        };
        usrListView.setOutputMarkupId(true);


        /*btn.setOutputMarkupId(true);
        form.add(btn);*/

        container.add(form);
        tabContainer.add(usrListView);
        add(container);
        add(tabContainer);

    }

    public abstract void selected(AjaxRequestTarget target, Utilisateur utilisateur);
    public abstract void selectedDep(AjaxRequestTarget target, Departement departement);
}
