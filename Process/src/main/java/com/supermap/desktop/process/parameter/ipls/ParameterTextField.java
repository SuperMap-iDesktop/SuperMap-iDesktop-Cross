package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterTextField extends AbstractParameter implements ISelectionParameter {
	private String describe;
	private String unit;
	private Boolean isSetUnit = false;
	private String toolTip;

	@ParameterField(name = PROPERTY_VALE)
	private String value = "";

	protected ISmTextFieldLegit smTextFieldLegit;

	public ParameterTextField() {
		this("");
	}

	public ParameterTextField(String describe) {
		this.describe = describe;
	}


	@Override
	public String getType() {
		return ParameterType.TEXTFIELD;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		if (value instanceof Double) {
			value = DoubleUtilities.getFormatString(((Double) value));
		} else {
			this.value = String.valueOf(value);
		}
		firePropertyChangeListener(new PropertyChangeEvent(this, PROPERTY_VALE, oldValue, value));
	}

	@Override
	public String getSelectedItem() {
		return value;
	}

	public String getDescribe() {
		return describe;
	}


	public ParameterTextField setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	public ISmTextFieldLegit getSmTextFieldLegit() {
		return smTextFieldLegit;
	}

	public void setSmTextFieldLegit(ISmTextFieldLegit smTextFieldLegit) {
		this.smTextFieldLegit = smTextFieldLegit;
	}

	@Override
	public void dispose() {

	}

	/**
	 * 设置文本框中参数单位
	 *
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.isSetUnit = true;
		String oldValue = this.unit;
		this.unit = unit;
		firePropertyChangeListener(new PropertyChangeEvent(this, "unit", oldValue, this.unit));
	}

	/**
	 * 文本框中参数是否有单位
	 *
	 * @return
	 */
	public Boolean isSetUnit() {
		return this.isSetUnit;
	}

	/**
	 * '文本框中参数单位
	 *
	 * @return
	 */
	public String getUnit() {
		return this.unit;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
}
