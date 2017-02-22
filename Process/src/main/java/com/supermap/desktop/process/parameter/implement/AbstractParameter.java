package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public abstract class AbstractParameter implements IParameter {

	public static final String PROPERTY_VALE = "value";


	private List<PropertyChangeListener> propertyChangeListeners = new ArrayList<>();

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

	@Override
	public void dispose() {

	}
}
