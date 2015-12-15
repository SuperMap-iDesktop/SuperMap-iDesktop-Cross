package com.supermap.desktop.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;

public class ChildExportModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] title = {
			DataConversionProperties.getString("string_tabletitle_dataset"),
			DataConversionProperties.getString("string_tabletitle_datasource"),
			DataConversionProperties.getString("string_tabletitle_type") };
	ArrayList<ExportFileInfo> exports;

	public ChildExportModel(List<ExportFileInfo> exports) {
		super();
		this.exports = (ArrayList<ExportFileInfo>) exports;
	}

	public ChildExportModel() {
		super();
	}

	// 得到某行的数据
	public ExportFileInfo getTagValueAt(int tag) {
		return exports.get(tag);
	}

	@Override
	public int getRowCount() {
		return exports.size();
	}

	@Override
	public int getColumnCount() {
		return title.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return title[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ExportFileInfo tempExportFileInfo = exports.get(rowIndex);
		if (0 == columnIndex) {
			return tempExportFileInfo.getDatasetCell();
		}
		if (1 == columnIndex) {
			return tempExportFileInfo.getDatasource().getAlias();
		}
		if (2 == columnIndex) {
			return tempExportFileInfo.getDataType();
		}
		return "";
	}

}
