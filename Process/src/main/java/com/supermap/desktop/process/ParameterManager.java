package com.supermap.desktop.process;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ParameterManager extends JPanel {
	private JPanel mainPanel = new JPanel();

	public ParameterManager() {
		this.setLayout(new GridBagLayout());
		this.add(mainPanel, new GridBagConstraintsHelper(0, 0, 1, 1));
	}

	public void setProcess(IProcess process) {
		mainPanel.removeAll();
		if (process != null) {
			mainPanel.add(process.getComponent(), new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		}
	}
}
