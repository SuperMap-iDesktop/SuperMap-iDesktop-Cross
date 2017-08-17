package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/8/17.
 */
public class ParameterDefaultValueTextField extends AbstractParameter implements ISelectionParameter {
	private String describe;
	private String unit;
	private Boolean isSetUnit = false;
	private String toolTip;
	private String defaultWarningValue;

	@ParameterField(name = PROPERTY_VALE)
	private String value = "";

	public ParameterDefaultValueTextField() {
		this("");
	}

	public ParameterDefaultValueTextField(String describe) {
		this.describe = describe;
	}


	@Override
	public String getType() {
		return ParameterType.DEFAULTVALUETEXTFIELD;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		return StringUtilities.isNullOrEmpty(value) ? defaultWarningValue : value;
	}

	public String getDescribe() {
		return describe;
	}


	public ParameterDefaultValueTextField setDescribe(String describe) {
		this.describe = describe;
		return this;
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

	public Boolean getSetUnit() {
		return isSetUnit;
	}

	public void setSetUnit(Boolean setUnit) {
		isSetUnit = setUnit;
	}

	public String getDefaultWarningValue() {
		return defaultWarningValue;
	}

	public void setDefaultWarningValue(String defaultWarningValue) {
		this.defaultWarningValue = defaultWarningValue;
	}
}
