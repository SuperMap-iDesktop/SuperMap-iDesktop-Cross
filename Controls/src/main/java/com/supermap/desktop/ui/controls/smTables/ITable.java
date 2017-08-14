package com.supermap.desktop.ui.controls.smTables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public interface ITable {

	public ITableController getTableController();

	public JTable getTable();

	public DefaultTableModel getTablesModel();

	//    当前table如果含有checkbox选择列，则必须返回true，否则返回false
	//    如果返回为true，则model类必须继承AbstractModel,根据相应的功能与model的checkbox列样式，实现相应的方法
	public Boolean isCheckBoxColumn();
}
