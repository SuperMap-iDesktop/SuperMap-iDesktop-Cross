package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_PRJCOORDSYS;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelDatasType extends AbstractTableModel {
	private Datasource datasource;
	private DatasetType datasetType;

	private int sum = 0;
	private Dataset[] aimDataset;

	//获得工作空间以及列名
	public TableModelDatasType(Datasource datasource, DatasetType dataType) {
		this.datasetType = dataType;
		this.datasource = datasource;
		//预处理
		for (int i = 0; i < this.datasource.getDatasets().getCount(); i++) {
			if ((this.datasource.getDatasets().get(i).getType()).equals(this.datasetType)) {
				sum++;
			}
		}
		this.aimDataset = new Dataset[sum];
		int m = 0;
		for (int i = 0; i < this.datasource.getDatasets().getCount(); i++) {
			if ((this.datasource.getDatasets().get(i).getType()).equals(this.datasetType)) {
				this.aimDataset[m] = this.datasource.getDatasets().get(i);
				m++;
			}
		}
	}

	@Override
	public String getColumnName(int column) {
		if (column == COLUMN_NAME) {
			return CommonProperties.getString("String_Name");
		} else if (column == COLUMN_TYPE) {
			return CommonProperties.getString("String_Type");
		} else if (column == COLUMN_NUMBER) {
			return DataViewProperties.getString("String_ObjectCount");
		} else if (column == COLUMN_PRJCOORDSYS) {
			return ControlsProperties.getString("String_PrjCoorSys");
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return sum;
	}

	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (sum > 0) {
			if (col == COLUMN_NAME) {
				return this.aimDataset[row].getName();
			}
			if (col == COLUMN_TYPE) {
				return this.datasetType;
			}
			if (col == COLUMN_NUMBER) {
				if (this.aimDataset[row].getType().equals(DatasetType.POINT)
						|| this.aimDataset[row].getType().equals(DatasetType.POINT)
						|| this.aimDataset[row].getType().equals(DatasetType.LINE)
						|| this.aimDataset[row].getType().equals(DatasetType.REGION)
						|| this.aimDataset[row].getType().equals(DatasetType.TEXT)
						|| this.aimDataset[row].getType().equals(DatasetType.CAD)) {
					DatasetVector datasetVector = (DatasetVector) this.aimDataset[row];
					return datasetVector.getRecordCount();
				} else {
					//其他数据类型，暂不显示对象个数的，默认为0
					return 0;
				}
			}
			if (col == COLUMN_PRJCOORDSYS) {
				return this.aimDataset[row].getPrjCoordSys().getName();
			}
		}
		return "";
	}

	public Class getColumnClass(int col) {
		//当列数为“1”，返回Icon,否则返回String
		if (col == COLUMN_NAME) {
			return Icon.class;
		} else {
			return getValueAt(0, col).getClass();
		}
	}
}
