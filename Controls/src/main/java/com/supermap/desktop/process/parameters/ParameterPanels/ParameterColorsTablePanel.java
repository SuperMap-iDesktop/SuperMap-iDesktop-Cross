package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterColorsTable;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by yuanR on 2017/9/5 0005.
 * 栅格颜色表参数面板
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.COLORSTABLE)
public class ParameterColorsTablePanel extends SwingPanel implements IParameterPanel {
	private ParameterColorsTable parameterColorsTable;
	private JLabel label = new JLabel();
	private ColorsComboBox colorsComboBox = new ColorsComboBox();

	public ParameterColorsTablePanel(IParameter parameterColor) {
		super(parameterColor);
		this.parameterColorsTable = (ParameterColorsTable) parameterColor;
		label.setText(getDescribe());
		label.setToolTipText(this.parameterColorsTable.getDescribe());
		initLayout();
		initListeners();
	}


	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		colorsComboBox.setPreferredSize(new Dimension(35, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(colorsComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}


	private void initListeners() {
		this.colorsComboBox.addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (colorsComboBox.getSelectedItem() != null) {
							parameterColorsTable.setSelectedItem((colorsComboBox.getSelectedItem()));
						}
					}
				}
		);
		this.parameterColorsTable.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ParameterColorsTablePanel.this.colorsComboBox.setSelectedItem((Colors) evt.getNewValue());
			}
		});
	}

	/**
	 * @return
	 */
	private String getDescribe() {
		String describe = parameterColorsTable.getDescribe();
		if (parameterColorsTable.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
