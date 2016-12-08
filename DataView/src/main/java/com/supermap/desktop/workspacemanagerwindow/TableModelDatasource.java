package com.supermap.desktop.workspacemanagerwindow;

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
public class TableModelDatasource extends AbstractTableModel {
	Datasource datasource;
	DatasetVector datasetVector;

	//获得工作空间以及列名
	public TableModelDatasource(Datasource datasource) {
		this.datasource = datasource;
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
		return this.datasource.getDatasets().getCount();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (this.datasource.getDatasets().getCount() > 0) {
			if (col == COLUMN_NAME) {
				return this.datasource.getDatasets().get(row).getName();
			}
			if (col == COLUMN_TYPE) {
				return this.datasource.getDatasets().get(row).getType();
			}
			if (col == COLUMN_NUMBER) {
				if (this.datasource.getDatasets().get(row) instanceof DatasetVector) {
					this.datasetVector = (DatasetVector) this.datasource.getDatasets().get(row);
					return this.datasetVector.getRecordCount();
				} else {
					return 0;
				}
			}
			if (col == COLUMN_PRJCOORDSYS) {
				return this.datasource.getDatasets().get(row).getPrjCoordSys().getName();
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



