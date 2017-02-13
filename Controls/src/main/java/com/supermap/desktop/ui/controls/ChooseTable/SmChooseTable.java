package com.supermap.desktop.ui.controls.ChooseTable;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.CheckTableModle;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/2/10.
 */
public class SmChooseTable extends JTable {
    CheckTableModle checkTableModle = null;
    private final static int checkColumnIndex=0;
    private final static int checkColumnIndexMaxSize=40;
    private final static int rowHeight=23;

    public SmChooseTable() {
        super();
        init();
    }

    private void init(){
        this.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(this,""));
    }

    public SmChooseTable(Object[][] data, Object[] columnNames, int enableColumn) {
        this.checkTableModle = new CheckTableModle(data, columnNames, enableColumn);
        this.setModel(this.checkTableModle);
        this.getColumn(this.getModel().getColumnName(checkColumnIndex)).setMaxWidth(checkColumnIndexMaxSize);
        this.setRowHeight(rowHeight);
        init();
    }


    public int getSelectedModelRow() {
        int selectedRows = -1;
        for (int i = 0; i < SmChooseTable.this.getRowCount(); i++) {
            if ((boolean) SmChooseTable.this.getValueAt(i, 0)) {
                selectedRows=i;
                break;
            }
        }
        return selectedRows;
    }

    /**
     * @param index 要获取的选中的多个行，对应列的值，当index为0时即是当前选中行的索引
     * @return
     */
    public ArrayList getSelectedModelRows(int index) {
        ArrayList<Object> selectedRows = new ArrayList<Object>();
        if (!isIndexValid(index)) {
            Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_setVauleIndexIsInValid"));
        } else {
            for (int i = 0; i < this.getRowCount(); i++) {
                if ((boolean) this.getValueAt(i, 0)) {
                    if (index != 0) {
                        selectedRows.add(this.getValueAt(i, index));
                    } else {
                        selectedRows.add(i);
                    }
                }
            }
        }
        return selectedRows;
    }

    private boolean isIndexValid(int index) {
        boolean result = true;
        if (index < 0 || index > this.getColumnCount() - 1) {
            result = false;
        }
        return result;
    }
}
