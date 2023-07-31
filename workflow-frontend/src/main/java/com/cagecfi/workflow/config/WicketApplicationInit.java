/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.config;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 * @author USER
 */
@ApplicationInitExtension
public class WicketApplicationInit implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        webApplication.getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        webApplication.getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
    }
}
