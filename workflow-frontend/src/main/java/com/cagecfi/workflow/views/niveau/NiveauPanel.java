package com.cagecfi.workflow.views.niveau;

import com.cagecfi.workflow.composant.DataTableScript;
import com.cagecfi.workflow.composant.action.panel.items.yesno.YesNoLink;
import com.cagecfi.workflow.composant.modal.YesNoModal;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.model.Niveau;
import com.cagecfi.workflow.service.impl.NiveauHttpClient;
import com.cagecfi.workflow.views.departement.DepartementPanel;
import com.cagecfi.workflow.views.utilisateur.ValidForm.ValidationForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public class NiveauPanel extends Panel {

    @SpringBean
    private NiveauHttpClient niveauHttpClient;

    private Niveau niveau;
    private List<Niveau> listNiv;

    IModel<Niveau> nModel;

    private Form form;
//    private ValidationForm form;

    public NiveauPanel(String id) {
        super(id);
        addComponents();
    }

    public NiveauPanel(String id, Niveau niveau, WebMarkupContainer c) {
        super(id);
        this.niveau = niveau;
    }

    private void addComponents() {

        WebMarkupContainer nivContainer = new WebMarkupContainer("nivContainer");
        nivContainer.setOutputMarkupId(true);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackNiv");
        feedbackPanel.setOutputMarkupId(true);

        nModel = new CompoundPropertyModel<>(new Niveau());

        form = new Form<Void>("nivForm", new CompoundPropertyModel(this)){
//        form = new ValidationForm<Niveau>("nivForm", nModel){

           /* @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    Niveau niv;
                    if (niveau.getId() == null){
                        niv = niveauHttpClient.saveNiveau(niveau);
                        (Form.findForm(this)).clearInput();
                        form.clearInput();
                    } else {
                        niv = niveauHttpClient.updateNiveau(niveau);
                    }
                } catch (Exception e){
                    feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
                }

            }

            @Override
            protected void onError() {
                super.onError();
                feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
            }*/
        };

        TextField libellefield = new TextField("niveau.libelle");
        libellefield.setLabel(Model.of("libelle"));
        libellefield.setRequired(true);
        libellefield.setOutputMarkupId(true);
        form.add(libellefield);

        TextField descriptionfield = new TextField("niveau.description");
        descriptionfield.setLabel(Model.of("description"));
        descriptionfield.setOutputMarkupId(true);
        form.add(descriptionfield);

        Button btn = new Button("submit"){
            @Override
            public void onSubmit() {
                super.onSubmit();
                try {
                    Niveau niv;
                    if (niveau.getId() == null){
                        niv = niveauHttpClient.saveNiveau(niveau);

                        IHMModel ihmModel = new IHMModel();
                        ihmModel.setLevel(niv.getLevel());
                        ihmModel.setBackendMessage(niv.getBackendMessage());
                        feedbackPanel.add(new AttributeAppender("class", "alert alert-success"));
                        success(niv.getBackendMessage());
                        niveau = new Niveau();

//                        (Form.findForm(this)).clearInput();
//                        form.clearInput();
//                        addOrReplace(new NiveauPanel(EnumConstantes.CONTENT_ID));
//                        nModel.setObject(new Niveau());
//                        Form.findForm(this).clearInput();
//                        form.clearInput();

                    } else {
                        niv = niveauHttpClient.updateNiveau(niveau);
                    }
                } catch (Exception e){
                    feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
                }
            }

            @Override
            public void onError() {
                super.onError();
                feedbackPanel.add(new AttributeAppender("class","alert alert-danger"));
            }
        };

        btn.setOutputMarkupId(true);
        form.add(btn);

        ListView<Niveau> listView = new ListView<Niveau>("listView", new LoadableDetachableModel<List<Niveau>>() {
            @Override
            protected List<Niveau> load() {
                try {
                    listNiv = niveauHttpClient.getAllNiveau();
                    return listNiv;
                } catch (Exception e){
                    e.printStackTrace();
                }
                return Collections.EMPTY_LIST;
            }
        }) {
            @Override
            protected void populateItem(ListItem<Niveau> listItem) {
//                listItem.add(new Label("libelle", listItem.getModelObject().get("libelle").to));
                listItem.add(new Label("libelleTab", new PropertyModel<>(listItem.getModelObject(), "libelle") ));
                listItem.add(new Label("descriptionTab", new PropertyModel<>(listItem.getModelObject(), "description") ));

                listItem.add(new AjaxLink<Void>("updateItem") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
//                        setResponsePage();
                        niveau = listItem.getModelObject();
                        nivContainer.addOrReplace(new NiveauPanel(EnumConstantes.CONTENT_ID, niveau, nivContainer));
                        target.add(nivContainer);

                    }
                });

               /* listItem.add(new YesNoModal("yesmodal") {
                    @Override
                    protected void yesClicked(AjaxRequestTarget target) {
                        this.close(target);
                    }
                });*/

                AjaxLink<Void> deleteLink = new AjaxLink<Void>("deleteItem") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        YesNoModal yesNoModal = new YesNoModal("defaultModal") {
                            @Override
                            protected void yesClicked(AjaxRequestTarget target) {

                                this.close(target);

                            }
                        };
                        yesNoModal.setOutputMarkupId(true);


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

        YesNoModal yesNoModal = new YesNoModal("yesnomode") {
            @Override
            protected void yesClicked(AjaxRequestTarget target) {

            }
        };
        yesNoModal.setOutputMarkupId(true);
        add(yesNoModal);

        DataTableScript dts = new DataTableScript("dts", "table");
        dts.add(listView);
        dts.setOutputMarkupId(true);

        nivContainer.add(form);
        nivContainer.add(feedbackPanel);
        nivContainer.add(dts);
        add(nivContainer);
    }
}
