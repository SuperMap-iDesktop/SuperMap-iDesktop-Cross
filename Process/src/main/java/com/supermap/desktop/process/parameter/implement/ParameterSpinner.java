package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterSpinner extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private double value;
	private double min;
	private double max;
	private double step;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.SPINNER;
	}


	@Override
	public void setSelectedItem(Object value) {
		try {
			Double doubleValue = Double.valueOf(String.valueOf(value));
			double oldValue = this.value;
			this.value = doubleValue;
			firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	@Override
	public Object getSelectedItem() {
		return value;
	}

	public double getMin() {
		return min;
	}

	public ParameterSpinner setMin(double min) {
		this.min = min;
		return this;
	}

	public double getMax() {
		return max;
	}

	public ParameterSpinner setMax(double max) {
		this.max = max;
		return this;
	}

	public double getStep() {
		return step;
	}

	public ParameterSpinner setStep(double step) {
		this.step = step;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterSpinner setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
