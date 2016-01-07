/*
 * Copyright (c) 2009-2010 SIS CORPORATION, All rights reserved.
 */
package com.supermap.desktop.ui.controls.mutiTable.component;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author 李海軍
 * @since 1.0.0
 * @version 1.0.0 2012/10/23 初版
 *          <p>
 */
public class HeaderCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	/**
	 * ID
	 */
	private static final long serialVersionUID = -3224639986882887200L;

	/**
	 * 构造函数。<br>
	 */
	public HeaderCheckBoxRenderer() {
		this.setBorderPainted(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return this;
	}

}
