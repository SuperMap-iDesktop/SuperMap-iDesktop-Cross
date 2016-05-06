package com.supermap.desktop.CtrlAction.Dataset.SpatialIndex;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexInfo;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilties.DatasetUtilties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilties.SpatialIndexTypeUtilties;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 空间分析的tableModel
 *
 * @author XiaJT
 */
public class SpatialIndexTableModel extends SortableTableModel {

	private List<SpatialIndexTableModelBean> datas = new ArrayList<>();

	private String[] columnNames = new String[]{
			CommonProperties.getString("String_ColumnHeader_Dataset"),
			CommonProperties.getString("String_ColumnHeader_Datasource"),
			DataEditorProperties.getString("String_CurrentInedxType"),
			DataEditorProperties.getString("String_DealIndexType")
	};
	public static final int COLUMN_DATASET = 0;
	public static final int COLUMN_DATASOURCE = 1;
	public static final int COLUMN_CURRENT_SPATIAL_INDEX_TYPE = 2;
	public static final int COLUMN_DEAL_INDEX_TYPE = 3;

	@Override
	public int getRowCount() {
		if (this.datas == null) {
			return 0;
		} else {
			return this.datas.size();
		}
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
	public boolean isCellEditable(int row, int column) {
		return column == COLUMN_DEAL_INDEX_TYPE;
	}

	@Override
	public Object getValueAt(int row, int column) {
		row = getIndexRow(row)[0];
		SpatialIndexTableModelBean spatialIndexTableModelBean = this.datas.get(row);
		switch (column) {
			case COLUMN_DATASET:
				return spatialIndexTableModelBean.getDataset();
			case COLUMN_DATASOURCE:
				return spatialIndexTableModelBean.getDataset().getDatasource();
			case COLUMN_CURRENT_SPATIAL_INDEX_TYPE:
				return SpatialIndexTypeUtilties.toString(((DatasetVector) spatialIndexTableModelBean.getDataset()).getSpatialIndexType());
			case COLUMN_DEAL_INDEX_TYPE:
				return SpatialIndexTypeUtilties.toString(spatialIndexTableModelBean.getSpatialIndexType());
			default:
				return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		row = getIndexRow(row)[0];
		if (column != COLUMN_DEAL_INDEX_TYPE) {
			return;
		}
		SpatialIndexType spatialIndexType = SpatialIndexTypeUtilties.valueOf(String.valueOf(aValue));
		this.datas.get(row).setSpatialIndexType(spatialIndexType);
		fireTableCellUpdated(row, column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		if (columnIndex == COLUMN_DATASET || columnIndex == COLUMN_DATASOURCE) {
			return DataCell.class;
		} else {
			return String.class;
		}
	}

	/**
	 * 添加数据集
	 *
	 * @param selectedDatasets 需要添加的数据集
	 */
	public void addDatasets(List<Dataset> selectedDatasets) {
		boolean isAdded = false;
		List<Dataset> currentDatasets = getCurrentDatasets();
		for (Dataset selectedDataset : selectedDatasets) {
			if (selectedDataset.isReadOnly()) {
				String message = MessageFormat.format(DataEditorProperties.getString("String_DatasetSpatialIndexControl_ReadOnlyDatasetError"), selectedDataset.getName());
				Application.getActiveApplication().getOutput().output(message);
			} else if (!currentDatasets.contains(selectedDataset) && SpatialIndexTableModelBean.isSupportDatasetType(selectedDataset.getType())) {
				this.datas.add(new SpatialIndexTableModelBean(selectedDataset));
				super.addIndexRow(getRowCount() - 1);
				isAdded = true;
			}

		}
		if (isAdded) {
			fireTableDataChanged();
		}
	}

	private List<Dataset> getCurrentDatasets() {
		List<Dataset> result = new ArrayList<>();
		for (SpatialIndexTableModelBean data : datas) {
			result.add(data.getDataset());
		}
		return result;
	}

	/**
	 * 删除选中行
	 *
	 * @param selectedRows 选中行
	 */
	protected void removeRowsHook(int[] selectedRows) {
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			this.datas.get(selectedRows[i]).dispose();
			this.datas.remove(selectedRows[i]);
		}
	}

	/**
	 * 构建空间索引
	 */
	public boolean bulid() {
		List<Dataset> datasets = new ArrayList<>();
		for (SpatialIndexTableModelBean data : datas) {
			datasets.add(data.getDataset());
		}
		List<Dataset> datasetsClosed = DatasetUtilties.sureDatasetClosed(datasets);

		if (datasetsClosed.size() <= 0) {
			return false;
		}
		List<SpatialIndexTableModelBean> tableModelBeans = new ArrayList<>();
		for (SpatialIndexTableModelBean data : datas) {
			if (datasetsClosed.contains(data.getDataset())) {
				tableModelBeans.add(data);
			}
		}
		fireTableDataChanged();
		FormProgress formProgress = new FormProgress(DataEditorProperties.getString("String_CreateDatasetSpatialIndex"));
		formProgress.doWork(new BulidSpatialIndexCallable(tableModelBeans, this));
		return true;
	}

	public SpatialIndexInfo getSpatialIndexInfo(int row) {
		return this.datas.get(row).getSpatialIndexInfo();
	}

	public void setSpatialIndexInfoValue(int selectedRow, String propetName, Object value) {
		selectedRow = getIndexRow(selectedRow)[0];
		datas.get(selectedRow).updateValue(propetName, value);
	}
}
