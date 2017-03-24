package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetCreatedEvent;
import com.supermap.data.DatasetCreatedListener;
import com.supermap.data.DatasetDeletedAllEvent;
import com.supermap.data.DatasetDeletedAllListener;
import com.supermap.data.DatasetDeletedEvent;
import com.supermap.data.DatasetDeletedListener;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
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
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
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
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASET)
public class ParameterDatasetPanel extends SwingPanel {
	private ParameterDataset parameterDataset;
	private JLabel labelDatasource = new JLabel();
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDataset = new JLabel();
	private DatasetComboBox datasetComboBox;
	private boolean isSelectingItem = false;
	private Datasource currentSelectedDatasource;

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
	private DatasetDeletedListener datasetDeletedListener = new DatasetDeletedListener() {
		@Override
		public void DatasetDeleted(DatasetDeletedEvent datasetDeletedEvent) {
			datasetsChanged();
		}
	};
	;
	private DatasetDeletedAllListener datasetDeletedAllListener = new DatasetDeletedAllListener() {
		@Override
		public void datasetDeletedAll(DatasetDeletedAllEvent datasetDeletedAllEvent) {
			datasetsChanged();
		}
	};
	private DatasetCreatedListener datasetCreatedListener = new DatasetCreatedListener() {
		@Override
		public void datasetCreated(DatasetCreatedEvent datasetCreatedEvent) {
			datasetsChanged();
		}
	};

	public ParameterDatasetPanel(IParameter parameterDataset) {
		super(parameterDataset);
		this.parameterDataset = (ParameterDataset) parameterDataset;
		labelDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
		labelDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		this.datasourceComboBox = new DatasourceComboBox(Application.getActiveApplication().getWorkspace().getDatasources());
		if (datasourceComboBox.getSelectedItemAlias() != null) {
			datasetComboBox = new DatasetComboBox(datasourceComboBox.getSelectedDatasource().getDatasets());
		} else {
			datasetComboBox = new DatasetComboBox();
			datasetComboBox.removeAllItems();
		}

		initLayout();
		initListener();
	}

	private void initLayout() {
		labelDatasource.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasourceComboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
		panel.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));

		panel.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(5, 0, 0, 0));
		panel.add(datasetComboBox, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));

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
					currentDatasourceChanged(datasourceComboBox.getSelectedDatasource());
					datasetComboBox.setDatasets(currentSelectedDatasource.getDatasets());
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

		Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.addOpenedListener(new WorkspaceOpenedListener() {
			@Override
			public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
				removeDatasourcesListener();
				datasourceComboBox.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), null);
				addDatasourcesListeners();
			}
		});
		workspace.addClosingListener(new WorkspaceClosingListener() {
			@Override
			public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
				removeDatasourcesListener();
				datasourceComboBox.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), null);
				addDatasourcesListeners();
			}
		});

	}

	private void currentDatasourceChanged(Datasource selectedDatasource) {
		removeCurrentSelectedDatasourceListener(selectedDatasource);
		currentSelectedDatasource = datasourceComboBox.getSelectedDatasource();
		addCurrentSelectedDatasourceListener(selectedDatasource);
	}

	private void addCurrentSelectedDatasourceListener(Datasource selectedDatasource) {
		Datasets datasets = selectedDatasource.getDatasets();
		datasets.addCreatedListener(datasetCreatedListener);
		datasets.addDeletedAllListener(datasetDeletedAllListener);
		datasets.addDeletedListener(datasetDeletedListener);

	}

	private void removeCurrentSelectedDatasourceListener(Datasource selectedDatasource) {
		Datasets datasets = selectedDatasource.getDatasets();
		datasets.removeCreatedListener(datasetCreatedListener);
		datasets.removeDeletedAllListener(datasetDeletedAllListener);
		datasets.removeDeletedListener(datasetDeletedListener);

	}

	private void datasetsChanged() {
		isSelectingItem = true;
		Dataset selectedDataset = datasetComboBox.getSelectedDataset();
		datasetComboBox.setDatasets(currentSelectedDatasource.getDatasets());
		datasetComboBox.setSelectedDataset(selectedDataset);
		isSelectingItem = false;
		if (datasetComboBox.getSelectedIndex() == -1 && datasetComboBox.getItemCount() > 0) {
			datasetComboBox.setSelectedIndex(0);
		}
	}

	private void addDatasourcesListeners() {
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		datasources.addCreatedListener(datasourceCreatedListener);
		datasources.addOpenedListener(datasourceOpenedListener);
		datasources.addClosedListener(datasourceClosedListener);
	}

	private void removeDatasourcesListener() {
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
}
