package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceClosedEvent;
import com.supermap.data.DatasourceClosedListener;
import com.supermap.data.DatasourceClosingEvent;
import com.supermap.data.DatasourceClosingListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.Datasources;
import com.supermap.data.Enum;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerRelocateDatasetPropertyModel;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class LayerRelocateDatasetPropertyControl extends AbstractLayerPropertyControl implements ILayerProperty {

	private static final long serialVersionUID = 1L;

	private JLabel labelDatasource;
	private JLabel labelDataset;

	private DatasourceComboBox comboBoxDatasource;
	private DatasetComboBox comboBoxDataset;

	private boolean itemListenerLock = false;

	 private WorkspaceClosingListener workspaceClosingListener = new WorkspaceClosingListener() {
		@Override
		public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
			Application.getActiveApplication().getWorkspace().getDatasources().removeClosingListener(datasourceClosingEvent);
			Application.getActiveApplication().getWorkspace().getDatasources().removeClosedListener(datasourceClosedListener);
			Application.getActiveApplication().getWorkspace().getDatasources().removeOpenedListener(datasourceOpenedListener);
			Application.getActiveApplication().getWorkspace().getDatasources().removeCreatedListener(datasourceCreatedListener);
			Application.getActiveApplication().getWorkspace().removeClosingListener(this);
		}
	};
	private DatasourceCreatedListener datasourceCreatedListener = new DatasourceCreatedListener() {
		@Override
		public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
			resetComboboxs();
		}
	};

	private DatasourceClosingListener datasourceClosingEvent = new DatasourceClosingListener(){
		@Override
		public void datasourceClosing(DatasourceClosingEvent datasourceClosingEvent) {
			try {
				if (getLayerPropertyModel().getDataset() != null && datasourceClosingEvent.getDatasource().equals(getLayerPropertyModel().getDataset().getDatasource())) {
					getLayerPropertyModel().setDataset(null);
				}
			} catch (Exception e) {
				getLayerPropertyModel().setDataset(null);
			}
		}
	};

	private DatasourceClosedListener datasourceClosedListener = new DatasourceClosedListener() {
		@Override
		public void datasourceClosed(DatasourceClosedEvent datasourceClosedEvent) {
			resetComboboxs();
		}
	};
	private DatasourceOpenedListener datasourceOpenedListener = new DatasourceOpenedListener() {
		@Override
		public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
			resetComboboxs();
		}
	};
	private ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();

	public LayerRelocateDatasetPropertyControl() {
		// TODO
	}

	@Override
	public LayerRelocateDatasetPropertyModel getLayerPropertyModel() {
		return (LayerRelocateDatasetPropertyModel) super.getLayerPropertyModel();
	}

	@Override
	protected LayerRelocateDatasetPropertyModel getModifiedLayerPropertyModel() {
		return (LayerRelocateDatasetPropertyModel) super.getModifiedLayerPropertyModel();
	}

	@Override
	protected void initializeComponents() {
		this.setBorder(BorderFactory.createTitledBorder("RelocateDataset"));

		this.labelDatasource = new JLabel("Datasource:");
		this.labelDataset = new JLabel("Dataset");

		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDataset = new DatasetComboBox();

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(labelDatasource, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelDataset, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(comboBoxDataset, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelDatasource)
						.addComponent(comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelDataset)
						.addComponent(comboBoxDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
		setComponentName();
	}
	private void setComponentName() {
		ComponentUIUtilities.setName(this.labelDatasource, "LayerRelocateDatasetPropertyControl_labelDatasource");
		ComponentUIUtilities.setName(this.labelDataset, "LayerRelocateDatasetPropertyControl_labelDataset");
		ComponentUIUtilities.setName(this.comboBoxDatasource, "LayerRelocateDatasetPropertyControl_comboBoxDatasource");
		ComponentUIUtilities.setName(this.comboBoxDataset, "LayerRelocateDatasetPropertyControl_comboBoxDataset");
	}
	@Override
	protected void initializeResources() {
		((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_FormRelateDataset_Title"));
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
	}

	private DatasetType[] getSupportDatasetTypes() {
		if (getLayerPropertyModel().getDataset() != null) {
			return new DatasetType[]{getLayerPropertyModel().getDataset().getType()};
		}

		List<DatasetType> datasetTypeList = new ArrayList<>();
		Enum[] enums = DatasetType.getEnums(DatasetType.class);
		for (Enum anEnum : enums) {
			if (anEnum != DatasetType.TABULAR && anEnum != DatasetType.TOPOLOGY) {
				datasetTypeList.add((DatasetType) anEnum);
			}
		}
		return (DatasetType[]) datasetTypeList.toArray(new DatasetType[datasetTypeList.size()]);
	}


	@Override
	protected void fillComponents() {
		if (getLayerPropertyModel() != null) {
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			Datasource activeDatasource = null;
			if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
				activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
			}
			this.comboBoxDatasource.resetComboBox(datasources, activeDatasource);

			if (getLayerPropertyModel().getDataset() != null) {
				// 图层数据集不为空
				this.comboBoxDatasource.setSelectedDatasource(getLayerPropertyModel().getDataset().getDatasource());
				this.comboBoxDataset.setSupportedDatasetTypes(getSupportDatasetTypes());
				this.comboBoxDataset.setDatasets(getLayerPropertyModel().getDataset().getDatasource().getDatasets());
				this.comboBoxDataset.setSelectedDataset(getLayerPropertyModel().getDataset());
			} else if (comboBoxDatasource.getSelectedDatasource() != null) {
				// 数据集为空 打开的数据源不为空
				resetComboboxDataset();
				this.comboBoxDataset.setSelectedIndex(-1);
			} else {
				// 数据集为空 打开的数据源也为空
				this.comboBoxDataset.setDatasets(null);
			}
		}
	}

	private void resetComboboxs() {
		comboBoxDatasource.removeItemListener(comboBoxItemListener);
		comboBoxDataset.removeItemListener(comboBoxItemListener);

		comboBoxDatasource.resetComboBox(Application.getActiveApplication().getWorkspace().getDatasources(), comboBoxDatasource.getSelectedDatasource());
		if (comboBoxDatasource.getSelectedDatasource() != null) {
			Dataset selectedDataset = comboBoxDataset.getSelectedDataset();
			resetComboboxDataset();
			comboBoxDataset.setSelectedDataset(selectedDataset);
		} else {
			comboBoxDataset.setDatasets(null);
		}
		comboBoxDatasource.addItemListener(comboBoxItemListener);
		comboBoxDataset.addItemListener(comboBoxItemListener);
	}

	@Override
	protected void registerEvents() {
		this.comboBoxDatasource.addItemListener(comboBoxItemListener);
		this.comboBoxDataset.addItemListener(comboBoxItemListener);

		Application.getActiveApplication().getWorkspace().addClosingListener(workspaceClosingListener);
		Application.getActiveApplication().getWorkspace().getDatasources().addClosingListener(datasourceClosingEvent);
		Application.getActiveApplication().getWorkspace().getDatasources().addClosedListener(datasourceClosedListener);
		Application.getActiveApplication().getWorkspace().getDatasources().addOpenedListener(datasourceOpenedListener);
		Application.getActiveApplication().getWorkspace().getDatasources().addCreatedListener(datasourceCreatedListener);
	}


	@Override
	protected void unregisterEvents() {
		this.comboBoxDatasource.removeItemListener(comboBoxItemListener);
		this.comboBoxDataset.removeItemListener(comboBoxItemListener);
		Application.getActiveApplication().getWorkspace().getDatasources().removeClosingListener(datasourceClosingEvent);
		Application.getActiveApplication().getWorkspace().getDatasources().removeClosedListener(datasourceClosedListener);
		Application.getActiveApplication().getWorkspace().getDatasources().removeOpenedListener(datasourceOpenedListener);
		Application.getActiveApplication().getWorkspace().getDatasources().removeCreatedListener(datasourceCreatedListener);
	}

	private void comboBoxDatasourceSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				resetComboboxDataset();
				this.comboBoxDataset.setSelectedIndex(-1);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void resetComboboxDataset() {
		this.comboBoxDataset.setSupportedDatasetTypes(getSupportDatasetTypes());
		this.comboBoxDataset.setDatasets(this.comboBoxDatasource.getSelectedDatasource().getDatasets());
	}

	private void comboBoxDatasetSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				getModifiedLayerPropertyModel().setDataset(this.comboBoxDataset.getSelectedDataset());
				checkChanged();
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private class ComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (itemListenerLock) {
				return;
			}
			itemListenerLock = true;
			if (e.getSource() == comboBoxDatasource) {
				comboBoxDatasourceSelectedItemChanged(e);
			} else if (e.getSource() == comboBoxDataset) {
				comboBoxDatasetSelectedItemChanged(e);
			}
			itemListenerLock = false;
		}
	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {
		if (propertyName.equals(LayerRelocateDatasetPropertyModel.DATASET)) {
			this.comboBoxDataset.setEnabled(enabled);
			this.comboBoxDatasource.setEnabled(enabled);
		}
	}
}
