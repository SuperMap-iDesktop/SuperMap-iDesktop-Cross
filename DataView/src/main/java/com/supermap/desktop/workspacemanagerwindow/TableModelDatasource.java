package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Datasources;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_PATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelDatasource extends AbstractTableModel {
	Datasources datasources;

	//获得工作空间以及列名
	public TableModelDatasource(Datasources datasources) {
		this.datasources = datasources;
	}

	@Override
	public String getColumnName(int column) {
		if (column == COLUMN_NAME) {
			return CommonProperties.getString("String_Name");
		} else if (column == COLUMN_TYPE) {
			return CommonProperties.getString("String_Type");
		} else if (column == COLUMN_NUMBER) {
			return DataViewProperties.getString("String_ObjectCount");
		} else if (column == COLUMN_PATH) {
			return DataViewProperties.getString("String_Path");
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return this.datasources.getCount();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		//增加">0"判断，防止索引越界
		if (this.datasources.getCount() > 0) {
			if (col == COLUMN_NAME) {
//				return this.datasources.get(row).getAlias();
				return this.datasources.get(row);
			}
			if (col == COLUMN_TYPE) {
				return this.datasources.get(row).getEngineType();
			}
			if (col == COLUMN_NUMBER) {
				return this.datasources.get(row).getDatasets().getCount();
			}
			if (col == COLUMN_PATH) {
				return this.datasources.get(row).getConnectionInfo().getServer();
			}
		}
		return "";
	}

	public Class getColumnClass(int col) {
		if (col == COLUMN_NAME) {
			return Icon.class;
		} else {
			return getValueAt(0, col).getClass();
		}
	}
}

