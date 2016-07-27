package com.supermap.desktop.Interface;

import javax.swing.JTable;

/**
 * @author xie
 *
 */
public interface IFormLBSControl extends IForm {
	/**
	 * 获取选中行（单行选择）
	 * 
	 * @return 选中行
	 */
	int getSelectRow();

	/**
	 * 获取选中行（多行选择）
	 * 
	 * @return
	 */
	int[] getSelectRows();

	/**
	 * 从hdfs文件中删除
	 */
	void delete();

	/**
	 * 从hdfs文件中下载
	 */
	void downLoad();

	/**
	 * 获取当前所在的
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * 获取table
	 * 
	 * @return
	 */
	JTable getTable();

	/**
	 * 刷新
	 */
	void refresh();
}
