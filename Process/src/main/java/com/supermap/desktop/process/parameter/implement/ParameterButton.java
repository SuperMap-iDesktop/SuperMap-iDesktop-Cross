package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by Administrator on 2017-04-26.
 */
public class ParameterButton extends AbstractParameter implements ISelectionParameter {

	public static final String PARAMETER_BUTTON_VALUE = "PARAMETER_BUTTON_VALUE";
	@ParameterField(name = PARAMETER_BUTTON_VALUE)
	private String describe;
	private Object value;
	private ActionListener actionListener;
	private boolean enabled;

	public ParameterButton(String describe) {
		this.describe = describe;
	}

	@Override
	public String getType() {
		return ParameterType.BUTTON;
	}

	@Override
	public void setSelectedItem(Object value) {

	}

	@Override
	public Object getSelectedItem() {
		return value;
	}

	public ParameterButton setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Object value){
		boolean oldValue = this.enabled;
		this.enabled = (boolean) value;
		firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, enabled));
	}

}
