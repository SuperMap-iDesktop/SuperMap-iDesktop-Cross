package com.supermap.desktop.ui.controls.datasetChoose;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasetCellRender;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasourceCellRender;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.utilities.DatasetTypeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * DatasetChooser可选模块，不建议直接新建
 *
 * @author XiaJT
 */
public class DatasetChooserDataset implements IDatasetChoose {

	private DatasetChooser datasetChooser;
	private DatasetTableModel tableModel;

	public DatasetChooserDataset(DatasetChooser datasetChooser) {
		this.datasetChooser = datasetChooser;
		tableModel = new DatasetTableModel();
	}

	public void initTable() {
		JTable table = datasetChooser.getTable();
		table.setModel(tableModel);
		table.getColumnModel().getColumn(DatasetTableModel.COLUMN_DATASET_NAME).setCellRenderer(new TableDatasetCellRender());
		table.getColumnModel().getColumn(DatasetTableModel.COLUMN_DATASOURCE).setCellRenderer(new TableDatasourceCellRender());
	}

	@Override
	public void initializeTableInfo(Object currentNodeData) {
		if (currentNodeData != null && currentNodeData instanceof Datasource) {
			tableModel.removeAll();
			Datasource datasource = (Datasource) currentNodeData;
			Datasets datasets = datasource.getDatasets();

			for (int i = 0; i < datasets.getCount(); i++) {
				Dataset dataset = datasets.get(i);
				if (datasetChooser.isAllowedDataset(dataset)) {
					this.tableModel.addDataset(dataset);
				}
				if (dataset instanceof DatasetVector && ((DatasetVector) dataset).getChildDataset() != null) {
					DatasetVector childDataset = ((DatasetVector) dataset).getChildDataset();
					if (datasetChooser.isAllowedDataset(childDataset)) {
						this.tableModel.addDataset(childDataset);
					}
				}
			}
		}
	}


	@Override
	public List<Object> getSelectedValues(int[] selectedRows) {

		return tableModel.getSelectedDatasets(selectedRows);
	}

	private class DatasetTableModel extends DefaultTableModel {
		private List<Dataset> datasetList = new ArrayList<>();
		public static final int COLUMN_DATASET_NAME = 0;
		public static final int COLUMN_DATASOURCE = 1;
		public static final int COLUMN_DATASET_TYPE = 2;

		private final String[] columnNames = {
				CommonProperties.getString(CommonProperties.stringDataset),
				CommonProperties.getString(CommonProperties.stringDatasource),
				CommonProperties.getString(CommonProperties.STRING_DATASET_TYPE)
		};

		@Override
		public Object getValueAt(int row, int col) {
			if (this.datasetList == null || this.datasetList.size() <= 0) {
				return null;
			}
			Dataset dataset = this.datasetList.get(row);
			switch (col) {
				case COLUMN_DATASET_NAME:
					return dataset;
				case COLUMN_DATASOURCE:
					return dataset.getDatasource();
				case COLUMN_DATASET_TYPE:
					return DatasetTypeUtilities.toString(dataset.getType());
				default:
					return "";
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public int getRowCount() {
			if (this.datasetList == null) {
				return 0;
			}
			return this.datasetList.size();
		}

		@Override
		public int getColumnCount() {
			return this.columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return this.columnNames[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == COLUMN_DATASET_NAME || columnIndex == COLUMN_DATASOURCE) {
				return DataCell.class;
			} else {
				return String.class;
			}
		}

		public void removeAll() {
			int size = datasetList.size();
			if (size > 0) {
				this.datasetList.clear();
				fireTableRowsDeleted(0, size - 1);
			}
		}

		public void addDataset(Dataset dataset) {
			if (this.datasetList == null) {
				this.datasetList = new ArrayList<>();
			}
			this.datasetList.add(dataset);
			fireTableDataChanged();
		}

		public List<Object> getSelectedDatasets(int[] selectedRows) {
			List<Object> resultDataset = new ArrayList<>();
			for (int selectedRow : selectedRows) {
				resultDataset.add(this.datasetList.get(selectedRow));
			}
			return resultDataset;
		}
	}

	@Override
	public void dispose() {

	}
}
