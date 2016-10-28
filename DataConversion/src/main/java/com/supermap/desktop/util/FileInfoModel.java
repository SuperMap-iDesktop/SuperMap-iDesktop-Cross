package com.supermap.desktop.util;

import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportFileInfo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 数据导入中用到的JTable模型
 */
public class FileInfoModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] title = {
			DataConversionProperties.getString("string_tabletitle_data"),
			DataConversionProperties.getString("string_tabletitle_filetype"),
			DataConversionProperties.getString("string_tabletitle_state") };
	private ArrayList<ImportFileInfo> fileInfos;

	public FileInfoModel(List<ImportFileInfo> fileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
	}

	public FileInfoModel() {
		super();
	}

	@Override
	public int getRowCount() {
		return fileInfos.size();
	}

	@Override
	public int getColumnCount() {
		return title.length;
	}

	public void addRow(ImportFileInfo fileInfo) {
		this.fileInfos.add(fileInfo);
		fireTableRowsInserted(0, getRowCount());
	}

	public void removeRow(int i){
		fileInfos.remove(i);
		fireTableRowsDeleted(0, getRowCount());
	}
	public void removeRows(int[] rows) {
		ArrayList<ImportFileInfo> removeInfo = new ArrayList<ImportFileInfo>();
		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				removeInfo.add(fileInfos.get(rows[i]));
			}
			fileInfos.removeAll(removeInfo);
			fireTableRowsDeleted(0, getRowCount());
		}
	}

	public void updateRows(List<ImportFileInfo> tempFileInfos) {
		this.fileInfos = (ArrayList<ImportFileInfo>) tempFileInfos;
		fireTableRowsUpdated(0, getRowCount());
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
		if (columnIndex==1) {
			return true;
		}
		return false;
	}

	// 得到某行的数据
	public ImportFileInfo getTagValueAt(int tag) {
		return fileInfos.get(tag);
	}

	// 得到选中的所有行的数据
	public List<ImportFileInfo> getTagValueAt(int[] tag) {
		ArrayList<ImportFileInfo> result = new ArrayList<ImportFileInfo>();
		for (int i = 0; i < tag.length; i++) {
			result.add(fileInfos.get(i));
		}
		return result;
	}

	// 在表格中填充数据
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ImportFileInfo importFileInfo = fileInfos.get(rowIndex);
		if (0 == columnIndex) {
			return importFileInfo.getFileName();
		}
		if (1 == columnIndex) {
			return importFileInfo.getFileType();
		}
		if (2 == columnIndex) {
			return importFileInfo.getState();
		}
		return "";
	}

}
