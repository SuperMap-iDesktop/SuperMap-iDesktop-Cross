package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.ParameterPanels.ParameterHDFSPathPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/27.
 */
public class ParameterHDFSPath extends AbstractParameter implements ISelectionParameter {
    private JPanel panel;
    private String url;

    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof String) {
            String oldValue = url;
            this.url = (String) item;
            firePropertyChangeListener(new PropertyChangeEvent(ParameterHDFSPath.this, AbstractParameter.PROPERTY_VALE, oldValue, item));
        }
    }

    @Override
    public Object getSelectedItem() {
        return url;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public JPanel getPanel() {
        if (null == panel) {
            panel = new ParameterHDFSPathPanel(this);
        }
	    return panel;
    }
}
