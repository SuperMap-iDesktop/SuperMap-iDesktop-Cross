package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.SCENES_ICON_PATH;

/**
 * @author YuanR
 */
public class TableCellRendererScenes extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		//给第一列设置“场景”样式图标
		this.setIcon(DataViewResources.getIcon(SCENES_ICON_PATH));
		return this;
	}
}

