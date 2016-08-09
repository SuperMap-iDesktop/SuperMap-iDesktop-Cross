package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;

public class DatasetChooserDataTopo {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private MutiTable preprocessTable;
	private JDialogTopoPreProgress topoPreProgress;
	private DatasetChooser datasetChooser;

	public DatasetChooserDataTopo(JDialog owner, boolean flag, Datasource datasource, MutiTable preprocessTable, DatasetType[] datasetType) {
		this.preprocessTable = preprocessTable;
		this.topoPreProgress = (JDialogTopoPreProgress) owner;
		this.datasetChooser = new DatasetChooser(owner);
		this.datasetChooser.setSupportDatasetTypes(datasetType);
		if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
			this.datasetChooser.getWorkspaceTree().setSelectedDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
		}else if (null != Application.getActiveApplication().getWorkspace().getDatasources()){
			this.datasetChooser.getWorkspaceTree().setSelectedDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
		if (datasetChooser.showDialog() == DialogResult.OK) {
			addInfoToMainTable();
		}
	}

	private void addInfoToMainTable() {
		try {
			List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();

			int rowCount = selectedDatasets.size();
			HashSet<DatasetType> datasetTypes = new HashSet<DatasetType>();
			for (int i = 0; i < selectedDatasets.size(); i++) {
				Dataset dataset = selectedDatasets.get(i);
				Object[] temp = new Object[3];
				temp[0] = preprocessTable.getRowCount() + 1;
				DataCell datasetCell = new DataCell();
				datasetCell.initDatasetType(dataset);
				topoPreProgress.getComboBoxConsultDataset().addItem(datasetCell);
				temp[1] = datasetCell;
				String datasourceName = dataset.getDatasource().getAlias();
				Datasource dataSource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				datasetTypes.add(dataset.getType());
				DataCell cell = new DataCell();
				cell.initDatasourceType(dataSource);
				temp[2] = cell;
				// 设置容限值为最后一个矢量数据集的结点容限
				topoPreProgress.getTextFieldTolerance().setText(((DatasetVector) dataset).getTolerance().getNodeSnap() + "");
				preprocessTable.addRow(temp);
			}
			if (rowCount < preprocessTable.getRowCount()) {
				if (datasetTypes.contains(DatasetType.REGION)) {
					topoPreProgress.getCheckBoxArcsInserted().setEnabled(true);
					topoPreProgress.getCheckBoxPolygonsChecked().setEnabled(true);
					topoPreProgress.getCheckBoxVertexArcInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				} else if (datasetTypes.contains(DatasetType.LINE)) {
					topoPreProgress.getCheckBoxArcsInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexArcInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				} else if (datasetTypes.contains(DatasetType.POINT)) {
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				}
				preprocessTable.setRowSelectionInterval(rowCount, preprocessTable.getRowCount() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
