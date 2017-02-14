package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class DefaultParameters implements IParameters {
	private IParameter[] parameters;
	private JPanel panel;

	public DefaultParameters() {

	}

	@Override
	public void setParameters(IParameter[] iParameters) {
		if (this.parameters != null && this.parameters.length > 0) {
			for (IParameter parameter : parameters) {
				parameter.dispose();
			}
		}
		if (panel != null) {
			panel.removeAll();
		}
		panel = null;
		this.parameters = iParameters;
	}


	@Override
	public IParameter[] getParameters() {
		return parameters;
	}

	@Override
	public IParameter getParameter(String key) {
		for (IParameter parameter : parameters) {
			if (parameter.getType().equals(key)) {
				return parameter;
			}
		}
		return null;
	}

	@Override
	public IParameter getParameter(int index) {
		if (index >= 0 && index < parameters.length) {
			return parameters[index];
		}
		return null;
	}

	@Override
	public int size() {
		return parameters.length;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			for (int i = 0; i < parameters.length; i++) {
				panel.add(parameters[i].getPanel(), new GridBagConstraintsHelper(0, i, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
			}
			panel.add(new JPanel(), new GridBagConstraintsHelper(0, parameters.length, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		}
		return panel;
	}
}
