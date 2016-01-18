package com.supermap.desktop.CtrlAction.Dataset.Pyramid;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.desktop.CtrlAction.Dataset.CtrlActionDeleteImagePyramid;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.DatasetUtilties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.progress.callable.CreateImagePyramidCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SillyB on 2016/1/1.
 */
public class PyramidManagerTableModel extends SortableTableModel {

	private List<Dataset> currentDatasets = new ArrayList<>();
	private String[] columnNames = new String[]{
			CommonProperties.getString("String_ColumnHeader_Dataset"),
			CommonProperties.getString("String_ColumnHeader_Datasource"),
			DataEditorProperties.getString("String_FormDatasetPyramidManager_ColumnHasPyramid"),
			DataEditorProperties.getString("String_FormDatasetPyramidManager_ColumnWidth"),
			DataEditorProperties.getString("String_FormDatasetPyramidManager_ColumnHeight"),
	};


	@Override
	public int getRowCount() {
		if (currentDatasets == null) {
			return 0;
		}
		return currentDatasets.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		int resultRow = getIndexRow(row);
		if (resultRow != -1) {
			row = resultRow;
		}
		Dataset dataset = currentDatasets.get(row);
		switch (column) {
			case 0:
				return dataset;
			case 1:
				return dataset.getDatasource();
			case 2:
				return isDatasetHasPyramid(dataset) ? CommonProperties.getString(CommonProperties.True) : CommonProperties.getString(CommonProperties.False);
			case 3:
				return getDatasetWidth(dataset);
			case 4:
				return getDatasetHeight(dataset);
			default:
				return null;
		}
	}

	private boolean isDatasetHasPyramid(Dataset dataset) {
		if (dataset instanceof DatasetGrid) {
			return ((DatasetGrid) dataset).getHasPyramid();
		} else if (dataset instanceof DatasetGridCollection) {
			return ((DatasetGridCollection) dataset).getHasPyramid();

		} else if (dataset instanceof DatasetImage) {
			return ((DatasetImage) dataset).getHasPyramid();

		} else if (dataset instanceof DatasetImageCollection) {
			return ((DatasetImageCollection) dataset).getHasPyramid();

		}
		return false;
	}

	private int getDatasetHeight(Dataset dataset) {
		if (dataset instanceof DatasetGrid) {
			return ((DatasetGrid) dataset).getHeight();
		} else if (dataset instanceof DatasetImage) {
			return ((DatasetImage) dataset).getHeight();
		}
		return 0;
	}

	private int getDatasetWidth(Dataset dataset) {
		if (dataset instanceof DatasetGrid) {
			return ((DatasetGrid) dataset).getWidth();
		} else if (dataset instanceof DatasetImage) {
			return ((DatasetImage) dataset).getWidth();

		}
		return 0;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0 || columnIndex == 1) {
			return DataCell.class;
		} else {
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void deleteRows(int[] selectedRows) {
		if (indexes != null) {
			for (int i = 0; i < selectedRows.length; i++) {
				int selectedRow = selectedRows[i];
				selectedRows[i] = (int) indexes.get(selectedRow);
			}
		}
		super.removeRows(selectedRows);
		Arrays.sort(selectedRows);
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			currentDatasets.remove(selectedRows[i]);
		}
		fireTableDataChanged();
	}

	/**
	 * 是否存在可创建影像金字塔的数据集
	 *
	 * @return 是否可创建
	 */
	public boolean isCreateEnable() {
		for (Dataset currentDataset : currentDatasets) {
			if (!isDatasetHasPyramid(currentDataset)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回是否存在可删除影像金字塔的数据集
	 *
	 * @return 是否可删除
	 */
	public boolean isRemoveEnable() {
		for (Dataset currentDataset : currentDatasets) {
			if (isDatasetHasPyramid(currentDataset)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 创建影像金字塔
	 *
	 * @return 是否创建
	 */
	public boolean bulidPyramid() {
		List<Dataset> datasets = new ArrayList();
		for (Dataset currentDataset : currentDatasets) {
			if (!isDatasetHasPyramid(currentDataset)) {
				datasets.add(currentDataset);
			}
		}
		datasets = DatasetUtilties.sureDatasetClosed(datasets);
		if (datasets == null || datasets.size() <= 0) {
			return false;
		}
		FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
		formProgressTotal.doWork(new CreateImagePyramidCallable(datasets.toArray(new Dataset[datasets.size()])));
		fireTableDataChanged();
		return true;
	}

	/**
	 * 删除影像金字塔
	 *
	 * @return 是否删除
	 */
	public boolean deletePyramid() {
		List<Dataset> datasets = new ArrayList<>();
		for (Dataset currentDataset : currentDatasets) {
			if (isDatasetHasPyramid(currentDataset)) {
				datasets.add(currentDataset);
			}
		}
		datasets = DatasetUtilties.sureDatasetClosed(datasets);
		if (datasets == null || datasets.size() <= 0) {
			return false;
		}
		CtrlActionDeleteImagePyramid.deleteImagePyramid(datasets);
		fireTableDataChanged();
		return true;
	}

	public void addDataset(List<Dataset> selectedDatasets) {
		boolean isAdded = false;
		for (Dataset selectedDataset : selectedDatasets) {
			if (!currentDatasets.contains(selectedDataset)) {
				currentDatasets.add(selectedDataset);
				if (indexes != null) {
					indexes.put(this.getRowCount() - 1, this.getRowCount() - 1);
				}
				isAdded = true;
			}
		}
		if (isAdded) {
			fireTableDataChanged();
		}
	}

	public void setCurrentDatasets(List<Dataset> activeSupportDatasets) {
		if (activeSupportDatasets == null) {
			this.currentDatasets.clear();
		} else {
			this.currentDatasets = activeSupportDatasets;
		}
		indexes = null;
		fireTableDataChanged();
	}
}
