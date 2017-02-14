package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Datasource;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterDatasource extends AbstractParameter implements ISelectionParameter {

	private JPanel panel;
	private Datasource datasource;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.DATASOURCE;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Datasource) {
			Datasource oldValue = this.datasource;
			datasource = (Datasource) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, datasource));
		}

	}

	@Override
	public Object getSelectedItem() {
		return datasource;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public void dispose() {

	}
}
