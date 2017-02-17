package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SortTable.SortTable;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 字段信息
 *
 * @author xiajt
 */
public class FieldInfoTable extends SortTable {


	private Dataset dataset;
	private FieldInfoTableModel fieldInfoTableModel;


	public FieldInfoTable() {
		super();
		fieldInfoTableModel = new FieldInfoTableModel();
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
//		this.getColumnModel().getColumn(0).setPreferredWidth(300);
//		this.getColumnModel().getColumn(1).setPreferredWidth(50);
	}

	public Object[] getAllValue() {
		int selectedRow = this.getSelectedRow();
		if (selectedRow == -1 || selectedRow == 0) {
			return null;
		} else {
			return ((FieldInfoTableModel) this.getModel()).getAllValue(selectedRow);
		}
	}

	public void setJoinItem(JoinItems joinItems) {
		fieldInfoTableModel.setJoinItems(joinItems);
	}

	public String getSqlValueAt(int row, int column) {
		return fieldInfoTableModel.getSqlValueAt(row, column);

	}

	/**
	 * tableModel类
	 */
	private class FieldInfoTableModel extends SortableTableModel {
		private DatasetVector dataset = null;
		private String[] columnNames;
		private JoinItems joinItems;
		private int rowCount;

		public FieldInfoTableModel() {
			super();
			columnNames = new String[]{CommonProperties.getString(CommonProperties.Caption), DataViewProperties.getString("String_SQLQueryColumnFieldName"), DataViewProperties.getString("String_SQLQueryColumnFieldType")};
		}

		public void setDataset(Dataset dataset) {
			if (dataset instanceof DatasetVector) {
				this.dataset = (DatasetVector) dataset;
			} else {
				this.dataset = null;
			}
			initRowCount();
			fireTableDataChanged();
		}

		private void initRowCount() {
			indexes = null;
			int count = 0;
			if (this.dataset != null) {
				// 全部字段 所以+1
				count = this.dataset.getFieldCount() + 1;
				if (joinItems != null && joinItems.getCount() > 0) {
					for (int i = 0; i < joinItems.getCount(); i++) {
						DatasetVector dataset = (DatasetVector) DatasourceUtilities.getDataset(joinItems.get(i).getForeignTable(), this.dataset.getDatasource());
						if (dataset != null) {
							FieldInfos fieldInfos = dataset.getFieldInfos();
							count += fieldInfos.getCount();
						}
					}
				}
			}
			this.rowCount = count;
		}

		@Override
		public int getRowCount() {
			return rowCount;
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
			row = getIndexRow(row)[0];
			if (row == 0) {
				if (column == 0) {
					return "*";
				} else if (column == 1) {
//					return formatString(dataset.getName(), "*");
					return "*";
				} else {
					return "All";
				}
			}
			//region 设置关联字段
//			else if (row == getRowCount() - 1) {
//				if (column == 0) {
//					return DataViewProperties.getString("String_SQLQueryRelated");
//				} else {
//					return "";
//				}
//			}
			//endregion
			else {
				Object result = null;

				FieldInfos datasetFieldInfos = dataset.getFieldInfos();
				if (row <= datasetFieldInfos.getCount()) {
					if (column == 0) {
						result = datasetFieldInfos.get(row - 1).getCaption();
					} else if (column == 1) {
//						result = formatString(this.dataset.getName(), datasetFieldInfos.get(row - 1).getName());
						result = datasetFieldInfos.get(row - 1).getName();
					} else {
						result = FieldTypeUtilities.getFieldTypeName(datasetFieldInfos.get(row - 1).getType());
					}
				} else {
					row -= datasetFieldInfos.getCount();
					for (int i = 0; i < joinItems.getCount(); i++) {
						DatasetVector datasetVector = (DatasetVector) DatasourceUtilities.getDataset(joinItems.get(i).getForeignTable(), dataset.getDatasource());
						if (datasetVector == null) {
							continue;
						}
						FieldInfos fieldInfos = datasetVector.getFieldInfos();
						if (row <= fieldInfos.getCount()) {
							if (column == 0) {
								result = fieldInfos.get(row - 1).getCaption();
							} else if (column == 1) {
								result = formatString(datasetVector.getName(), fieldInfos.get(row - 1).getName());
							} else {
								result = FieldTypeUtilities.getFieldTypeName(fieldInfos.get(row - 1).getType());
							}
							break;
						}
						row -= fieldInfos.getCount();
					}
				}
				return result;
			}
		}

		private String formatString(String a, String b) {
			return MessageFormat.format("{0}.{1}", a, b);
		}

		public Object[] getAllValue(int row) {
			row = getIndexRow(row)[0];
			if (row == -1 || row == 0 || dataset == null) {
				return null;
			} else {
				String[] strings = getSqlValueAt(row, 1).split("\\.");
				String datasetName = strings[0];
				String fieldName = strings[1];

				LinkedHashMap<Object, String> map = new LinkedHashMap<>();
				DatasetVector tempDataset = dataset;
				if (!dataset.getName().equals(datasetName)) {
					tempDataset = ((DatasetVector) dataset.getDatasource().getDatasets().get(datasetName));
				}
				// 得到字段类型
				FieldType fieldType = tempDataset.getFieldInfos().get(fieldName).getType();
				//  2017/2/10 lixiaoyao 针对原来的唯一值获取查询进行优化，原本是遍历获取，现在改为SQL查询
				String queryString=datasetName+'.'+fieldName;
				QueryParameter queryParameter = new QueryParameter();
				queryParameter.setCursorType(CursorType.STATIC);
				queryParameter.setHasGeometry(false);
				queryParameter.setOrderBy(new String[]{queryString + " asc",});
				queryParameter.setGroupBy(new String[]{queryString});
				queryParameter.setResultFields(new String[]{queryString});
				Recordset recordset = tempDataset.query(queryParameter);
				//Recordset recordset = tempDataset.getRecordset(false, CursorType.STATIC);
				try {
					recordset.moveFirst();
					for (; !recordset.isEOF(); recordset.moveNext()) {
						Object result = formatData(recordset.getFieldValue(fieldName), fieldType);
						if (result != null) {
							map.put(result, "");
						}
					}
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(e);
				} finally {
					recordset.dispose();
				}
				Object[] result = new Object[map.size()];
				Iterator<Object> iterator = map.keySet().iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					result[i] = iterator.next();
				}
				//SortUIUtilities.sortList(result);
				return result;
			}
		}


		private Object formatData(Object fieldValue, FieldType fieldType) {
			if (fieldValue == null) {
				return null;
			}
			if (fieldType == FieldType.DATETIME) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd hh:mm:ss");
				return dateFormat.format(fieldValue);
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
				return fieldValue;
			}
		}

		public void setJoinItems(JoinItems joinItems) {
			this.joinItems = joinItems;
			indexes = null;
			initRowCount();
			fireTableDataChanged();
		}

		public String getSqlValueAt(int row, int column) {
			String s = String.valueOf(this.getValueAt(row, column));
			if (s.indexOf('.') == -1) {
				s = formatString(dataset.getName(), s);
			}
			return s;
		}
	}
}
