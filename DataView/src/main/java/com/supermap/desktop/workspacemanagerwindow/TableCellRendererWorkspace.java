package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

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
		//给第一列设置图标
		if (value.equals(ControlsProperties.getString("String_ToolBar_HideDatasource"))) {
			this.setIcon(DataViewResources.getIcon(DATASOURCES_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {
			this.setIcon(DataViewResources.getIcon(MAPS_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {
			this.setIcon(DataViewResources.getIcon(SCENES_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {
			this.setIcon(DataViewResources.getIcon(LAYOUTS_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
		}
		return this;
	}
}
