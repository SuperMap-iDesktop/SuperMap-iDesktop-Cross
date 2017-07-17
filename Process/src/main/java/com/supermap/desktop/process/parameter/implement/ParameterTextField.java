package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;

import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
public class ParameterTextField extends AbstractParameter implements ISelectionParameter {
	private String describe;
	private Boolean isRequisite = false;

	@ParameterField(name = PROPERTY_VALE)
	private String value = "";

	protected ISmTextFieldLegit smTextFieldLegit;

	public ParameterTextField() {
		this("");
	}

	public ParameterTextField(String describe) {
		this.describe = describe;
	}

	/**
	 * 拿参数对象构建控件
	 * yuanR
	 *
	 * @param abstractParameterObjects
	 * @param describe
	 */
	public ParameterTextField(AbstractParameterObjects abstractParameterObjects, String describe) {
		if (abstractParameterObjects != null) {
			this.isRequisite = abstractParameterObjects.isRequisite();
		}
		if (isRequisite) {
			// 当参数为必填参数时，设置其描述文字时，添加必填标记-yuanR 2017.7.17
			this.describe = MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			this.describe = describe;
		}
	}

	@Override
	public String getType() {
		return ParameterType.TEXTFIELD;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		return value;
	}

	public String getDescribe() {
		return describe;
	}

	/**
	 * yuanR 2017.7.17
	 * @param describe
	 * @return
	 */
	public ParameterTextField setDescribe(String describe) {
		if (isRequisite) {
			// 当参数为必填参数时，设置其描述为必填样式-yuanR 2017.7.17
			this.describe = MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
//			this.describe = describe + CommonProperties.getString("String_IsRequiredLable");
		} else {
			this.describe = describe;
		}
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
}
