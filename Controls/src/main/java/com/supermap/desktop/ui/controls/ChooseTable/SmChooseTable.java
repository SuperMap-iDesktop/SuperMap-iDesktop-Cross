package com.supermap.desktop.ui.controls.ChooseTable;

import com.supermap.desktop.ui.CheckTableModle;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;

import javax.swing.*;

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
        this.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(this, ""));
    }

    public SmChooseTable(Object data[][], Object titles[]) {
        this.checkTableModle = new CheckTableModle(data, titles, enableColumn);
        this.setModel(this.checkTableModle);
        init();
    }

}
