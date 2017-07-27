package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterColor;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentDropDown;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by lixiaoyao on 2017/7/6.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.COLOR)
public class ParameterColorPanel extends SwingPanel implements IParameterPanel {
	private ParameterColor parameterColor;
	private JLabel label = new JLabel();
	private ComponentDropDown componentDropDown = new ComponentDropDown(ComponentDropDown.COLOR_TYPE);
	private boolean isSelectedItem = false;

	public ParameterColorPanel(IParameter parameterColor) {
		super(parameterColor);
		this.parameterColor = (ParameterColor) parameterColor;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterColor.getDescribe());
		initLayout();
		this.componentDropDown.setColor(((ParameterColor) parameterColor).getInitColor());
		initListeners();

	}

	private void initListeners() {
		this.componentDropDown.addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!isSelectedItem && componentDropDown.getColor()!=null) {
							try {
								isSelectedItem = true;
								parameterColor.setSelectedItem((componentDropDown.getColor()));
							} finally {
								isSelectedItem = false;
							}
						}
					}
				}
		);
		this.parameterColor.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem){
					isSelectedItem = true;
					ParameterColorPanel.this.componentDropDown.selectColor((Color)evt.getNewValue());
					isSelectedItem = false;
				}
			}
		});
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		componentDropDown.setPreferredSize(new Dimension(35, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(componentDropDown, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterColor.getDescribe();
		if (parameterColor.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
