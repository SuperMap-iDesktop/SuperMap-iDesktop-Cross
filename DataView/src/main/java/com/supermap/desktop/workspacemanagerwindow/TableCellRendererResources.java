package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.RESOURCES_ICON_PATH;

/**
 * @author YuanR
 */
public class TableCellRendererResources extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		//给第一列设置资源样式图标
		if (value.equals(ControlsProperties.getString("SymbolMarkerLibNodeName"))) {
			//暂未找到点符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("SymbolLineLibNodeName"))) {
			//暂未找到线符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
		}
		if (value.equals(ControlsProperties.getString("SymbolFillLibNodeName"))) {
			//暂未找到填充符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
		}
		return this;
	}
}

