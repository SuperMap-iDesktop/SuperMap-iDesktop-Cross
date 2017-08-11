package com.supermap.desktop.ui.controls.smTables.models;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.parameters.ParameterPanels.ParameterFieldGroupPanel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.smTables.IModel;
import com.supermap.desktop.ui.controls.smTables.IModelController;
import com.supermap.desktop.ui.controls.smTables.ITable;
import com.supermap.desktop.ui.controls.smTables.ModelControllerAdapter;
import com.supermap.desktop.utilities.ArrayUtilities;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class ModelFieldNameCaptionType extends DefaultTableModel implements IModel {

	String[] columnHeaders = new String[]{
			"",
			ControlsProperties.getString("String_ColumnRampName"),
			CommonProperties.getString("String_Field_Caption"),
			ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")
	};
	private ArrayList<TableData> tableDatas = new ArrayList<>();

	public static final int COLUMN_INDEX_IS_SELECTED = 0;
	public static final int COLUMN_INDEX_NAME = 1;
	public static final int COLUMN_INDEX_CAPTION = 2;
	public static final int COLUMN_INDEX_TYPE = 3;
	private Dataset dataset;
	private FieldType[] fieldTypes;
	private Boolean isShowSystemField;

	public ModelFieldNameCaptionType(FieldType[] fieldTypes, Boolean isShowSystemField) {
		this.fieldTypes = fieldTypes;
		this.isShowSystemField = isShowSystemField;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;
	}

	@Override
	public int getRowCount() {
		return tableDatas == null ? 0 : tableDatas.size();
	}

	@Override
	public int getColumnCount() {
		return columnHeaders == null ? 0 : columnHeaders.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnHeaders[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case COLUMN_INDEX_IS_SELECTED:
				return Boolean.class;
			case COLUMN_INDEX_NAME:
			case COLUMN_INDEX_CAPTION:
				return String.class;
			case COLUMN_INDEX_TYPE:
				return FieldType.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
			case 0:
				return tableDatas.get(row).isSelected;
			case 1:
				return tableDatas.get(row).fieldInfo.getName();
			case 2:
				return tableDatas.get(row).fieldInfo.getCaption();
			case 3:
				return tableDatas.get(row).fieldInfo.getType();
		}
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 0) {
			this.tableDatas.get(row).isSelected = (boolean) aValue;
			fireTableCellUpdated(row, column);
		}
	}

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(DatasetVector dataset) {
		this.dataset = dataset;
		this.tableDatas.clear();
		if (dataset != null) {
			FieldInfos fieldInfos = dataset.getFieldInfos();
			int count = fieldInfos.getCount();
			for (int i = 0; i < count; i++) {
				if (this.fieldTypes == null || ArrayUtilities.isArrayContains(this.fieldTypes, fieldInfos.get(i).getType())) {
					if (!fieldInfos.get(i).isSystemField() || this.isShowSystemField) {
						this.tableDatas.add(new TableData(fieldInfos.get(i)));
					}
				}
			}
		}
		fireTableDataChanged();
	}

	public FieldInfo[] getSelectedFields() {
		ArrayList<FieldInfo> fieldInfoArrayList = new ArrayList<>();
		for (TableData tableData : tableDatas) {
			if (tableData.isSelected) {
				fieldInfoArrayList.add(tableData.fieldInfo);
			}
		}
		return fieldInfoArrayList.toArray(new FieldInfo[fieldInfoArrayList.size()]);
	}

	private IModelController modelController = new ModelControllerAdapter() {
		@Override
		public void selectedAll() {
			for (int i = 0; i < getRowCount(); i++) {
				tableDatas.get(i).isSelected = true;
				fireTableCellUpdated(i, COLUMN_INDEX_IS_SELECTED);
			}
		}

		@Override
		public void selectedIInverse() {
			for (int i = 0; i < getRowCount(); i++) {
				tableDatas.get(i).isSelected = !tableDatas.get(i).isSelected;
				fireTableCellUpdated(i, COLUMN_INDEX_IS_SELECTED);
			}
		}

		@Override
		public void delete(int row) {
			tableDatas.remove(row);
			fireTableRowsDeleted(row, row);
		}

		@Override
		public void selectedSystemField() {
			for (int i = 0; i < tableDatas.size(); i++) {
				if (tableDatas.get(i).fieldInfo.isSystemField()) {
					tableDatas.get(i).isSelected=true;
				}else{
					tableDatas.get(i).isSelected=false;
				}
				fireTableCellUpdated(i, COLUMN_INDEX_IS_SELECTED);
			}
		}

		@Override
		public void selectedNonSystemField() {
			for (int i = 0; i < tableDatas.size(); i++) {
				if (tableDatas.get(i).fieldInfo.isSystemField()) {
					tableDatas.get(i).isSelected=false;
				}else{
					tableDatas.get(i).isSelected=true;
				}
				fireTableCellUpdated(i, COLUMN_INDEX_IS_SELECTED);
			}
		}
	};

	@Override
	public IModelController getModelController() {
		return this.modelController;
	}

	private class TableData {
		boolean isSelected;
		FieldInfo fieldInfo;

		TableData(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
		}
	}
}
