package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class DefaultParameterPanel extends JPanel implements IParameterPanel {
	@Override
	public Object getPanel() {
		return this;
	}
}
