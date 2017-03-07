package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetCreatedEvent;
import com.supermap.data.DatasetCreatedListener;
import com.supermap.data.DatasetDeletingAllEvent;
import com.supermap.data.DatasetDeletingAllListener;
import com.supermap.data.DatasetDeletingEvent;
import com.supermap.data.DatasetDeletingListener;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceClosingEvent;
import com.supermap.data.DatasourceClosingListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosedEvent;
import com.supermap.data.WorkspaceClosedListener;
import com.supermap.data.WorkspaceCreatedEvent;
import com.supermap.data.WorkspaceCreatedListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDatasetPanel extends JPanel {
	private ParameterSingleDataset parameterSingleDataset;
	private JLabel labelDataset;
	private DatasetComboBox datasetComboBox;
	private boolean isSelectingItem = false;
	private DatasetType[] datasetTypes;
	private Datasource datasource;
	private DatasetCreatedListener datasetCreatedListener = new DatasetCreatedListener() {
		@Override
		public void datasetCreated(DatasetCreatedEvent datasetCreatedEvent) {
			isSelectingItem = true;
			datasetComboBox.addDataset(ParameterSingleDatasetPanel.this.datasource.getDatasets().get(datasetCreatedEvent.getDatasetName()));
			isSelectingItem = false;
		}
	};
	private DatasetDeletingListener datasetDeletingListener = new DatasetDeletingListener() {
		@Override
		public void datasetDeleting(DatasetDeletingEvent datasetDeletingEvent) {
			isSelectingItem = true;
			boolean isDeleted = false;
			try {
				if (((Dataset) parameterSingleDataset.getSelectedItem()).getName().equals(datasetDeletingEvent.getDatasetName())) {
					isDeleted = true;
				}
			} catch (Exception e) {
				isDeleted = true;
			}
			datasetComboBox.removeItem(ParameterSingleDatasetPanel.this.datasource.getDatasets().get(datasetDeletingEvent.getDatasetName()));
			isSelectingItem = false;
			if (isDeleted && datasetComboBox.getItemCount() > 0) {
				datasetComboBox.setSelectedIndex(0);
			}
		}
	};
	private DatasetDeletingAllListener datasetDeletingAllListener = new DatasetDeletingAllListener() {
		@Override
		public void datasetDeletingAll(DatasetDeletingAllEvent datasetDeletingAllEvent) {
			isSelectingItem = true;
			datasetComboBox.removeAllItems();
			parameterSingleDataset.setSelectedItem(null);
			isSelectingItem = false;
		}
	};

	public ParameterSingleDatasetPanel(ParameterSingleDataset parameterSingleDataset, DatasetType[] datasetTypes) {
		this.parameterSingleDataset = parameterSingleDataset;
		this.datasetTypes = datasetTypes;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initListener();
	}

	private void initComponents() {
		this.labelDataset = new JLabel();
		this.labelDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
			setSelectedDatasource(datasource);
			this.datasetComboBox = new DatasetComboBox(datasource.getDatasets());
			this.datasetComboBox.setSupportedDatasetTypes(datasetTypes);
			parameterSingleDataset.setSelectedItem(datasetComboBox.getSelectedDataset());
		}
	}

	private void setSelectedDatasource(Datasource datasource) {
		removeDatasourceListener(this.datasource);
		this.datasource = datasource;
		addDatasourceListener(this.datasource);
		if (datasetComboBox == null) {
			return;
		}
		if (datasource == null) {
			datasetComboBox.removeAllItems();
		} else {
			datasetComboBox.setDatasets(datasource.getDatasets());
		}
	}

	private void removeDatasourceListener(Datasource datasource) {
		if (datasource != null) {
			datasource.getDatasets().removeCreatedListener(datasetCreatedListener);
			datasource.getDatasets().removeDeletingListener(datasetDeletingListener);
			datasource.getDatasets().removeDeletingAllListener(datasetDeletingAllListener);
		}
	}

	private void addDatasourceListener(Datasource datasource) {
		if (datasource != null) {
			datasource.getDatasets().addCreatedListener(datasetCreatedListener);
			datasource.getDatasets().addDeletingListener(datasetDeletingListener);
			datasource.getDatasets().addDeletingAllListener(datasetDeletingAllListener);
		}
	}

	private void initLayout() {
		labelDataset.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasetComboBox.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(labelDataset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		this.add(datasetComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListener() {
		final Workspace workspace = Application.getActiveApplication().getWorkspace();
		workspace.addClosedListener(new WorkspaceClosedListener() {
			@Override
			public void workspaceClosed(WorkspaceClosedEvent workspaceClosedEvent) {
				workspaceChanged();
			}
		});
		workspace.addOpenedListener(new WorkspaceOpenedListener() {
			@Override
			public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
				workspaceChanged();
			}
		});
		workspace.addCreatedListener(new WorkspaceCreatedListener() {
			@Override
			public void workspaceCreated(WorkspaceCreatedEvent workspaceCreatedEvent) {
				workspaceChanged();
			}
		});
		Datasources datasources = workspace.getDatasources();
		datasources.addClosingListener(new DatasourceClosingListener() {
			@Override
			public void datasourceClosing(DatasourceClosingEvent datasourceClosingEvent) {

				if (datasourceClosingEvent.getDatasource() == datasource) {
					Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
					for (int i = 0; i < datasources.getCount(); i++) {
						if (datasources.get(i) != datasource) {
							setSelectedDatasource(datasources.get(i));
							return;
						}
					}
					setSelectedDatasource(null);
				}
			}
		});
		datasources.addOpenedListener(new DatasourceOpenedListener() {
			@Override
			public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
				if (datasource == null) {
					setSelectedDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
				}
			}
		});
		datasources.addCreatedListener(new DatasourceCreatedListener() {
			@Override
			public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
				if (datasource == null) {
					setSelectedDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
				}
			}
		});
		this.datasetComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED && null != datasetComboBox.getSelectedDataset()) {
					isSelectingItem = true;
					parameterSingleDataset.setSelectedItem(datasetComboBox.getSelectedDataset());
					isSelectingItem = false;
				}
			}
		});
		this.parameterSingleDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					if (evt.getNewValue() instanceof Dataset)
						datasetComboBox.setSelectedDataset((Dataset) evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});

	}

	private void workspaceChanged() {
		setSelectedDatasource(null);
		parameterSingleDataset.setSelectedItem(null);
		datasetComboBox.setDatasets(null);
	}


}
