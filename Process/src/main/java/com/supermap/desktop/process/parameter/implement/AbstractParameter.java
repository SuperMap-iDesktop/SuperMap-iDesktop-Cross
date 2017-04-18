package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;
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
	protected IParameters parameters;

	public static final String DO_NOT_CARE = new String("Don't Care");
	public static final String NO = new String("NO");

	private List<PropertyChangeListener> propertyChangeListeners = new ArrayList<>();
	private List<ParameterValueLegalListener> parameterValueLegalListeners = new ArrayList<>();
	private List<FieldConstraintChangedListener> fieldConstraintChangedListeners = new ArrayList<>();


	@Override
	public void addPropertyListener(PropertyChangeListener propertyChangeListener) {
		if (!propertyChangeListeners.contains(propertyChangeListener)) {
			propertyChangeListeners.add(propertyChangeListener);
		}
	}

	@Override
	public void removePropertyListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeListeners.remove(propertyChangeListener);
	}

	public void firePropertyChangeListener(PropertyChangeEvent propertyChangeEvent) {
		for (int i = propertyChangeListeners.size() - 1; i >= 0; i--) {
			propertyChangeListeners.get(i).propertyChange(propertyChangeEvent);
		}
	}

	@Override
	public void addValueLegalListener(ParameterValueLegalListener parameterValueLegalListener) {
		if (!parameterValueLegalListeners.contains(parameterValueLegalListener)) {
			parameterValueLegalListeners.add(parameterValueLegalListener);
		}
	}

	@Override
	public void removeValueLegalListener(ParameterValueLegalListener parameterValueLegalListener) {
		parameterValueLegalListeners.remove(parameterValueLegalListener);
	}

	@Override
	public boolean isValueLegal(String fieldName, Object value) {
		for (ParameterValueLegalListener parameterValueLegalListener : parameterValueLegalListeners) {
			if (!parameterValueLegalListener.isValueLegal(new ParameterValueLegalEvent(this, fieldName, value))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void addFieldConstraintChangedListener(FieldConstraintChangedListener fieldConstraintChangedListener) {
		if (!fieldConstraintChangedListeners.contains(fieldConstraintChangedListener)) {
			fieldConstraintChangedListeners.add(fieldConstraintChangedListener);
		}
	}

	@Override
	public void removeFieldConstraintChangedListener(FieldConstraintChangedListener fieldConstraintChangedListener) {
		fieldConstraintChangedListeners.remove(fieldConstraintChangedListener);
	}

	@Override
	public void fireFieldConstraintChanged(String fieldName) {
		for (FieldConstraintChangedListener fieldConstraintChangedListener : fieldConstraintChangedListeners) {
			fieldConstraintChangedListener.fieldConstraintChanged(new FieldConstraintChangedEvent(fieldName, this));
		}
	}

	@Override
	public ArrayList<String> getFieldNameList(Class<AbstractParameter> clazz) {
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

	public IParameters getParameters() {
		return parameters;
	}

	public void setParameters(IParameters parameters) {
		this.parameters = parameters;
	}

	public Object isValueSelected(String fieldName, Object value) {
		for (ParameterValueLegalListener parameterValueLegalListener : parameterValueLegalListeners) {
			Object valueSelected = parameterValueLegalListener.isValueSelected(new ParameterValueSelectedEvent(this, fieldName, value));
			if (valueSelected != DO_NOT_CARE) {
				return valueSelected;
			}
		}
		return DO_NOT_CARE;
	}

	@Override
	public void dispose() {

	}


	/**
	 * 直接通过反射设置字段值，不会触发属性改变事件
	 * Setting field values directly by reflection, does not trigger an event to change the property
	 *
	 * @param fieldName 字段的ParameterField注解里面的name值；The name value of the ParameterField annotation in the field
	 * @param value     值
	 * @return
	 */
	@Override
	public boolean setFieldVale(String fieldName, Object value) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			ParameterField annotation = field.getAnnotation(ParameterField.class);
			if (annotation != null && annotation.name().equals(fieldName)) {
				field.setAccessible(true);
				try {
					field.set(this, value);
				} catch (Exception e) {
					return false;
				} finally {
					field.setAccessible(false);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getFieldValue(String fieldName) throws Exception {
		Field[] fields = this.getClass().getFields();
		for (Field field : fields) {
			ParameterField annotation = field.getAnnotation(ParameterField.class);
			if (annotation != null && annotation.name().equals(fieldName)) {
				return field.get(this);
			}
		}
		throw new Exception(ProcessProperties.getString("String_FieldDontExist"));
	}
}
