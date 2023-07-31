package com.cagecfi.workflow.composant.modal;

import com.cagecfi.workflow.composant.DataTableScript;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.model.UtilisateurDepartementMod;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurDepartementHttpClient;
import lombok.SneakyThrows;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;


public abstract class DepartementModalPanel extends Panel {

    @SpringBean
    private DepartementHttpClient departementHttpClient;

    @SpringBean
    private UtilisateurDepartementHttpClient utilisateurDepartementHttpClient;

    private UtilisateurDepartementMod udMod;
    private List<Departement> departements;
    private List<Utilisateur> listUsrs;

    public DepartementModalPanel(String id) {
        super(id);
        udMod = new UtilisateurDepartementMod();
        departements = new LinkedList<>();
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer container = new WebMarkupContainer("depSelecteur");
        container.setOutputMarkupId(true);

        LoadableDetachableModel detachableModel = new LoadableDetachableModel() {
            @SneakyThrows
            @Override
            protected Object load() {
                departements = departementHttpClient.getAllDep();

                return departements;
            }
        };
        ListView depListView = new ListView<Departement>("rows", detachableModel) {

            @Override
            protected void populateItem(ListItem<Departement> item) {
                item.add(new Label("libelle", new PropertyModel(item.getModelObject(), "libelle")));
                item.add(new Label("niveau", new PropertyModel(item.getModelObject(),"niveau.libelle")));

                item.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        listUsrs = utilisateurDepartementHttpClient.getAllUsrsByDep(item.getModelObject().getId());
                        udMod.setUtilisateurList(listUsrs);
                        udMod.setDepartement(item.getModelObject());
//                        selected(target, item.getModelObject());
                        selected(target, udMod);
                    }
                });
                item.setOutputMarkupPlaceholderTag(true);
            }
        };
        depListView.setOutputMarkupId(true);

        DataTableScript dts = new DataTableScript("dts", "table");
        dts.add(depListView);
        container.add(dts);
        add(container);
    }

//    public abstract void selected(AjaxRequestTarget target, Departement departement);
    public abstract void selected(AjaxRequestTarget target, UtilisateurDepartementMod udm);
}
