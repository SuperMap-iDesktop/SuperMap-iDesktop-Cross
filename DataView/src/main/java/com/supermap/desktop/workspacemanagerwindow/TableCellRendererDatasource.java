package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.DATAVIEW_ICON_ROOTPATH;


/**
 * @author YuanR
 *         添加数据集图标
 */

public class TableCellRendererDatasource extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		//根据数据集类型添加图标
		this.setIcon(DataViewResources.getIcon(DATAVIEW_ICON_ROOTPATH + table.getValueAt(row, COLUMN_TYPE).toString() + ".png"));
		return this;
	}
}
