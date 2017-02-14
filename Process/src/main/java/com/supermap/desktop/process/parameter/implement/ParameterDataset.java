package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterDatasetPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterDataset extends AbstractParameter implements ISelectionParameter {

	private JPanel panel;
	private Dataset dataset;

	@Override
	public String getType() {
		return ParameterType.DATASET;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterDatasetPanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Dataset) {
			Dataset oldValue = this.dataset;
			dataset = (Dataset) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, dataset));
		}
	}

	@Override
	public Object getSelectedItem() {
		return dataset;
	}

	@Override
	public void dispose() {

	}
}
