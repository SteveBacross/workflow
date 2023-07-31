package com.cagecfi.workflow.composant.action.panel;

import com.cagecfi.workflow.composant.action.panel.items.AbstrractActionItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

public class ActionPanel extends Panel {

    public ActionPanel(String id, List<AbstrractActionItem> items) {
        super(id);
        ListView<AbstrractActionItem> listItems = new ListView<AbstrractActionItem>("items", items) {
            @Override
            protected void populateItem(ListItem<AbstrractActionItem> item) {
                item.add(item.getModel().getObject());
            }
        };
        add(listItems);
    }
}
