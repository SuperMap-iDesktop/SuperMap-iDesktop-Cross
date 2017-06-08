package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterNumber;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.NUMBER)
public class ParameterNumberPanel extends ParameterTextFieldPanel {

	private ParameterNumber parameterNumber;

	public ParameterNumberPanel(IParameter parameterNumber) {
		super(parameterNumber);
		this.parameterNumber = (ParameterNumber) parameterNumber;
		label.setText(parameterNumber.getDescribe());
		label.setToolTipText(this.parameterNumber.getToolTip());
	}


}
