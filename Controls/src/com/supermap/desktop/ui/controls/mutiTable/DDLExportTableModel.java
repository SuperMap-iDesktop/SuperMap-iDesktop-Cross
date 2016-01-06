/*
 * Copyright (c) 2009-2010 SIS CORPORATION, All rights reserved.
 */
package com.supermap.desktop.ui.controls.mutiTable;

import com.supermap.desktop.Application;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;

/**
 * @author 李海軍
 * @since 1.0.0
 * @version 1.0.0 2012/10/24 
 * <p>
 */
public class DDLExportTableModel extends MutiTableModel {
    /**
     * ID
     */
    private static final long serialVersionUID = 2901808435846952546L;

    /**
     * 列标题
     */
    private static final String[] HEAD_TITLE = new String[] {"check", "text"};

    /**
     * 构造函数。
     */
    public DDLExportTableModel() {
        super(HEAD_TITLE);
    }
    
    /**
     * 构造函数。
     * @param columnNames
     */
    public DDLExportTableModel(String[] columnNames) {
        super(columnNames);
    }

    /**
     * 构造函数。
     * 
     * @param datas 数据内容
     * @throws Exception 数据不正常的异常
     */
    public DDLExportTableModel(Object[][] datas, String[] columnNames) {
    	super(columnNames);
    	// 更新数据
        try {
			refreshContents(datas);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
    }
}
