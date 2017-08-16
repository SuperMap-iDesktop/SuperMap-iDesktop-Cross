package com.supermap.desktop.ui.controls.smTables.models;

import com.supermap.analyst.spatialanalyst.StatisticsField;
import com.supermap.analyst.spatialanalyst.StatisticsFieldType;
import com.supermap.analyst.spatialanalyst.StatisticsType;
import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.smTables.IModel;
import com.supermap.desktop.ui.controls.smTables.IModelController;
import com.supermap.desktop.ui.controls.smTables.ModelControllerAdapter;
import com.supermap.desktop.utilities.ArrayUtilities;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/15.
 */
public class ModelRarefyPoints extends DefaultTableModel implements IModel {
	String[] columnHeaders = new String[]{
			"",
			ControlsProperties.getString("String_OriginField"),
			ControlsProperties.getString("String_StatisticsField"),
			ControlsProperties.getString("String_FieldInfoType"),
			ControlsProperties.getString("String_StatisticsType"),
	};
	private ArrayList<TableData> tableDatas = new ArrayList<>();

	public static final int COLUMN_FIELD_IS_SELECTED = 0;
	public static final int COLUMN_FIELD_NAME = 1;
	public static final int COLUMN_STATISTICS_FIELD_NAME = 2;
	public static final int COLUMN_FIELD_TYPE = 3;
	public static final int COLUMN_FIELD_STATISTICS_TYPE = 4;
	private Dataset dataset;

