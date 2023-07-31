/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.base;

import com.cagecfi.workflow.base.partials.FooterPanel;
import com.cagecfi.workflow.base.partials.HeaderPanel;
import com.cagecfi.workflow.base.partials.MenuPanel;
import com.cagecfi.workflow.constantes.EnumConstantes;
import com.cagecfi.workflow.model.User;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author USER
 */
public class BasePage extends WebPage {
    
    private User user;
     
    public BasePage() {
        add(new HeaderPanel("headerPanel"));
        add(new MenuPanel(panelId(), null));
        add(new FooterPanel("footerPanel"));
        add(new Label(EnumConstantes.CONTENT_ID));
    }
    
    public String panelId() {
        return "menuPanel";
    }
    
}
