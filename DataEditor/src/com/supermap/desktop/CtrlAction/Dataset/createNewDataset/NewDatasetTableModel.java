package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.CharsetUtilties;
import com.supermap.desktop.utilties.EncodeTypeUtilties;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class NewDatasetTableModel extends DefaultTableModel {

	private final String[] columnNames = new String[]{
			CommonProperties.getString("String_ColumnHeader_Index"),
			CommonProperties.getString("String_ColumnHeader_TargetDatasource"),
			DataEditorProperties.getString("String_CreateType"),
			DataEditorProperties.getString("String_ColumnTitle_DtName"),
			CommonProperties.getString("String_ColumnHeader_EncodeType"),
			DataEditorProperties.getString("String_Charset"),
			DataEditorProperties.getString("String_DataGridViewComboBoxColumn_Name")
	};
	private final boolean[] isColumnEditable = new boolean[]{
			false, true, true, true, true, true, true
	};

	public static final int COLUMN_INDEX_INDEX = 0;
	public static final int COLUMN_INDEX_TARGET_DATASOURCE = 1;
	public static final int COLUMN_INDEX_DatasetType = 2;
	public static final int COLUMN_INDEX_DatasetName = 3;
	public static final int COLUMN_INDEX_EncodeType = 4;
	public static final int COLUMN_INDEX_Charset = 5;
	public static final int COLUMN_INDEX_WindowMode = 6;

	private ArrayList<NewDatasetBean> datasetBeans = new ArrayList<>();

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == COLUMN_INDEX_INDEX) {
			return String.class;
		} else if (columnIndex == COLUMN_INDEX_TARGET_DATASOURCE) {
			return Datasource.class;
		} else if (columnIndex == COLUMN_INDEX_DatasetType) {
			return DatasetType.class;
		} else if (columnIndex == COLUMN_INDEX_DatasetName) {
			return String.class;
		} else if (columnIndex == COLUMN_INDEX_EncodeType) {
			return String.class;
		} else if (columnIndex == COLUMN_INDEX_Charset) {
			return String.class;
		} else if (columnIndex == COLUMN_INDEX_WindowMode) {
			return String.class;
		}
		return String.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == COLUMN_INDEX_INDEX) {
			return row + 1;
		} else if (column == COLUMN_INDEX_TARGET_DATASOURCE) {
			return datasetBeans.get(row).getDatasource();
		} else if (column == COLUMN_INDEX_DatasetType) {
			return datasetBeans.get(row).getDatasetType();
		} else if (column == COLUMN_INDEX_DatasetName) {
			return datasetBeans.get(row).getDatasetName();
		} else if (column == COLUMN_INDEX_EncodeType) {
			return EncodeTypeUtilties.toString(datasetBeans.get(row).getEncodeType());
		} else if (column == COLUMN_INDEX_Charset) {
			return CharsetUtilties.toString(datasetBeans.get(row).getCharset());
		} else if (column == COLUMN_INDEX_WindowMode) {
			return datasetBeans.get(row).getAddToWindowMode();
		}
		throw new UnsupportedOperationException("column out of index");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return isColumnEditable[column];
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == COLUMN_INDEX_INDEX) {
			throw new UnsupportedOperationException("index can't change");
		} else if (column == COLUMN_INDEX_TARGET_DATASOURCE) {
			datasetBeans.get(row).setDatasource((Datasource) aValue);
		} else if (column == COLUMN_INDEX_DatasetType) {
			datasetBeans.get(row).setDatasetType((DatasetType) aValue);
		} else if (column == COLUMN_INDEX_DatasetName) {
			setDatasetName(row, (String) aValue);
		} else if (column == COLUMN_INDEX_EncodeType) {
			datasetBeans.get(row).setEncodeType((EncodeType) aValue);
		} else if (column == COLUMN_INDEX_Charset) {
			datasetBeans.get(row).setCharset((Charset) aValue);
		} else if (column == COLUMN_INDEX_WindowMode) {
			datasetBeans.get(row).setAddToWindowMode((AddToWindowMode) aValue);
		}
	}

	private void setDatasetName(int row, String aValue) {

	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return datasetBeans.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public void removeRow(int row) {
		this.datasetBeans.remove(row);
		fireTableRowsDeleted(row, row);
	}

	public void addEmptyRow() {
		NewDatasetBean newDatasetBean = new NewDatasetBean();
		if (datasetBeans.size() > 0) {
			newDatasetBean.setDatasetType(datasetBeans.get(datasetBeans.size() - 1).getDatasetType());
		}
		datasetBeans.add(newDatasetBean);
	}
}
