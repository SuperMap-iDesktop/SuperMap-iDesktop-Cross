package com.supermap.desktop.CtrlAction.Dataset.CollectionDataset;

import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2017/7/19.
 */
public class CollectionDatasetTableModel extends AbstractTableModel {
	/**
	 *
	 */
	public final int COLUMN_CAPTION = 0;
	private final int COLUMN_NAME = 1;
	private final int COLUMN_STATE = 2;

	private String[] title = {CommonProperties.getString("String_Field_Caption"),
			CommonProperties.getString("String_FieldName"),
			CommonProperties.getString("String_State")};
	private ArrayList<DatasetInfo> datasetInfos = new ArrayList<>();

	public CollectionDatasetTableModel() {
		super();
	}

	@Override
	public int getRowCount() {
		return datasetInfos.size();
	}

	@Override
	public int getColumnCount() {
		return title.length;
	}

	public void addRow(DatasetInfo fileInfo) {
		this.datasetInfos.add(fileInfo);
		fireTableRowsInserted(COLUMN_CAPTION, getRowCount());
	}

	public void removeRow(int i) {
		datasetInfos.remove(i);
		fireTableRowsDeleted(COLUMN_CAPTION, getRowCount());
	}

	public void removeRows(int[] rows) {
		ArrayList<DatasetInfo> removeInfo = new ArrayList<>();
		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				removeInfo.add(datasetInfos.get(rows[i]));
			}
			datasetInfos.removeAll(removeInfo);
			fireTableRowsDeleted(COLUMN_CAPTION, getRowCount());
		}
	}

	public void updateRows(List<DatasetInfo> tempFileInfos) {
		this.datasetInfos = (ArrayList<DatasetInfo>) tempFileInfos;
		fireTableRowsUpdated(COLUMN_CAPTION, getRowCount());
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
		if (columnIndex == COLUMN_CAPTION) {
			return true;
		}
		return false;
	}

	// 得到某行的数据
	public DatasetInfo getTagValueAt(int tag) {
		return datasetInfos.get(tag);
	}

	// 得到选中的所有行的数据
	public List<DatasetInfo> getTagValueAt(int[] tag) {
		ArrayList<DatasetInfo> result = new ArrayList<>();
		for (int i = 0; i < tag.length; i++) {
			result.add(datasetInfos.get(i));
		}
		return result;
	}

	// 在表格中填充数据
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DatasetInfo datasetInfo = datasetInfos.get(rowIndex);
		if (columnIndex == COLUMN_CAPTION) {
			return datasetInfo.getCapiton();
		}
		if (columnIndex == COLUMN_NAME) {
			return datasetInfo.getName();
		}
		if (columnIndex == COLUMN_STATE) {
			return datasetInfo.getState();
		}
		return "";
	}

	public ArrayList<DatasetInfo> getDatasetInfos() {
		return datasetInfos;
	}
}
