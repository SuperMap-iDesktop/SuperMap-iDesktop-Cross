package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Datasources;
import com.supermap.data.Layouts;
import com.supermap.data.Maps;
import com.supermap.data.Resources;
import com.supermap.data.Scenes;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.DATASOURCES_ICON_PATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.LAYOUTS_ICON_PATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.MAPS_ICON_PATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.RESOURCES_ICON_PATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.SCENES_ICON_PATH;

/**
 * @author YuanR
 */
public class TableCellRendererWorkspace extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		//给第一列设置图标/显示字符串
		if (value instanceof Datasources) {
			this.setIcon(DataViewResources.getIcon(DATASOURCES_ICON_PATH));
			this.setText(ControlsProperties.getString("String_ToolBar_HideDatasource"));
		}
		if (value instanceof Maps) {
			this.setIcon(DataViewResources.getIcon(MAPS_ICON_PATH));
			this.setText(ControlsProperties.getString("String_ToolBar_HideMap"));
		}
		if (value instanceof Scenes) {
			this.setIcon(DataViewResources.getIcon(SCENES_ICON_PATH));
			this.setText(ControlsProperties.getString("String_ToolBar_HideScene"));
		}
		if (value instanceof Layouts) {
			this.setIcon(DataViewResources.getIcon(LAYOUTS_ICON_PATH));
			this.setText(ControlsProperties.getString("String_ToolBar_HideLayout"));
		}
		if (value instanceof Resources) {
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
			this.setText(ControlsProperties.getString("String_ToolBar_HideResources"));

		}
		if (column == COLUMN_NUMBER) {
			//靠左对齐
			this.setHorizontalAlignment(LEFT);
		}
		return this;
	}
}
