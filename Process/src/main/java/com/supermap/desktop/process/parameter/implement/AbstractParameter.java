package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public abstract class AbstractParameter implements IParameter {

	protected IParameterPanel panel;
	public static final String PROPERTY_VALE = "value";
	private IParameters parameters;

	private List<PropertyChangeListener> propertyChangeListeners = new ArrayList<>();
	private List<PropertyChangeListener> filedChangeListeners = new ArrayList<>();


	public void addPropertyListener(PropertyChangeListener propertyChangeListener) {
		if (!propertyChangeListeners.contains(propertyChangeListener)) {
			propertyChangeListeners.add(propertyChangeListener);
		}
	}

	public void removePropertyListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeListeners.remove(propertyChangeListener);
	}

	public void firePropertyChangeListener(PropertyChangeEvent propertyChangeEvent) {
		for (int i = propertyChangeListeners.size() - 1; i >= 0; i--) {
			propertyChangeListeners.get(i).propertyChange(propertyChangeEvent);
		}
	}

	public void addFieldListener(PropertyChangeListener propertyChangeListener) {
		if (!filedChangeListeners.contains(propertyChangeListener)) {
			filedChangeListeners.add(propertyChangeListener);
		}
	}

	public void removeFieldListener(PropertyChangeListener propertyChangeListener) {
		filedChangeListeners.remove(propertyChangeListener);
	}

	public void fireFieldValueChanged(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener filedChangeListener : filedChangeListeners) {
			filedChangeListener.propertyChange(propertyChangeEvent);
		}
	}

	public static ArrayList<String> getFieldNameList(Class<AbstractParameter> clazz) {
		ArrayList<String> nameList = new ArrayList<>();
		Field[] fields = clazz.getClass().getFields();
		for (Field field : fields) {
			ParameterField annotation = field.getAnnotation(ParameterField.class);
			if (annotation != null) {
				String name = annotation.name();
				nameList.add(name);
			}
		}
		return nameList;
	}

	@Override
	public IParameterPanel getParameterPanel() {
		if (panel == null) {
			panel = parameters.createPanel(this);
		}
		return panel;
	}
	@Override
	public void dispose() {

	}
}
