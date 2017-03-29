package com.supermap.desktop.ui.controls.ChooseTable;

import com.supermap.desktop.ui.CheckTableModle;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lixiaoyao on 2017/2/28.
 */
public class SmChooseTable extends JTable {
    private CheckTableModle checkTableModle = null;
    private final static int rowHeight = 23;
    private final static int enableColumn = 0;
    private int checkColumnIndex = 0;
    private final static int checkColumnIndexMaxSize = 40;

    private void init() {
        this.getColumn(this.getModel().getColumnName(this.checkColumnIndex)).setMaxWidth(checkColumnIndexMaxSize);
        this.setRowHeight(rowHeight);
        this.getTableHeader().getColumnModel().getColumn(checkColumnIndex).setHeaderRenderer(new CheckHeaderCellRenderer(this, "", true));
    }

    //  选择列除了checkbox外没有任何其他文字或图片
    public SmChooseTable(Object data[][], Object titles[]) {
        this.checkTableModle = new CheckTableModle(data, titles, enableColumn);
        this.setModel(this.checkTableModle);
        init();
        this.removeMouseListener(this.mouseListener);
    }

    //  选择列checkbox带有显示文字则用这个
    public SmChooseTable(Object data[][], Object titles[], boolean columnEditables[]) {
        MultipleCheckboxTableModel multipleCheckboxTableModel =new MultipleCheckboxTableModel(data,titles,columnEditables);
        this.setModel(multipleCheckboxTableModel);
        this.setRowHeight(rowHeight);
        this.removeMouseListener(this.mouseListener);
        this.addMouseListener(this.mouseListener);
    }

    private MouseListener mouseListener=new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row=SmChooseTable.this.getSelectedRow();
            int column = SmChooseTable.this.getSelectedColumn();
            if (column==checkColumnIndex){
                MultipleCheckboxItem multipleCheckboxItem=(MultipleCheckboxItem)SmChooseTable.this.getValueAt(row,column);
                boolean value = !multipleCheckboxItem.getSelected();
                multipleCheckboxItem.setSelected(value);
                SmChooseTable.this.setValueAt(multipleCheckboxItem, row, column);
            }
        }
    };
}
