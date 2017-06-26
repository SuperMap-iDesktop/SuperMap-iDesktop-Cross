package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.ListDataCellRender;
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
@ParameterPanelDescribe(parameterPanelType = ParameterType.SAVE_DATASET)
public class ParameterSaveDatasetPanel extends SwingPanel implements IParameterPanel {
	private ParameterSaveDataset parameterSaveDataset;
	private JLabel labelDatasource;
	private JComboBox<Datasource> datasourceComboBox;
	private JLabel labelDataset;
	private SmTextFieldLegit textFieldDataset;
	private boolean isSelectingItem = false;
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
				isSelectingItem = true;
				parameterSaveDataset.setResultDatasource((Datasource) datasourceComboBox.getSelectedItem());
				isSelectingItem = false;
			}
		}
	};


	public ParameterSaveDatasetPanel(IParameter parameterSaveDataset) {
		super(parameterSaveDataset);
		this.parameterSaveDataset = (ParameterSaveDataset) parameterSaveDataset;
		labelDatasource = new JLabel(CommonProperties.getString(CommonProperties.Label_Datasource));
		labelDataset = new JLabel(CommonProperties.getString(CommonProperties.Label_Dataset));
		datasourceComboBox = new JComboBox<>();
		datasourceComboBox.setRenderer(new ListDataCellRender());
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
				if (datasourceComboBox.getSelectedItem() == null) {
					return true;
				}
				boolean isLegit = ((Datasource) datasourceComboBox.getSelectedItem()).getDatasets().isAvailableDatasetName(textFieldValue);
				if (isLegit) {
					isSelectingItem = true;
					ParameterSaveDatasetPanel.this.parameterSaveDataset.setDatasetName(textFieldValue);
					isSelectingItem = false;
				}
				return isLegit;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return ((Datasource) datasourceComboBox.getSelectedItem()).getDatasets().getAvailableDatasetName(currentValue);
			}
		});
		initLayout();
		initListener();
		initComponentState();
		this.parameterSaveDataset.setResultDatasource((Datasource) datasourceComboBox.getSelectedItem());
	}

	private void initLayout() {
		labelDatasource.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasourceComboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		panel.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));

		panel.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 0, 0));
		panel.add(textFieldDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));


	}

	private void initListener() {

		datasourceComboBox.addItemListener(itemListener);
	}

	private void initComponentState() {
		datasourceComboBoxInit();
		isSelectingItem = true;
		if (parameterSaveDataset.getResultDatasource() != null) {
			datasourceComboBox.setSelectedItem(parameterSaveDataset.getResultDatasource());
		}
		if (!StringUtilities.isNullOrEmpty(parameterSaveDataset.getDatasetName())) {
			textFieldDataset.setText(parameterSaveDataset.getDatasetName());
		}
		isSelectingItem = false;
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterSaveDataset.DATASOURCE_FIELD_NAME)) {
			datasourceComboBoxInit();
		}
	}

	private void datasourceComboBoxInit() {
		isSelectingItem = true;
		datasourceComboBox.removeAllItems();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource datasource = datasources.get(i);
			if (!datasource.isReadOnly() && parameterSaveDataset.isValueLegal(ParameterSaveDataset.DATASOURCE_FIELD_NAME, datasource)) {
				datasourceComboBox.addItem(datasource);
			}
		}
		datasourceComboBox.setSelectedItem(null);
		for (int i = 0; i < datasourceComboBox.getItemCount(); i++) {
			Datasource datasource = datasourceComboBox.getItemAt(0);
			Object valueSelected = parameterSaveDataset.isValueSelected(ParameterSaveDataset.DATASOURCE_FIELD_NAME, datasource);
			if (valueSelected != ParameterValueLegalListener.NO) {
				if (valueSelected == ParameterValueLegalListener.DO_NOT_CARE) {
					break;
				} else if (valueSelected instanceof Datasource) {
					datasourceComboBox.setSelectedItem(valueSelected);
					break;
				}
			}
		}

		if (datasourceComboBox.getSelectedItem() == null && datasourceComboBox.getItemCount() > 0) {
			if (parameterSaveDataset.getResultDatasource() != null && JComboBoxUIUtilities.getItemIndex(datasourceComboBox, parameterSaveDataset.getResultDatasource()) != -1) {
				datasourceComboBox.setSelectedItem(parameterSaveDataset.getResultDatasource());
			} else {
				isSelectingItem = false;
				datasourceComboBox.setSelectedIndex(0);
				isSelectingItem = true;
			}
		}
		isSelectingItem = false;
	}
}
