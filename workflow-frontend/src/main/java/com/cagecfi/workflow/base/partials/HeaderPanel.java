/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.base.partials;

import com.cagecfi.workflow.base.BasePage;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.UtilisateurHttpClient;
import com.cagecfi.workflow.views.login.LoginPage;
import com.cagecfi.workflow.views.profil.ProfilPanel;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author USER
 */
public class HeaderPanel extends Panel {

    @SpringBean
    private UtilisateurHttpClient utilisateurHttpClient;
    @SpringBean
    private DepartementHttpClient departementHttpClient;

    private SecureWebSession session = (SecureWebSession) getSession();

    private String nomDemandeur;
    private String libDepartement;

    public HeaderPanel(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {

        if (session != null) {
            session.getAttribute("SESSION_USER_ID");
            session.getAttribute("SESSION_USER_DEPARTMENT_ID");
        }

        Utilisateur demandeur = utilisateurHttpClient.getUtilisateurById((Long) session.getAttribute("SESSION_USER_ID"));
        nomDemandeur = demandeur.getNom() + " " + demandeur.getPrenoms();
        add(new Label("nomDemandeur", nomDemandeur));
        
        Departement departement = departementHttpClient.getDepartmentById((Long)session.getAttribute("SESSION_USER_DEPARTMENT_ID"));
        libDepartement = departement.getLibelle();
        add(new Label("departement", libDepartement));

        AjaxLink profil = new AjaxLink("profil") {
            @Override
            public void onClick(AjaxRequestTarget target) {
//                remove(EnumConstantes.CONTENT_ID);
                addOrReplace(new ProfilPanel(EnumConstantes.CONTENT_ID));
                target.add(this);
            }
        };
        add(profil);

        AjaxLink logout = new AjaxLink("logout") {
            @Override
            public void onClick(AjaxRequestTarget target) {
//                remove(EnumConstantes.CONTENT_ID);
                setResponsePage(LoginPage.class);
                target.add(this);
            }
        };
        add(logout);

    }

}
