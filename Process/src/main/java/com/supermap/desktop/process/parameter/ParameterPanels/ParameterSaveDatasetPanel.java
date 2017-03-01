package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author XiaJT
 */
public class ParameterSaveDatasetPanel extends JPanel {
	private ParameterSaveDataset parameterSaveDataset;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDataset;
	private SmTextFieldLegit textFieldDataset;
	private boolean isSelectingItem = false;


	public ParameterSaveDatasetPanel(ParameterSaveDataset parameterSaveDataset) {
		this.parameterSaveDataset = parameterSaveDataset;
		labelDatasource = new JLabel(CommonProperties.getString(CommonProperties.Label_Datasource));
		labelDataset = new JLabel(CommonProperties.getString(CommonProperties.Label_Dataset));
		datasourceComboBox = new DatasourceComboBox();
		textFieldDataset = new SmTextFieldLegit();
		textFieldDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (isSelectingItem) {
					return true;
				}
				if (StringUtilities.isNullOrEmpty(textFieldDataset.getText())) {
					return false;
				}
				boolean isLegit = datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(textFieldValue);
				if (isLegit) {
					isSelectingItem = true;
					ParameterSaveDatasetPanel.this.parameterSaveDataset.setDatasetName(textFieldValue);
					isSelectingItem = false;
				}
				return isLegit;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName(currentValue);
			}
		});
		initLayout();
		initListener();
		initComponentState();
		parameterSaveDataset.setResultDatasource(datasourceComboBox.getSelectedDatasource());
	}

	private void initLayout() {
		labelDatasource.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasourceComboBox.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));

		this.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 0, 0));
		this.add(textFieldDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));


	}

	private void initListener() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterSaveDataset.setResultDatasource(datasourceComboBox.getSelectedDatasource());
					isSelectingItem = false;
				}
			}
		});
	}

	private void initComponentState() {
		isSelectingItem = true;
		if (parameterSaveDataset.getResultDatasource() != null) {
			datasourceComboBox.setSelectedDatasource(parameterSaveDataset.getResultDatasource());
		}
		if (!StringUtilities.isNullOrEmpty(parameterSaveDataset.getDatasetName())) {
			textFieldDataset.setText(parameterSaveDataset.getDatasetName());
		}
		isSelectingItem = false;

	}
}
