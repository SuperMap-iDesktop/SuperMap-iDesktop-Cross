package com.supermap.desktop.ui.controls.cellRenders;

import com.supermap.data.Dataset;
import com.supermap.desktop.ui.controls.DataCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Table对应的getValueAt()得到的对象为数据集时可使用
 * <p/>
 * 得到带图片的数据集
 *
 * @author XiaJT
 */
public class TableDatasetCellRender extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result;
		if (value == null) {
			result = new JLabel();
		} else if (!(value instanceof Dataset)) {
			result = new JLabel(String.valueOf(value));
		} else {
			result = new DataCell(((Dataset) value));
		}
		if (isSelected) {
			result.setBackground(table.getSelectionBackground());
		} else {
			result.setBackground(table.getBackground());
		}
		return result;
	}
}
