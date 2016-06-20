package com.supermap.desktop.utilties;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.table.AbstractTableModel;

import java.sql.Time;
import java.util.HashMap;

/**
 * 属性表的TableModel
 *
 * @author XiaJT
 */
public class TabularTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private transient Recordset recordset;
	private transient FieldInfos fieldInfos;
	private transient FieldInfos fieldInfosDataset;
	private int nowRow = 0;
	// private TabularCache tabularCache = new TabularCache();
	//用于存放行值和recordset的ID之间的关系
	private HashMap<Integer, Object> rowIndexMap = new HashMap<Integer, Object>();
	//用于存放行值和ID与行值之间的关系
	private HashMap<Object,Integer> idMap = new HashMap<Object,Integer>();
	public TabularTableModel(Recordset recordset) {
		setRecordset(recordset);
	}

	private void init() {
		if (this.recordset != null) {
			this.recordset.moveFirst();
			nowRow = 0;
			this.fieldInfos = recordset.getFieldInfos();
			this.fieldInfosDataset = recordset.getDataset().getFieldInfos();
		}
	}

	/**
	 * 得到表头名字
	 */
	@Override
	public String getColumnName(int column) {
		if (this.recordset == null) {
			return null;
		}

		return fieldInfos.get(column).getCaption();
	}

	@Override
	public int getRowCount() {
		if (recordset == null || recordset.isClosed()) {
			return 0;
		} else {
			return recordset.getRecordCount();
		}
	}

	@Override
	public int getColumnCount() {
		if (recordset == null || recordset.isClosed()) {
			return 0;
		} else {
			return recordset.getFieldCount();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// return 0;
		if (recordset == null || recordset.isClosed()) {
			return null;
		}
		// 缓存用处效果不明显
		// int key = getKey(rowIndex, columnIndex);
		// Object value = tabularCache.getValue(key);
		// if (null == value) {
		Object value = null;
		moveToRow(rowIndex);
		value = recordset.getFieldValue(columnIndex);
		// tabularCache.updateValue(key, value);
		// }
		return value;
	}

	private int getKey(int rowIndex, int columnIndex) {
		// 暂定最多一百列
		return rowIndex * 100 + columnIndex;
	}

	/**
	 * 从当前位置移动到行
	 *
	 * @param rowIndex
	 */
	private void moveToRow(int rowIndex) {
		if (recordset == null || recordset.isClosed()) {
			return;
		}

		while (rowIndex != nowRow) {
			if (rowIndex > nowRow) {
				nowRow++;
				recordset.moveNext();
			} else {
				nowRow--;
				recordset.movePrev();
			}
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (recordset == null || recordset.getDataset().isReadOnly() || recordset.isClosed()) {
			return false;
		}
		if (fieldInfosDataset.get(getColumnName(column)) == null) {
			return false;
		}
		boolean flag = true;
		if (column == 0 || fieldInfos.get(column).isSystemField()) {
			flag = false;
		}
		return flag;
	}

	public void setRecordset(Recordset recordset) {
		if (this.recordset != null && !this.recordset.isClosed()) {
			this.recordset.dispose();
			this.recordset = null;
		}
		this.recordset = recordset;
		recordset.moveFirst();
		int count = 0;
		while (!recordset.isEOF()) {
			Object value = recordset.getFieldValue(0);
			rowIndexMap.put(count, value);
			idMap.put(value, count);
			recordset.moveNext();
			count++;
		}
		init();
		fireTableStructureChanged();
	}

	public Recordset getRecordset() {
		return this.recordset;
	}

	// public void updateData(int row, int column, Object data) {
	// if (recordset == null) {
	// return;
	// }
	//
	// moveToRow(row);
	// try {
	// // 判断Data是否为空
	// boolean isDataNull = false;
	// if (data == null)
	// isDataNull = true;
	// else if (data instanceof String && ((String) data).length() <= 0) {
	// isDataNull = true;
	// }
	//
	// if ((!isDataNull || recordset.getFieldInfos().get(column).isRequired()) && (recordset.getFieldValue(column) == null
	// || !recordset.getFieldValue(column).equals(data))) {
	//
	// recordset.edit();
	// if (recordset.setFieldValue(column, data)) {
	// recordset.update();
	// }
	// }
	// } catch (Exception e) {
	// deadException(e);
	// // everything will be fine
	// }
	// }

	public HashMap<Integer, Object> getRowIndexMap() {
		return rowIndexMap;
	}

	public HashMap<Object, Integer> getIdMap() {
		return idMap;
	}

	@Override
	public Class getColumnClass(int c) {
		if (recordset == null) {
			return Object.class;
		}

		Class result;
		if (c == 0) {
			result = String.class;
		} else {
			if (recordset.getRecordCount() > 0) {
				if (CoreProperties.getString(CoreProperties.Boolean).equals(FieldTypeUtilities.getFieldTypeName(fieldInfos.get(c).getType()))) {
					result = Boolean.class;
				} else if (CoreProperties.getString(CoreProperties.DateTime).equals(FieldTypeUtilities.getFieldTypeName(fieldInfos.get(c).getType()))) {
					return Time.class;
				} else {
					result = String.class;
				}
			} else {
				result = String.class;
			}
		}
		return result;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (recordset == null) {
			return;
		}

		if (fieldInfos.get(getColumnName(columnIndex)) == null) {
			return;
		}

		moveToRow(rowIndex);
		try {
			// 判断Data是否为空
			boolean isDataNull = false;
			if (aValue == null) {
				isDataNull = true;
			} else if (aValue instanceof String && ((String) aValue).length() <= 0) {
				isDataNull = true;
			}
			if ((!isDataNull || !fieldInfos.get(getColumnName(columnIndex)).isRequired())
					&& (recordset.getFieldValue(getColumnName(columnIndex)) == null || !recordset.getFieldValue(getColumnName(columnIndex)).equals(aValue))) {
				recordset.edit();
				// bool类型先处理

				Object value = aValue;
				if (FieldType.BOOLEAN == fieldInfos.get(getColumnName(columnIndex)).getType()) {
					if (!isDataNull) {
						value = "True".equals(aValue);
					} else {
						value = null;
					}
				}
				if (isDataNull) {
					recordset.setFieldValueNull(getColumnName(columnIndex));
				} else if (FieldType.BYTE == fieldInfos.get(getColumnName(columnIndex)).getType()) {
					recordset.setByte(getColumnName(columnIndex), Short.parseShort(String.valueOf(value)));
				} else {
					recordset.setFieldValue(getColumnName(columnIndex), value);
				}
				// tabularCache.updateValue(getKey(rowIndex, columnIndex), value);
				recordset.update();

			}
		} catch (Exception e) {
			deadException(e);
			// everything will be fine
		}
	}

	public void dispose() {
		if (this.recordset != null) {
			this.recordset.dispose();
			this.recordset = null;
		}
	}

	/**
	 * 数据集排序
	 */
	public Recordset sortRecordset(String sortKind, int... selectedColumns) {
		DatasetVector datasetVector = recordset.getDataset();

		boolean isFirst = true;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < selectedColumns.length; i++) {
			if (!isFirst) {
				buffer.append("#");
			}
			buffer.append(fieldInfos.get(selectedColumns[i]).getName());
			buffer.append(" ");
			buffer.append(sortKind);
			isFirst = false;
		}

		try {
			QueryParameter queryParameter = recordset.getQueryParameter();
			queryParameter.setOrderBy(buffer.toString().split("#"));
			queryParameter.setCursorType(CursorType.DYNAMIC);

			this.setRecordset(datasetVector.query(queryParameter));
		} catch (Exception e) {
			deadException(e);
		}
		return recordset;
	}

	public void deadException(Exception e) {
		// do nothing
	}
}