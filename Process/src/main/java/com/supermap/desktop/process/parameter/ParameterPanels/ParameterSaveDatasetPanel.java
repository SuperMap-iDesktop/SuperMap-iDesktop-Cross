package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.DatasourceClosedEvent;
import com.supermap.data.DatasourceClosedListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
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
@ParameterPanelDescribe(parameterPanelType = ParameterType.SAVE_DATASET)
public class ParameterSaveDatasetPanel extends DefaultParameterPanel implements IParameterPanel {
	private ParameterSaveDataset parameterSaveDataset;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDataset;
	private SmTextFieldLegit textFieldDataset;
	private boolean isSelectingItem = false;
	private DatasourceCreatedListener datasourceCreatedListener = new DatasourceCreatedListener() {
		@Override
		public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
			datasourcesChanged();
		}
	};

	private DatasourceClosedListener datasourceClosedListener = new DatasourceClosedListener() {
		@Override
		public void datasourceClosed(DatasourceClosedEvent datasourceClosedEvent) {
			boolean isDeleteSelectedDatasource = datasourceClosedEvent.getDatasource() == datasourceComboBox.getSelectedDatasource();
			datasourcesChanged();
			if (isDeleteSelectedDatasource) {
				isSelectingItem = true;
				datasourceComboBox.setSelectedIndex(-1);
				isSelectingItem = false;
				if (datasourceComboBox.getItemCount() > 0) {
					datasourceComboBox.setSelectedIndex(0);
				}
			}
		}
	};
	private DatasourceOpenedListener datasourceOpenedListener = new DatasourceOpenedListener() {
		@Override
		public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
			datasourcesChanged();
		}
	};
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
				isSelectingItem = true;
				parameterSaveDataset.setResultDatasource(datasourceComboBox.getSelectedDatasource());
				isSelectingItem = false;
			}
		}
	};


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
				if (datasourceComboBox.getSelectedDatasource() == null) {
					return true;
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

		datasourceComboBox.addItemListener(itemListener);
		addDatasourcesListeners();
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.addOpenedListener(new WorkspaceOpenedListener() {
			@Override
			public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
				removeSelectedDatasourceListener();
				datasourceComboBox.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), null);
				addDatasourcesListeners();
			}
		});
		workspace.addClosingListener(new WorkspaceClosingListener() {
			@Override
			public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
				removeSelectedDatasourceListener();
				datasourceComboBox.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), null);
				addDatasourcesListeners();
			}
		});
	}

	private void addDatasourcesListeners() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		datasources.addCreatedListener(datasourceCreatedListener);
		datasources.addOpenedListener(datasourceOpenedListener);
		datasources.addClosedListener(datasourceClosedListener);
	}

	private void removeSelectedDatasourceListener() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		datasources.removeCreatedListener(datasourceCreatedListener);
		datasources.removeOpenedListener(datasourceOpenedListener);
		datasources.removeClosedListener(datasourceClosedListener);
	}

	private void datasourcesChanged() {
		isSelectingItem = true;
		datasourceComboBox.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), datasourceComboBox.getSelectedDatasource());
		isSelectingItem = false;
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
