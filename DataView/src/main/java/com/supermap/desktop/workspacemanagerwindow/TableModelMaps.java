package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Maps;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelMaps extends AbstractTableModel {
	Maps maps;

	//获得工作空间以及列名
	public TableModelMaps(Maps maps) {
		this.maps = maps;
	}

	@Override
	public String getColumnName(int column) {
		if (column == COLUMN_NAME) {
			return CommonProperties.getString("String_Name");
		} else if (column == COLUMN_TYPE) {
			return CommonProperties.getString("String_Type");
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return this.maps.getCount();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (this.maps.getCount() > 0) {
			if (col == COLUMN_NAME) {
				return this.maps.get(row);
			}
			if (col == COLUMN_TYPE) {
				return ControlsProperties.getString("String_ToolBar_HideMap");
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





