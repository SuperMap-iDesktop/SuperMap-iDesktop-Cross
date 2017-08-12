package com.supermap.desktop.ui;

import com.supermap.data.FieldInfos;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
public class TableModelOutputExcel extends DefaultTableModel {
	private FieldInfos fieldInfos;
	private static String[] column = new String[]{
			CommonProperties.getString("String_Name"),
			CommonProperties.getString("String_Field_Caption"),
			CommonProperties.getString("String_FieldType")
	};

	public TableModelOutputExcel(IFormTabular tabular) {
		this.fieldInfos = tabular.getRecordset().getFieldInfos();
	}

	@Override
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return super.getColumnCount();
	}

	@Override
	public String getColumnName(int column) {
		return super.getColumnName(column);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return super.isCellEditable(row, column);
	}

	@Override
	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
	}
}
