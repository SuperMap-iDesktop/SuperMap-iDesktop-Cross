package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Layouts;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelLayouts extends AbstractTableModel {
	Layouts layouts;

	//获得工作空间以及列名
	public TableModelLayouts(Layouts layouts) {
		this.layouts = layouts;
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
		return this.layouts.getCount();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (this.layouts.getCount() > 0) {
			if (col == COLUMN_NAME) {
				return this.layouts.get(row);
			}
			if (col == COLUMN_TYPE) {
				return ControlsProperties.getString("String_ToolBar_HideLayout");
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


