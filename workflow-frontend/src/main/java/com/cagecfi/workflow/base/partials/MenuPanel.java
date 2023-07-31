/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.base.partials;

import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.views.demande.DemandePanel;
import com.cagecfi.workflow.views.departement.AffectationDepartement;
import com.cagecfi.workflow.views.niveau.NiveauPanel;
import com.cagecfi.workflow.views.validation.ValidationDirectionPanel;
import com.cagecfi.workflow.views.departement.DepartementPanel;
import com.cagecfi.workflow.views.profil.ProfilPanel;
import com.cagecfi.workflow.views.rapport.RapportDemande;
import com.cagecfi.workflow.views.utilisateur.adduser.AddUserPanel;
import com.cagecfi.workflow.views.utilisateur.edituser.EditUserPanel;
import com.cagecfi.workflow.views.validation.ValidationDmdFinalPanel;
import com.cagecfi.workflow.views.validation.ValidationDmdPanel;
import com.cagecfi.workflow.views.validation.ValidationFormationPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author USER
 */
public class MenuPanel extends Panel {

    public MenuPanel(String id, WebMarkupContainer container) {
        super(id);
        addComponents(container);
    }

    private void addComponents(WebMarkupContainer container) {

        add(new AjaxLink<Void>("ccc") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new ProfilPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("dmd") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new DemandePanel(EnumConstantes.CONTENT_ID, container));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("validDmd") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new ValidationDmdPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });
        
                add(new AjaxLink<Void>("validFormation") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new ValidationFormationPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });


        add(new AjaxLink<Void>("validDmdFinale") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new ValidationDmdFinalPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("addUser") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new AddUserPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("editUser") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new EditUserPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("setDepartement") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new DepartementPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("setNiveau") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new NiveauPanel(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("setAffectationDep") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                container.addOrReplace(new AffectationDepartement(EnumConstantes.CONTENT_ID));
                target.add(container);
            }
        });
        
        add(new AjaxLink<Void>("validationDirectionPanel"){
            @Override
            public void onClick(AjaxRequestTarget target){
                container.addOrReplace(new ValidationDirectionPanel(EnumConstantes.CONTENT_ID,container));
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("rapportDmd"){
            @Override
            public void onClick(AjaxRequestTarget target){
                container.addOrReplace(new RapportDemande(EnumConstantes.CONTENT_ID,container));
                target.add(container);
            }
        });

    }

}
