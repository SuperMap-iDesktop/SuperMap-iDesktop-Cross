package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.SymbolFillLibrary;
import com.supermap.data.SymbolLineLibrary;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.RESOURCES_ICON_PATH;

/**
 * @author YuanR
 */
public class TableCellRendererResource extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		//给第一列设置资源样式图标
		if (value instanceof SymbolMarkerLibrary) {
			//暂未找到点符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
			this.setText(ControlsProperties.getString("SymbolMarkerLibNodeName"));
		}
		if (value instanceof SymbolLineLibrary) {
			//暂未找到线符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
			this.setText(ControlsProperties.getString("SymbolLineLibNodeName"));
		}
		if (value instanceof SymbolFillLibrary) {
			//暂未找到填充符号图标
			this.setIcon(DataViewResources.getIcon(RESOURCES_ICON_PATH));
			this.setText(ControlsProperties.getString("SymbolFillLibNodeName"));
		}
		return this;
	}
}

