package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class SwingPanel extends DefaultParameterPanel {
	protected JPanel panel = new JPanel();

	public SwingPanel(IParameter parameter) {
		super(parameter);
	}

	@Override
	public Object getPanel() {
		return panel;
	}
}
