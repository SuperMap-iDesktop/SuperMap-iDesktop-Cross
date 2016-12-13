package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
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
		if (column == COLUMN_NAME) {
			this.setIcon(DataViewResources.getIcon(DATAVIEW_ICON_ROOTPATH + table.getValueAt(row, COLUMN_TYPE).toString() + ".png"));
		}
		if (column == COLUMN_TYPE) {
			// 对数据类型显示内容进行转换，转换为中文
			String replaceString = DataViewProperties.getString("String_Dataset_T");
			String datasetTypeName = CommonToolkit.DatasetTypeWrap.findName((DatasetType) table.getValueAt(row, COLUMN_TYPE));
			String newDatasetTypeName = datasetTypeName.replace(replaceString, "");
			this.setText(newDatasetTypeName);
		}
		return this;
	}
}
