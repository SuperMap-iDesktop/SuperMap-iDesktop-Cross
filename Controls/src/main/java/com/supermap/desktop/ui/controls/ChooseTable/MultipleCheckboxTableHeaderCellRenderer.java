package com.supermap.desktop.ui.controls.ChooseTable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MultipleCheckboxTableHeaderCellRenderer implements TableCellRenderer {
    MultipleCheckboxTableModel tableModel;
    JTableHeader tableHeader;
    final JCheckBox checkBox;

    public MultipleCheckboxTableHeaderCellRenderer(JTable table, String title, boolean isSelected) {
        this.tableModel = (MultipleCheckboxTableModel) table.getModel();
        this.tableHeader = table.getTableHeader();
        this.checkBox = new JCheckBox(tableModel.getColumnName(0));
        this.checkBox.setSelected(isSelected);
        this.checkBox.setText(title);
        this.tableHeader.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    //获得选中列
                    int selectColumn = tableHeader.columnAtPoint(e.getPoint());
                    if (selectColumn == 0) {
                        boolean value = !checkBox.isSelected();
                        checkBox.setSelected(value);
                        tableModel.selectAllOrNull(value);
                        tableHeader.repaint();
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        String valueStr = (String) value;
        JLabel label = new JLabel(valueStr);
        label.setHorizontalAlignment(SwingConstants.RIGHT); // 表头标签居右边
        this.checkBox.setHorizontalAlignment(SwingConstants.LEFT);// 表头checkBox居中
        JComponent component = (column == 0) ? checkBox : label;

        component.setForeground(tableHeader.getForeground());
        component.setBackground(tableHeader.getBackground());
        component.setFont(tableHeader.getFont());
        component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

        return component;
    }

}
