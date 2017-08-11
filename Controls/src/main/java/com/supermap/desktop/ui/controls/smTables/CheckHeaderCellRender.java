package com.supermap.desktop.ui.controls.smTables;


import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public class CheckHeaderCellRender implements TableCellRenderer {
	IModel tableModel;
	JTableHeader tableHeader;
	final JCheckBox checkBox;

	/*
	*parm table要渲染的table，titletable标题，isSelectedCheckBox是否选中checkbox
	 */
	public CheckHeaderCellRender(JTable table, String title, boolean isSelectedCheckBox) {
		this.tableModel = (IModel) table.getModel();
		this.tableHeader = table.getTableHeader();
		this.checkBox = CheckHeaderModelFactory.getHeaderCheck(this.tableModel);
		this.checkBox.setSelected(isSelectedCheckBox);
		this.checkBox.setText(title);

		if (isSelectedCheckBox) {
			this.tableModel.getModelController().selectAllOrNull(true);
			tableHeader.repaint();
		}
		this.tableHeader.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					//获得选中列
					int selectColumn = tableHeader.columnAtPoint(e.getPoint());
					if (selectColumn == 0) {
						boolean value = !checkBox.isSelected();
						checkBox.setSelected(value);
						tableModel.getModelController().selectAllOrNull(value);
						tableHeader.repaint();
					}
				}
			}
		});
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		String valueStr = (String) value;
		JLabel label = new JLabel(valueStr);
		label.setHorizontalAlignment(SwingConstants.CENTER);   // 表头标签居左边
		this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);  // 表头checkBox居中
		JComponent component = (column == 0) ? checkBox : label;

		component.setForeground(tableHeader.getForeground());
		component.setBackground(tableHeader.getBackground());
		component.setFont(tableHeader.getFont());
		component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return component;
	}
}
