package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasetType;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.DatasetTypeComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by hanyz on 2017/4/25.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASETTYPE)
public class ParameterDatasetTypePanel extends SwingPanel {
	private JLabel label = new JLabel();
	private DatasetTypeComboBox datasetTypeComboBox = new DatasetTypeComboBox();
	private ParameterDatasetType parameterDatasetType;
	private boolean isSelectingItem;

	public ParameterDatasetTypePanel(IParameter parameterDatasetType) {
		super(parameterDatasetType);
		this.parameterDatasetType = ((ParameterDatasetType) parameterDatasetType);
		this.label.setText(this.parameterDatasetType.getDescribe());
		this.datasetTypeComboBox = new DatasetTypeComboBox(((ParameterDatasetType) parameterDatasetType).getSupportedDatasetTypes());
		if (((ParameterDatasetType) parameterDatasetType).isSimpleDatasetShown()) {
			this.datasetTypeComboBox.setSimpleDatasetShown(true);
		}
		if (((ParameterDatasetType) parameterDatasetType).isAllShown()) {
			this.datasetTypeComboBox.setAllShown(true);
		}
		this.datasetTypeComboBox.setSelectedItem(this.parameterDatasetType.getSelectedItem());
		initLayout();
		initListener();
		if (datasetTypeComboBox.getSelectedIndex() == -1 && datasetTypeComboBox.getItemCount() > 0) {
			datasetTypeComboBox.setSelectedIndex(0);
		}
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasetTypeComboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		panel.add(datasetTypeComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListener() {
		datasetTypeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterDatasetType.setSelectedItem(datasetTypeComboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
		parameterDatasetType.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(ParameterDatasetType.DATASETTYPE_FIELD_NAME)) {
					isSelectingItem = true;
					datasetTypeComboBox.setSelectedItem(evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});
	}
}
