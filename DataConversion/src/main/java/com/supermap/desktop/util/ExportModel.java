package com.supermap.desktop.util;

import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

//自定义表格模型
public class ExportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {
			DataConversionProperties.getString("string_tabletitle_dataset"),
			DataConversionProperties.getString("string_tabletitle_datasource"),
			DataConversionProperties.getString("string_tabletitle_state"),
			DataConversionProperties.getString("string_tabletitle_filename") };
	private ArrayList<ExportFileInfo> exports;

	public ExportModel(List<ExportFileInfo> exports) {
		this.exports = (ArrayList<ExportFileInfo>) exports;
	}

	public List<ExportFileInfo> getExports() {
		return exports;
	}

	public void setExports(List<ExportFileInfo> exports) {
		this.exports = (ArrayList<ExportFileInfo>) exports;
	}

	@Override
	public int getRowCount() {
		return exports.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public ExportFileInfo getTagValueAt(int tag) {
		return this.exports.get(tag);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ExportFileInfo exportFileInfo = exports.get(rowIndex);
		if (0 == columnIndex) {
			return exportFileInfo.getDatasetCell();
		}
		if (1 == columnIndex) {
			return exportFileInfo.getDatasourceCell();
		}
		if (2 == columnIndex) {
			return exportFileInfo.getState();
		}
		if (3 == columnIndex) {
			return exportFileInfo.getFileName();
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<String> getColumnClass(int col) {
		return String.class;
	}

	public void removeRows(int[] rows) {
		ArrayList<ExportFileInfo> removeInfo = new ArrayList<ExportFileInfo>();
		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				removeInfo.add(exports.get(rows[i]));
			}
			exports.removeAll(removeInfo);
			fireTableRowsDeleted(0, getRowCount());
		}
	}

	public void addRow(ExportFileInfo export) {
		exports.add(export);
		fireTableRowsInserted(0, getRowCount());
	}

	public void updateRows(List<ExportFileInfo> tempExports) {
		exports = (ArrayList<ExportFileInfo>) tempExports;
		fireTableRowsUpdated(0, getRowCount());
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (3 == col) {
			exports.get(row).setFileName((String) value);
		}
		fireTableCellUpdated(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (3 == col) {
			return true;
		}
		return false;
	}
}
