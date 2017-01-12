package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.implement.ParameterSpinner;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class ParameterSpinnerPanel extends JPanel {
	private ParameterSpinner parameterSpinner;
	private JLabel label = new JLabel();
	private JSpinner spinner;

	public ParameterSpinnerPanel(ParameterSpinner parameterSpinner) {
		this.parameterSpinner = parameterSpinner;
		spinner = new JSpinner();
	}
}
