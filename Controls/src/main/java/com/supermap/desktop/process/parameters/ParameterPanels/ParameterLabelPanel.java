package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterLabel;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.LABEL)
public class ParameterLabelPanel extends SwingPanel {
	private ParameterLabel parameter;
	private JLabel label = new JLabel();

	public ParameterLabelPanel(IParameter parameter) {
		super(parameter);
		this.parameter = ((ParameterLabel) parameter);
		label.setText(parameter.getDescribe());
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		initComponent();
		initListeners();
	}

	private void initComponent() {
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initListeners() {
		parameter.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					ParameterLabelPanel.this.label.setText(evt.getNewValue() == null ? null : evt.getNewValue().toString());
				}
			}
		});
	}

}
