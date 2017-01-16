package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterDatasetPanel extends JPanel {
	private ParameterDataset parameterDataset;
	private JLabel labelDatasource = new JLabel();
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDataset = new JLabel();
	private DatasetComboBox datasetComboBox;
	private boolean isSelectingItem = false;

	public ParameterDatasetPanel(ParameterDataset parameterDataset) {
		this.parameterDataset = parameterDataset;
		labelDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
		labelDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		this.datasourceComboBox = new DatasourceComboBox(Application.getActiveApplication().getWorkspace().getDatasources());
		if (datasourceComboBox.getSelectItem() != null) {
			datasetComboBox = new DatasetComboBox(datasourceComboBox.getSelectedDatasource().getDatasets());
		} else {
			datasetComboBox = new DatasetComboBox();
			datasetComboBox.removeAllItems();
		}

		initLayout();
		initListener();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
		this.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

		this.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
		this.add(datasetComboBox, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initListener() {
		parameterDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					Object newValue = evt.getNewValue();
					if (newValue instanceof Dataset) {
						Dataset dataset = (Dataset) newValue;
						datasourceComboBox.setSelectedDatasource(dataset.getDatasource());
						datasetComboBox.setSelectedDataset(dataset);
					}
					isSelectingItem = false;
				}
			}
		});
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					datasetComboBox.setDatasets(datasourceComboBox.getSelectedDatasource().getDatasets());
				}
			}
		});
		datasetComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterDataset.setSelectedItem(datasetComboBox.getSelectedDataset());
					isSelectingItem = false;
				}
			}
		});

//		Application.getActiveApplication().getWorkspace().addClosedListener();
	}
}