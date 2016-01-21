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
public class TabelDatasourceCellRender extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (!(value instanceof Datasource)) {
			return new JLabel(String.valueOf(value));
		}
		Datasource datasource = (Datasource) value;
		DataCell dataCell = new DataCell(datasource);
		if (isSelected) {
			dataCell.setBackground(table.getSelectionBackground());
		} else {
			dataCell.setBackground(table.getBackground());
		}
		dataCell.setForeground(Color.red);
		return dataCell;
	}
}
