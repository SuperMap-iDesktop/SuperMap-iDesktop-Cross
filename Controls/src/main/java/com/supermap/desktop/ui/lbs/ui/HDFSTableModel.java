package com.supermap.desktop.ui.lbs.ui;


import com.supermap.desktop.lbs.HDFSDefine;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;

import java.util.Vector;

public class HDFSTableModel extends MutiTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[] title = new String[]{"Name", "Size", "BlockSize", "Owner", "Group", "Permission", "Replication"};

    /**
     * 构造函数。
     *
     * @param
     */
    public HDFSTableModel() {
    }

    @Override
    public String getColumnName(int column) {
        return title[column];
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    /**
     * 添加指定数据的一行。<br>
     *
     * @param define 　数据
     * @throws Exception 抛出数据数不正确的异常
     */
    public void addRow(HDFSDefine define) {
        if (null == define) {
            return;
        }

        // 初始化内容存储
        Vector<Object> content = new Vector<Object>(this.columnNames.size());
        content.add(define.getName());
        content.add(define.getSize());
        content.add(define.getBlockSize());
        content.add(define.getOwner());
        content.add(define.getGroup());
        content.add(define.getPermission());
        content.add(define.getReplication());

        // 追加内容
        contents.add(content);
        this.setRowTagAt(define, this.getRowCount() - 1);
    }
}

