package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Scenes;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;

/**
 * @author YuanR
 */
public class TableModelScenes extends AbstractTableModel {
	Scenes scenes;

	//获得工作空间以及列名
	public TableModelScenes(Scenes scenes) {
		this.scenes = scenes;
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
		return this.scenes.getCount();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (this.scenes.getCount() > 0) {
			if (col == COLUMN_NAME) {
				return this.scenes.get(row);
			}
			if (col == COLUMN_TYPE) {
				return ControlsProperties.getString("String_ToolBar_HideScene");
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


