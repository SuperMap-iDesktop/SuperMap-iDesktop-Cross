/*
 * Copyright (c) 2009-2010 SIS CORPORATION, All rights reserved.
 */
package com.supermap.desktop.ui.controls.mutiTable.component;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

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
		this.setHorizontalAlignment(SwingConstants.CENTER);// 表头checkBox居中
		//this.setBorderPainted(true);   //   这句代码是针对checkbox，产生边框，即当列头的checkbox选中之后就会产生边框，进而产生一种凹陷的效果，但是不美观，所以注释掉   by  liwenfa
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
