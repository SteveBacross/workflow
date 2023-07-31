package com.cagecfi.workflow.views.departement;

import com.cagecfi.workflow.composant.DataTableScript;
import com.cagecfi.workflow.composant.modal.YesNoModal;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.model.Niveau;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.NiveauHttpClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DepartementPanel extends Panel {

    @SpringBean
    private DepartementHttpClient departementHttpClient;

    @SpringBean
    private NiveauHttpClient niveauHttpClient;

    private Departement departement;
    private List<Departement> listDep;

    public DepartementPanel(String id) {
        super(id);
        addComponents();
    }

    public DepartementPanel(String id, Departement departement, WebMarkupContainer c) {
        super(id);
        this.departement = departement;
        addComponents();
    }

    private void addComponents() {
        WebMarkupContainer depContainer = new WebMarkupContainer("depContainer");
        depContainer.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackDep");
        feedbackPanel.setOutputMarkupId(true);

        Form form = new Form<Void>("depForm", new CompoundPropertyModel(this)){

            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    Departement dep;
                    if (departement.getId() == null){
                        dep = departementHttpClient.saveDep(departement);
                    } else {
                        dep = departementHttpClient.updateDep(departement);
                    }
                } catch (Exception e){
                    feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
                }

            }

            @Override
            protected void onError() {
                super.onError();
                feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
            }
        };

        TextField libellefield = new TextField("departement.libelle");
        libellefield.setLabel(Model.of("libelle"));
        libellefield.setRequired(true);
        libellefield.setOutputMarkupId(true);
        form.add(libellefield);

        /*IChoiceRenderer choiceRenderer = new ChoiceRenderer<Niveau>("description");
        List<Niveau> choices = Arrays.asList(Niveau.values());
        DropDownChoice<Niveau> niveauChoice = new DropDownChoice<Niveau>("departement.niveau", choices, choiceRenderer);
        niveauChoice.setRequired(true).setLabel(Model.of("niveau"));
        niveauChoice.setOutputMarkupId(true);
        form.add(niveauChoice);*/

        IChoiceRenderer choiceRenderer = new ChoiceRenderer<Niveau>("libelle");
        List<Niveau> choices = niveauHttpClient.getAllNiveau();
        DropDownChoice<Niveau> niveauChoice = new DropDownChoice<Niveau>("departement.niveau", choices, choiceRenderer);
        niveauChoice.setRequired(true).setLabel(Model.of("niveau"));
        niveauChoice.setOutputMarkupId(true);
        form.add(niveauChoice);

        IChoiceRenderer depChoiceRenderer = new ChoiceRenderer<Niveau>("libelle");
        List<Departement> depChoices = departementHttpClient.getAllDep();
        DropDownChoice<Departement> depChoice = new DropDownChoice<Departement>("department", new Model<Departement>(), depChoices, depChoiceRenderer);
        depChoice.setLabel(Model.of("departement"));
        depChoice.setOutputMarkupId(true);
        form.add(depChoice);

        ListView<Departement> listView = new ListView<Departement>("listView", new LoadableDetachableModel<List<Departement>>() {
            @Override
            protected List<Departement> load() {
                try {
                    listDep = departementHttpClient.getAllDep();
                    return listDep;
                } catch (Exception e){
                    e.printStackTrace();
                }
                return Collections.EMPTY_LIST;
            }
        }) {
            @Override
            protected void populateItem(ListItem<Departement> listItem) {
//                listItem.add(new Label("libelle", listItem.getModelObject().get("libelle").to));
                listItem.add(new Label("libelleTab", new PropertyModel<>(listItem.getModelObject(), "libelle") ));
                listItem.add(new Label("niveauTab", new PropertyModel<>(listItem.getModelObject(), "niveau.libelle") ));

//                listItem.add(new Label("libelleTabs", new PropertyModel<>(listItem.getModelObject(), "libelle") ));
                Long idDep = listItem.getModelObject().getIdDepartementSup();
                Departement d =departementHttpClient.getDepartmentById(idDep);
                listItem.add(new Label("libelleTabs", new PropertyModel<>(d, "libelle")));

                listItem.add(new AjaxLink<Void>("updateItem") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
//                        setResponsePage();
                        departement = listItem.getModelObject();
                        depContainer.addOrReplace(new DepartementPanel(EnumConstantes.CONTENT_ID, departement, depContainer));
                        target.add(depContainer);
                    }
                });

                AjaxLink<Void> deleteLink = new AjaxLink<Void>("deleteItem") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        YesNoModal yesNoModal = new YesNoModal("defaultModal") {
                            @Override
                            protected void yesClicked(AjaxRequestTarget target) {

                                this.close(target);
                            }
                        };

                        /*IHMModel result = departementHttpClient.deleteDep(listItem.getModelObject().getId());
                        if (result != null){
                            if (result.getLevel()){
                                feedbackPanel.add(new AttributeAppender("class", "alert alert-success"));
                                success(result.getBackendMessage());
                                listDep.remove(listItem.getModelObject());
                                target.add(feedbackPanel);
                            }else {
                                feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                                error(result.getBackendMessage());
                                target.add(feedbackPanel);
                            }
                        }else {
                            feedbackPanel.add(new AttributeAppender("class", "alert alert-danger"));
                            target.add(feedbackPanel);
                        }
                        target.add(depContainer);*/
                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                        super.updateAjaxAttributes(attributes);
                    }
                };
                listItem.add(deleteLink);
            }
        };

        DataTableScript dts = new DataTableScript("dts", "table");
        dts.add(listView);
        dts.setOutputMarkupId(true);

        depContainer.add(form);
        depContainer.add(feedbackPanel);
        depContainer.add(dts);
        add(depContainer);
    }

}
