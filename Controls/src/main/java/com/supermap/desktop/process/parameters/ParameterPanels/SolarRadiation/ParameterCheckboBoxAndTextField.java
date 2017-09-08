package com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
public class ParameterCheckboBoxAndTextField extends AbstractParameter implements ISelectionParameter{
	private String describe;
	private String value;
	private boolean isSelected=true;

	public ParameterCheckboBoxAndTextField() {
		this("");
	}

	public ParameterCheckboBoxAndTextField(String describe){
		this.describe=describe;
	}

	@Override
	public void setSelectedItem(Object value) {
		this.value = String.valueOf(value);
	}

	@Override
	public String getSelectedItem() {
		return this.value;
	}

	@Override
	public String getType() {
		return ParameterType.CHECK_AND_TEXTFIELD;
	}

	public String getDescribe() {
		return this.describe;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

}
