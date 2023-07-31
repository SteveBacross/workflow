/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.composant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 *
 * @author dell
 */
public class DataTableScript extends WebMarkupContainer {

    public DataTableScript(String id, String tableId) {
        super(id);
        setOutputMarkupId(true);
        add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("tableId", "#" + tableId);

                try (PackageTextTemplate template = new PackageTextTemplate(DataTableScript.class,
                        "dataTableScript.js")) {
                    response.render(new OnDomReadyHeaderItem(template.asString(variables)));
                    super.renderHead(component, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
