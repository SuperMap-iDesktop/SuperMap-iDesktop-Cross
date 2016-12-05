package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelResources extends AbstractTableModel {
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
		//资源行数暂定为3
		return 3;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row == 0) {
			return ControlsProperties.getString("SymbolMarkerLibNodeName");
		} else if (row == 1) {
			return ControlsProperties.getString("SymbolLineLibNodeName");
		} else if (row == 2) {
			return ControlsProperties.getString("SymbolFillLibNodeName");
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


