package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

/**
 * @author XiaJT
 */
public class EmptyParameterPanel implements IParameterPanel {
	private Object panel;

	public EmptyParameterPanel() {
		this(null);
	}

	public EmptyParameterPanel(Object panel) {
		this.panel = panel;
	}

	public void setPanel(Object panel) {
		this.panel = panel;
	}

	@Override
	public Object getPanel() {
		return panel;
	}

}
