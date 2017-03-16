package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterSpinner;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;

import javax.swing.*;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SPINNER)
public class ParameterSpinnerPanel extends DefaultParameterPanel implements IParameterPanel {
	private ParameterSpinner parameterSpinner;
	private JLabel label = new JLabel();
	private JSpinner spinner;

	public ParameterSpinnerPanel(ParameterSpinner parameterSpinner) {
		this.parameterSpinner = parameterSpinner;
		spinner = new JSpinner();
	}
}
