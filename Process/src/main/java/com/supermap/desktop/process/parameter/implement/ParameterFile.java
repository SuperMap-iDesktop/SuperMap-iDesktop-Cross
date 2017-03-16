package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;
import java.io.File;

/**
 * @author XiaJT
 */
public class ParameterFile extends AbstractParameter implements ISelectionParameter {

	private File selectedFile;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.FILE;
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

	public ParameterFile setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
