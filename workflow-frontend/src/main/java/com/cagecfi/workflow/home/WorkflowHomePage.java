/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.home;

import com.cagecfi.workflow.base.BasePage;
import com.cagecfi.workflow.base.partials.MenuPanel;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.views.home.HomePanel;
import com.cagecfi.workflow.views.profil.ProfilPanel;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 *
 * @author USER
 */
@WicketHomePage
@AuthorizeInstantiation("SIGNED_IN")
public class WorkflowHomePage extends BasePage{
    
    private WebMarkupContainer container;

    public WorkflowHomePage() {
        super();
        addComponent();
    }

    private void addComponent() {
        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        container.add(new HomePanel(EnumConstantes.CONTENT_ID));
        addOrReplace(new MenuPanel(panelId(), container));
    }

    
}
