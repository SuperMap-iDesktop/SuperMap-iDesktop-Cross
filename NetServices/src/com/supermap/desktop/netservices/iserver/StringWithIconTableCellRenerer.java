package com.supermap.desktop.netservices.iserver;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 带图标和文字的 TableCellRenderer
 * 
 * @author highsad
 *
 */
public class StringWithIconTableCellRenerer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringWithIconTableCellRenerer() {
		super();
		setHorizontalAlignment(JLabel.LEADING);
	}

	@Override
	public void setValue(Object value) {
		setText((value instanceof StringWithIcon) ? value.toString() : "");
		setIcon((value instanceof StringWithIcon) ? ((StringWithIcon) value).getIcon() : null);
	}
}
