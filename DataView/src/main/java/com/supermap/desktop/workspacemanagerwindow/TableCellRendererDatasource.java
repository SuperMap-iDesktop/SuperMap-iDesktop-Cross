package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.DatasetType;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NULL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.DATAVIEW_ICON_ROOTPATH;


/**
 * @author YuanR
 *         添加数据集图标,类型列用中文显示
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
		if (column == COLUMN_NUMBER) {
			if (table.getValueAt(row, COLUMN_TYPE).equals(DatasetType.GRID)) {

				String widthGrid = String.valueOf(table.getValueAt(row, COLUMN_NULL));
				String heightGrid = String.valueOf((Integer) value / (Integer) (table.getValueAt(row, COLUMN_NULL)));
				this.setText(widthGrid + "*" + heightGrid);
			}
			if (table.getValueAt(row, COLUMN_TYPE).equals(DatasetType.IMAGE)) {
				String widthImage = String.valueOf(table.getValueAt(row, COLUMN_NULL));
				String heightImage = String.valueOf((Integer) value / (Integer) (table.getValueAt(row, COLUMN_NULL)));
				this.setText(widthImage + "*" + heightImage);
			}
			this.setHorizontalAlignment(LEFT);
		}
		if (column == COLUMN_NULL) {
			this.setText("");
		}

		return this;
	}
}
