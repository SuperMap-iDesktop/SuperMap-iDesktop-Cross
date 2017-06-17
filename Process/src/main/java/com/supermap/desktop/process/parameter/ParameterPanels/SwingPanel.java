package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.events.PanelPropertyChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.ui.UICommonToolkit;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class SwingPanel extends DefaultParameterPanel {
	protected JPanel panel = new JPanel();
	private boolean isFirstGetPanel = true;

	public SwingPanel(IParameter parameter) {
		super(parameter);
		parameter.addPanelPropertyChangedListener(new PanelPropertyChangedListener() {
			@Override
			public void propertyChanged(PropertyChangeEvent propertyChangeEvent) {
				panelPropertyChanged(propertyChangeEvent);
			}


		});
	}
	protected void panelPropertyChanged(PropertyChangeEvent propertyChangeEvent) {
		if (propertyChangeEvent.getPropertyName().equals(PanelPropertyChangedListener.ENABLE)) {
			UICommonToolkit.setComponentEnabled(panel, (Boolean) propertyChangeEvent.getNewValue());
		}
	}
	@Override
	public Object getPanel() {
		if (isFirstGetPanel) {
			UICommonToolkit.setComponentEnabled(panel, parameter.isEnabled());
			isFirstGetPanel = false;
		}
		return panel;
	}
}
