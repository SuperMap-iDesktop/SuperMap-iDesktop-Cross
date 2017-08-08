package com.supermap.desktop.ui;

import javax.swing.table.DefaultTableModel;

/**
 * Created by xie on 2016/12/8.
 */
public class CheckTableModel extends DefaultTableModel {
    private int enableColumn;

    public CheckTableModel(Object[][] data, Object[] columnNames, int enableColumn) {
        super(data, columnNames);
        this.enableColumn = enableColumn;
    }


    // /**
    // * 根据类型返回显示控件
    // * 布尔类型返回显示checkbox
    // */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (enableColumn == columnIndex) {
            return true;
        }
        return false;
    }

    public void selectAllOrNull(boolean value) {
        for (int i = 0; i < getRowCount(); i++) {
            this.setValueAt(value, i, 0);
        }
    }

}
