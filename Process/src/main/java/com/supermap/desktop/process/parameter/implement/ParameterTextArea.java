package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.ParameterPanels.ParameterTextAreaPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/21.
 */
public class ParameterTextArea extends AbstractParameter implements ISelectionParameter {
    private String selectItem;
    private JPanel panel;
    private String discribe;

    public ParameterTextArea() {
        this("");
    }

    public ParameterTextArea(String discribe) {
        this.discribe = discribe;
    }

    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof String) {
            String oldValue = (String) item;
            this.selectItem = (String) item;
            firePropertyChangeListener(new PropertyChangeEvent(ParameterTextArea.this, AbstractParameter.PROPERTY_VALE, oldValue, selectItem));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectItem;
    }

    @Override
    public String getType() {
        return null;
    }

    public ParameterTextArea setDiscribe(String discribe) {
        this.discribe = discribe;
        return this;
    }

    public String getDiscribe(){
        return discribe;
    }
    @Override
    public JPanel getPanel() {
        if (null == panel) {
            this.panel = new ParameterTextAreaPanel(this);
        }
        return panel;
    }

    @Override
    public void dispose() {

    }
}
