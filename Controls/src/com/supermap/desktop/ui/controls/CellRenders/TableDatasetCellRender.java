package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.data.Dataset;
import com.supermap.desktop.ui.controls.DataCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Table对应的getValueAt()得到的对象为数据集时可使用
 * <P>得到带图片的数据集
 *
 * @author XiaJT
 */
public class TableDatasetCellRender extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (!(value instanceof Dataset)) {
			return new JLabel(String.valueOf(value));
		}
		Dataset dataset = (Dataset) value;
		DataCell dataCell = new DataCell(dataset);
		if (isSelected) {
			dataCell.setBackground(table.getSelectionBackground());
		} else {
			dataCell.setBackground(table.getBackground());
		}
		return dataCell;
	}
}
