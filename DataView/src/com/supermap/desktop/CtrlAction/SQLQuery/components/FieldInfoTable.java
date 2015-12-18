package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.FieldTypeUtilties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 字段信息
 *
 * @author xiajt
 */
public class FieldInfoTable extends JTable {


	private Dataset dataset = null;
	private FieldInfoTableModel fieldInfoTableModel = new FieldInfoTableModel();


	public FieldInfoTable() {
		super();
		this.setModel(fieldInfoTableModel);
		initTable();
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		int columnWidth0 = this.getColumnModel().getColumn(0).getWidth();
		int columnWidth1 = this.getColumnModel().getColumn(1).getWidth();
		int columnWidth2 = this.getColumnModel().getColumn(2).getWidth();
		((FieldInfoTableModel) this.getModel()).setDataset(this.dataset);
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth0);
		this.getColumnModel().getColumn(1).setPreferredWidth(columnWidth1);
		this.getColumnModel().getColumn(2).setPreferredWidth(columnWidth2);
		if (this.getRowCount() > 0) {
			this.setRowSelectionInterval(0, 0);
		}
	}


	private void initTable() {
		this.setRowHeight(23);
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.getColumnModel().getColumn(0).setPreferredWidth(300);
		this.getColumnModel().getColumn(1).setPreferredWidth(50);
	}

	public String[] getAllValue() {
		int selectedRow = this.getSelectedRow();
		if (selectedRow == -1 || selectedRow == 0 || selectedRow == this.getRowCount()) {
			return null;
		} else {
			return ((FieldInfoTableModel) this.getModel()).getAllValue(selectedRow);
		}
	}

	/**
	 * tablemodel类
	 */
	private class FieldInfoTableModel extends DefaultTableModel {
		private Dataset dataset = null;
		private String[] columnNames;
		private Recordset recordset = null;

		public FieldInfoTableModel() {
			super();
			columnNames = new String[]{CommonProperties.getString(CommonProperties.Caption), DataViewProperties.getString("String_SQLQueryColumnFieldName"), DataViewProperties.getString("String_SQLQueryColumnFieldType")};
		}

		public void setDataset(Dataset dataset) {
			this.dataset = dataset;
			// TODO 初始化表格数据
			if (this.recordset != null) {
				this.recordset.dispose();
				this.recordset = null;
			}
			if (dataset instanceof DatasetVector) {
				this.recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.DYNAMIC);
			}
			fireTableStructureChanged();
		}

		@Override
		public int getRowCount() {
			int count = 0;
			if (this.recordset != null) {
				// TODO 设置管理字段需要再加1
				count = this.recordset.getFieldCount() + 1;
			}
			return count;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row == 0) {
				if(column == 0){
					return "*";
				} else if (column == 1) {
					return formatString(dataset.getName(), "*");
				} else {
					return "All";
				}
			}
			//region 设置关联字段
			// TODO 设置关联字段未实现
			/*else if (row == getRowCount() - 1) {
				if (column == 0) {
					return DataViewProperties.getString("String_SQLQueryRelated");
				} else {
					return "";
				}
			} */
			//endregion
			else {
				if(column == 0) {
					return recordset.getFieldInfos().get(row - 1).getCaption();
				}else if (column == 1) {
					return formatString(dataset.getName(), recordset.getFieldInfos().get(row - 1).getName());
				} else {
					return FieldTypeUtilties.getFieldTypeName(recordset.getFieldInfos().get(row - 1).getType());
				}
			}
		}

		private String formatString(String a, String b) {
			return MessageFormat.format("{0}.{1}", a, b);
		}

		public String[] getAllValue(int row) {
			if (row == -1 || row == 0 || row == getRowCount() - 1) {
				return null;
			} else {
				// TODO 外接表需处理
				LinkedHashMap map = new LinkedHashMap();
				FieldType fieldType = this.recordset.getFieldInfos().get(row - 1).getType();
				this.recordset.moveFirst();
				for (; !recordset.isEOF(); recordset.moveNext()) {
					String result = formatData(recordset.getFieldValue(row - 1), fieldType);
					if (result != null) {
						map.put(result, 0);
					}
				}
				String[] result = new String[map.size()];
				Iterator iterator = map.keySet().iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					result[i] = (String) iterator.next();
				}
				return result;
			}
		}


		private String formatData(Object fieldValue, FieldType fieldType) {
			if (fieldValue == null) {
				return null;
			}
			if (fieldType == FieldType.DATETIME) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return fieldValue == null ? null : dateFormat.format(fieldValue);
			} else if (fieldType == FieldType.BOOLEAN) {
				if (fieldValue.equals(true)) {
					return "True";
				} else if (fieldValue.equals(false)) {
					return "False";
				} else {
					return null;
				}
			} else if (fieldType == FieldType.LONGBINARY) {
				return "BinaryData";
			} else {
				return String.valueOf(fieldValue);
			}
		}
	}
}
