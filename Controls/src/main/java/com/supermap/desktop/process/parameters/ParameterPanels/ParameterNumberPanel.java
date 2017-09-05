package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterNumber;
import com.supermap.desktop.properties.CommonProperties;

import java.text.MessageFormat;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.NUMBER)
public class ParameterNumberPanel extends ParameterTextFieldPanel {

	private ParameterNumber parameterNumber;

	public ParameterNumberPanel(IParameter parameterNumber) {
		super(parameterNumber);
		this.parameterNumber = (ParameterNumber) parameterNumber;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterNumber.getToolTip());
		labelUnit.setText(this.parameterNumber.getUnit());
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterNumber.getDescribe();
		if (parameterNumber.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}

}
