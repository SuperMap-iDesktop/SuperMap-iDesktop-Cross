package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterFilePanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.File;

/**
 * @author XiaJT
 */
public class ParameterFile extends AbstractParameter {

	private JPanel panel;
	private File selectedFile;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.FILE;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterFilePanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		File oldValue = this.selectedFile;
		if (value instanceof File) {
			selectedFile = (File) value;
		} else if (value instanceof String && new File((String) value).exists()) {
			selectedFile = new File((String) value);
		}
		firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, selectedFile));
	}

	@Override
	public Object getSelectedItem() {
		return selectedFile;
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