	public ModelRarefyPoints() {
		// do nothing
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return (column == COLUMN_FIELD_IS_SELECTED || column == COLUMN_FIELD_STATISTICS_TYPE || column == COLUMN_STATISTICS_FIELD_NAME);
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
			case COLUMN_FIELD_IS_SELECTED:
				return Boolean.class;
			case COLUMN_FIELD_NAME:
				return String.class;
			case COLUMN_STATISTICS_FIELD_NAME:
				return String.class;
			case COLUMN_FIELD_TYPE:
				return FieldType.class;
			case COLUMN_FIELD_STATISTICS_TYPE:
				return StatisticsType.class;
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
				return tableDatas.get(row).statisticsFieldName;
			case 3:
				return tableDatas.get(row).fieldInfo.getType();
			case 4:
				return tableDatas.get(row).statisticsType;
		}
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == COLUMN_FIELD_IS_SELECTED) {
			tableDatas.get(row).isSelected = (boolean) aValue;
			fireTableCellUpdated(row, column);
		}
		if (column == COLUMN_STATISTICS_FIELD_NAME) {
			if (!((String) aValue).isEmpty()) {
				tableDatas.get(row).statisticsFieldName = (String) aValue;
				fireTableCellUpdated(row, column);
			}
		}
		if (column == COLUMN_FIELD_STATISTICS_TYPE) {
			tableDatas.get(row).statisticsType = (StatisticsFieldType) aValue;
			fireTableCellUpdated(row, column);
			String newName= tableDatas.get(row).fieldInfo.getName()+getNewStatisticsFieldName((StatisticsFieldType) aValue);
			if (!newName.equals(tableDatas.get(row).statisticsFieldName)){
				tableDatas.get(row).statisticsFieldName = newName;
				fireTableCellUpdated(row, column-2);
			}
		}
	}

	private String getNewStatisticsFieldName(StatisticsFieldType statisticsFieldType){
		String result="_";
		if (statisticsFieldType==StatisticsFieldType.AVERAGE){
			result=result+"Average";
		}else if (statisticsFieldType==StatisticsFieldType.MAXVALUE){
			result=result+"MaxValue";
		}else if (statisticsFieldType==StatisticsFieldType.MINVALUE){
			result=result+"MinValue";
		}else if (statisticsFieldType==StatisticsFieldType.SAMPLESTDDEV){
			result=result+"SampleStdDev";
		}else if (statisticsFieldType==StatisticsFieldType.SAMPLEVARIANCE){
			result=result+"SampleVariance";
		}else if (statisticsFieldType==StatisticsFieldType.STDDEVIATION){
			result=result+"StdDeviation";
		}else if (statisticsFieldType==StatisticsFieldType.SUM){
			result=result+"Sum";
		}else if (statisticsFieldType==StatisticsFieldType.VARIANCE){
			result=result+"Variance";
		}
		return result;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(DatasetVector dataset) {
		this.dataset = dataset;
		tableDatas.clear();
		if (dataset != null) {
			FieldInfos fieldInfos = dataset.getFieldInfos();
			int count = fieldInfos.getCount();
			FieldType[] fieldType = new FieldType[]{FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.DOUBLE, FieldType.SINGLE};

			for (int i = 0; i < count; i++) {
				if (ArrayUtilities.isArrayContains(fieldType, fieldInfos.get(i).getType())) {
					if (!fieldInfos.get(i).isSystemField()) {
						ArrayList<StatisticsFieldType> supportedStatisticsType = getSupportedStatisticsType();
						tableDatas.add(new TableData(fieldInfos.get(i), supportedStatisticsType, supportedStatisticsType.get(0),fieldInfos.get(i).getName()+"_Average"));
					}
				}
			}
		}
		fireTableDataChanged();
	}

	public StatisticsField[] getSelectedStatisticsField() {
		ArrayList<StatisticsField> statisticsFields = new ArrayList<>();
		for (TableData tableData : tableDatas) {
			if (tableData.isSelected) {
				StatisticsField statisticsField=new StatisticsField();
				statisticsField.setSourceField(tableData.fieldInfo.getName());
				statisticsField.setResultField(tableData.statisticsFieldName);
				statisticsField.setMode(tableData.statisticsType);
				statisticsFields.add(statisticsField);
			}
		}
		return statisticsFields.toArray(new StatisticsField[statisticsFields.size()]);
	}

	public ArrayList<StatisticsFieldType> getCurrentSelectedStatisticsType(int row) {
		ArrayList<StatisticsFieldType> statisticsTypes = new ArrayList<>();
		statisticsTypes = tableDatas.get(row).supportedStatisticsType;
		return statisticsTypes;
	}

	private ArrayList<StatisticsFieldType> getSupportedStatisticsType() {
		ArrayList<StatisticsFieldType> statisticsTypes = new ArrayList<>();
		statisticsTypes.add(StatisticsFieldType.AVERAGE);
		statisticsTypes.add(StatisticsFieldType.MAXVALUE);
		statisticsTypes.add(StatisticsFieldType.MINVALUE);
		statisticsTypes.add(StatisticsFieldType.SAMPLESTDDEV);
		statisticsTypes.add(StatisticsFieldType.SAMPLEVARIANCE);
		statisticsTypes.add(StatisticsFieldType.STDDEVIATION);
		statisticsTypes.add(StatisticsFieldType.SUM);
		statisticsTypes.add(StatisticsFieldType.VARIANCE);
		return statisticsTypes;
	}

	private IModelController modelController = new ModelControllerAdapter() {
		@Override
		public void selectAllOrNull(boolean value) {
			for (int i = 0; i < getRowCount(); i++) {
				setValueAt(value, i, 0);
			}
		}
	};

	@Override
	public IModelController getModelController() {
		return this.modelController;
	}

	class TableData {
		boolean isSelected=true;
		FieldInfo fieldInfo;
		StatisticsFieldType statisticsType;
		ArrayList<StatisticsFieldType> supportedStatisticsType;
		String statisticsFieldName;

		TableData(FieldInfo fieldInfo, ArrayList<StatisticsFieldType> supportedStatisticsType, StatisticsFieldType statisticsType, String statisticsFieldName) {
			this.fieldInfo = fieldInfo;
			this.supportedStatisticsType = supportedStatisticsType;
			this.statisticsType = statisticsType;
			this.statisticsFieldName = statisticsFieldName;
		}
	}
}
