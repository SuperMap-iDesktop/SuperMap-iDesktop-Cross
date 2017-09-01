package com.supermap.desktop.ui.controls.CollectionDataset;

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
	private int collectionType;

	public final int VECTOR_COLLECTION_TYPE = 0;
	public final int IMAGE_COLLECTION_TYPE = 1;

	public final int COLUMN_IMAGE_CAPTION = 0;
	public final int COLUMN_IMAGE_NAME = 1;
	public final int COLUMN_IMAGE_STATE = 2;

	public final int COLUMN_VECTOR_NAME = 0;
	public final int COLUMN_VECTOR_STATE = 1;

	private String[] title = null;
	private ArrayList<DatasetInfo> datasetInfos = new ArrayList<>();

	public CollectionDatasetTableModel(int collectionType) {
		super();
		title = collectionType == IMAGE_COLLECTION_TYPE ?
				new String[]{CommonProperties.getString("String_Field_Caption"),
						CommonProperties.getString("String_FieldName"),
						CommonProperties.getString("String_State")} :
				new String[]{CommonProperties.getString("String_FieldName"),
						CommonProperties.getString("String_State")};
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
		fireTableRowsInserted(0, getRowCount());
	}

	public void removeRow(int i) {
		datasetInfos.remove(i);
		fireTableRowsDeleted(0, getRowCount());
	}

	public void removeRows(int[] rows) {
		ArrayList<DatasetInfo> removeInfo = new ArrayList<>();
		if (rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				removeInfo.add(datasetInfos.get(rows[i]));
			}
			datasetInfos.removeAll(removeInfo);
			fireTableRowsDeleted(0, getRowCount());
		}
	}

	public void updateRows(List<DatasetInfo> tempFileInfos) {
		this.datasetInfos = (ArrayList<DatasetInfo>) tempFileInfos;
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
		if (collectionType == IMAGE_COLLECTION_TYPE && columnIndex == COLUMN_IMAGE_CAPTION) {
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
			result.add(datasetInfos.get(tag[i]));
		}
		return result;
	}

	// 在表格中填充数据
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DatasetInfo datasetInfo = datasetInfos.get(rowIndex);
		if (collectionType == IMAGE_COLLECTION_TYPE) {
			if (columnIndex == COLUMN_IMAGE_CAPTION) {
				return datasetInfo.getCapiton();
			}
			if (columnIndex == COLUMN_IMAGE_NAME) {
				return datasetInfo.getName();
			}
			if (columnIndex == COLUMN_IMAGE_STATE) {
				return datasetInfo.getState();
			}
		} else {
			if (columnIndex == COLUMN_VECTOR_NAME) {
				return datasetInfo.getName();
			}
			if (columnIndex == COLUMN_VECTOR_STATE) {
				return datasetInfo.getState();
			}
		}
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		super.setValueAt(aValue, rowIndex, columnIndex);
		if (collectionType == IMAGE_COLLECTION_TYPE) {
			if (columnIndex == COLUMN_IMAGE_CAPTION) {
				datasetInfos.get(rowIndex).setCapiton((String) aValue);
			}
			if (columnIndex == COLUMN_IMAGE_NAME) {
				datasetInfos.get(rowIndex).setName((String) aValue);
			}
			if (columnIndex == COLUMN_IMAGE_STATE) {
				datasetInfos.get(rowIndex).setState((String) aValue);
			}
		} else {
			if (columnIndex == COLUMN_VECTOR_NAME) {
				datasetInfos.get(rowIndex).setName((String) aValue);
			}
			if (columnIndex == COLUMN_VECTOR_STATE) {
				datasetInfos.get(rowIndex).setState((String) aValue);
			}
		}
	}

	public ArrayList<DatasetInfo> getDatasetInfos() {
		return datasetInfos;
	}
}
