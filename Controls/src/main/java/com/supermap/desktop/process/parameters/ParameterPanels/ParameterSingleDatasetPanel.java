package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
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
@ParameterPanelDescribe(parameterPanelType = ParameterType.SINGLE_DATASET)
public class ParameterSingleDatasetPanel extends SwingPanel implements IParameterPanel {
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

	public ParameterSingleDatasetPanel(IParameter parameterSingleDataset) {
		super(parameterSingleDataset);
		this.parameterSingleDataset = (ParameterSingleDataset) parameterSingleDataset;
//		this.datasetTypes = datasetTypes;
		init();
	}

	public DatasetType[] getDatasetTypes() {
		return datasetTypes;
	}

	public void setDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
		this.datasetComboBox.setSupportedDatasetTypes(datasetTypes);// bug?
		this.parameterSingleDataset.setSelectedItem(datasetComboBox.getSelectedDataset());
	}

	private void init() {
		initComponents();
		initLayout();
		initListener();
	}

	private void initComponents() {
		this.labelDataset = new JLabel();
		this.labelDataset.setText(parameterSingleDataset.getDescribe());
		this.datasetTypes = parameterSingleDataset.getDatasetTypes();
		this.datasource = parameterSingleDataset.getDatasource();
		if (this.datasource != null) {
			this.datasetComboBox = new DatasetComboBox(this.datasource.getDatasets());
		} else {
			this.datasetComboBox = new DatasetComboBox();

		}

		this.datasetComboBox.setShowNullValue(parameterSingleDataset.isShowNullValue());
		this.datasetComboBox.setSupportedDatasetTypes(datasetTypes);
		if (this.datasource != null){
			Object selectedItem = parameterSingleDataset.getSelectedItem();
			if (selectedItem != null && selectedItem instanceof Dataset) {
				this.datasetComboBox.setSelectedDataset((Dataset) selectedItem);
			}
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
		try {
			if (datasource != null && datasource.isConnected()) {
				datasource.getDatasets().removeCreatedListener(datasetCreatedListener);
				datasource.getDatasets().removeDeletingListener(datasetDeletingListener);
				datasource.getDatasets().removeDeletingAllListener(datasetDeletingAllListener);
			}
		} catch (Exception e) {
			// ignore 数据源没有判断是否已经被关闭的方法，只有调用之后抛异常才知道
			// The data source does not determine whether the method has been closed, only after the call to throw exceptions to know
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
		panel.setLayout(new GridBagLayout());
		panel.add(labelDataset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		panel.add(datasetComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
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
		final Datasources datasources = workspace.getDatasources();
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
				} else if (null == datasetComboBox.getSelectedDataset()) {
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

		this.parameterSingleDataset.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				if (event.getFieldName().equals(ParameterSingleDataset.DATASOURCE_FIELD_NAME)) {
					Datasources datasources1 = Application.getActiveApplication().getWorkspace().getDatasources();
					for (int i = 0; i < datasources1.getCount(); i++) {
						if (parameterSingleDataset.isValueLegal(ParameterSingleDataset.DATASOURCE_FIELD_NAME, datasources1.get(i))) {
							Object valueSelected = parameterSingleDataset.isValueSelected(ParameterSingleDataset.DATASOURCE_FIELD_NAME, datasources1.get(i));
							if (valueSelected == ParameterValueLegalListener.DO_NOT_CARE) {
								setSelectedDatasource(datasources1.get(i));
							} else if (valueSelected != ParameterValueLegalListener.NO) {
								setSelectedDatasource((Datasource) valueSelected);
							} else {
								continue;
							}
							break;
						}
					}
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
