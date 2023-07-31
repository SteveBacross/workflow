package com.cagecfi.workflow.views.home;

import com.cagecfi.workflow.model.DashBoardStatusData;
import com.cagecfi.workflow.service.impl.DashboardHttpClient;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePanel extends Panel {

    @SpringBean
    private DashboardHttpClient dashboardHttpClient;

    private DashBoardStatusData dbd;

    public HomePanel(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);

        dbd = dashboardHttpClient.getDashboardStatusData();

        Label nvDmdField = new Label("nvDmd", new PropertyModel(dbd, "nbrDmdNv"));
        nvDmdField.setOutputMarkupId(true);
        container.add(nvDmdField);

        Label enDmdField = new Label("enDmd", new PropertyModel(dbd, "nbrDmdEncours"));
        enDmdField.setOutputMarkupId(true);
        container.add(enDmdField);

        Label valDmdField = new Label("valDmd", new PropertyModel(dbd, "nbrDmdValidee"));
        valDmdField.setOutputMarkupId(true);
        container.add(valDmdField);

        Label rejDmdField = new Label("rejDmd", new PropertyModel(dbd, "nbrDmdRefuse"));
        rejDmdField.setOutputMarkupId(true);
        container.add(rejDmdField);

        Label percnvDmdField = new Label("percNvDmd", new PropertyModel(dbd, "percNbrDmdNv"));
        percnvDmdField.setOutputMarkupId(true);
        container.add(percnvDmdField);

        add(container);
    }

}
