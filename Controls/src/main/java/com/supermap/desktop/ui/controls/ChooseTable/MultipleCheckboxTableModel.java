package com.supermap.desktop.ui.controls.ChooseTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by lixiaoyao on 2017/3/20.
 */
public class MultipleCheckboxTableModel extends DefaultTableModel {
    private boolean enableColumn[];

    public MultipleCheckboxTableModel(Object[][] data, Object[] columnNames, boolean[] enableColumn) {
        super(data, columnNames);
        this.enableColumn = enableColumn;
    }

    // /**
    // * 根据类型返回显示控件
    // * 布尔类型返回显示checkbox
    // */
    @Override
    public Class getColumnClass(int c) {
        if (c==0){
            return JCheckBox.class;
        }else{
            return getValueAt(0, c).getClass();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.enableColumn[columnIndex];
    }

    public void selectAllOrNull(boolean value) {
        for (int i = 0; i < getRowCount(); i++) {
            MultipleCheckboxItem item = (MultipleCheckboxItem) this.getValueAt(i, 0);
            item.setSelected(value);
            this.setValueAt(item, i, 0);
        }
    }
}
