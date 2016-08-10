package com.supermap.desktop.ui.controls.borderPanel;

import javax.swing.*;
import java.awt.*;


public class SmComponentPanel extends JPanel {
	protected SmBorder border;
	protected JComponent component;
	protected JPanel panel;
	protected boolean transmittingAllowed;
	protected StateTransmitter transmitter;


	public SmComponentPanel(JComponent component) {
		this.component = component;
		border = new SmBorder(component);
		setBorder(border);
		panel = new JPanel();
		setLayout(null);
		add(component);
		add(panel);
		transmittingAllowed = false;
		transmitter = null;
	}

	public JComponent getTitleComponent() {
		return component;
	}

	public void setTitleComponent(JComponent newComponent) {
		remove(component);
		add(newComponent);
		border.setTitleComponent(newComponent);
		component = newComponent;
	}

	public JPanel getContentPane() {
		return panel;
	}

	public void doLayout() {
		Insets insets = getInsets();
		Rectangle rect = getBounds();
		rect.x = 0;
		rect.y = 0;

		Rectangle compR = border.getComponentRect(rect, insets);
		component.setBounds(compR);
		rect.x += insets.left;
		rect.y += insets.top;
		rect.width -= insets.left + insets.right;
		rect.height -= insets.top + insets.bottom;
		panel.setBounds(rect);
	}


	public void setTransmittingAllowed(boolean enable) {
		transmittingAllowed = enable;
	}

	public boolean getTransmittingAllowed() {
		return transmittingAllowed;
	}

	public void setTransmitter(StateTransmitter transmitter) {
		this.transmitter = transmitter;
	}

	public StateTransmitter getTransmitter() {
		return transmitter;
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		if (transmittingAllowed && transmitter != null) {
			transmitter.setChildrenEnabled(enable);
		}
	}

}