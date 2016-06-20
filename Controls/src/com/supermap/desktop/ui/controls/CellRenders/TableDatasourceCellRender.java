package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.data.Datasource;
import com.supermap.desktop.ui.controls.DataCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Table对应的getValueAt()得到的对象为数据源时可使用
 * <P>得到带图片的数据源
 *
 * @author XiaJT
 */
public class TableDatasourceCellRender extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result;
		if (value == null) {
			result = new JLabel();
		} else if (!(value instanceof Datasource)) {
			result = new JLabel(String.valueOf(value));
		} else {
			result = new DataCell(((Datasource) value));
		}
		if (isSelected) {
			result.setBackground(table.getSelectionBackground());
		} else {
			result.setBackground(table.getBackground());
		}
		return result;
	}
}
