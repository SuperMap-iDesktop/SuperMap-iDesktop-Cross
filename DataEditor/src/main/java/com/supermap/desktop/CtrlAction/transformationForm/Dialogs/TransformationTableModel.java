package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.TransformationResampleMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationTableModel extends DefaultTableModel {

	private List<TransformationAddObjectBean> datas = new ArrayList<>();
	private String[] columnNames = new String[]{
			"",
			CommonProperties.getString("String_ColumnHeader_SourceDataset"),
			CommonProperties.getString("String_ColumnHeader_SourceDatasource"),
			DataEditorProperties.getString("String_Transformation_ColumnNeedResave"),
			CommonProperties.getString("String_Label_ResultDatasource"),
			CommonProperties.getString("String_Label_ResultDataset"),
	};
	public static final int COLUMN_ENABLE = 0;
	public static final int COLUMN_DATASET = 1;
	public static final int COLUMN_DATA_SOURCE = 2;
	public static final int COLUMN_SAVE_AS = 3;
	public static final int column_ResultDatasource = 4;
	public static final int column_ResultDataset = 5;

	@Override
	public int getRowCount() {
		if (datas == null) {
			return 0;
		}
		return datas.size();
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
		if (column == COLUMN_ENABLE) {
			return true;
		}
		if (column == COLUMN_DATASET || column == COLUMN_DATA_SOURCE) {
			return false;
		}
		if (column == COLUMN_SAVE_AS) {
			if (datas.get(row).getDataset().getDatasource().isReadOnly()) {
				Application.getActiveApplication().getOutput().output(
						MessageFormat.format(DataEditorProperties.getString("String_Transformation_DatasetReadonly"), datas.get(row).getDataset().getName()));
				return false;
			}
			return true;
		}
		if (column == column_ResultDataset || column == column_ResultDatasource) {
			return datas.get(row).isSaveAs();
		}
		return super.isCellEditable(row, column);
	}

	@Override
	public Object getValueAt(int row, int column) {

		TransformationAddObjectBean data = datas.get(row);
		switch (column) {
			case COLUMN_ENABLE:
				return data.isEnable();
			case COLUMN_DATASET:
				return data.getDataset();
			case COLUMN_DATA_SOURCE:
				return data.getDataset().getDatasource();
			case COLUMN_SAVE_AS:
				return data.isSaveAs();
			case column_ResultDatasource:
				return data.getResultDatasource();
			case column_ResultDataset:
				return data.getResultDatasetName();
		}
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == COLUMN_ENABLE) {
			datas.get(row).setEnable((Boolean) aValue);
		} else if (column == COLUMN_SAVE_AS) {
			datas.get(row).setSaveAs((Boolean) aValue);
		} else if (column == column_ResultDatasource) {
			datas.get(row).setResultDatasource((Datasource) aValue);
			datas.get(row).setResultDatasetName(
					datas.get(row).getResultDatasource().getDatasets().getAvailableDatasetName(datas.get(row).getResultDatasetName()));
		} else if (column == column_ResultDataset) {
			datas.get(row).setResultDatasetName(datas.get(row).getResultDatasource().getDatasets().getAvailableDatasetName((String) aValue));
		}
		fireTableRowsUpdated(row, row);
	}

	public boolean addDataset(Dataset selectedDataset, Datasource saveAsDatasources, String datasetName) {
		if (!isDatasetAdded(selectedDataset)) {
			datas.add(new TransformationAddObjectBean(selectedDataset, saveAsDatasources, datasetName));
			fireTableRowsInserted(datas.size() - 1, datas.size() - 1);
			return true;
		}
		return false;
	}

	public boolean isDatasetAdded(Dataset selectedDataset) {
		for (TransformationAddObjectBean data : datas) {
			if (data.getDataset() == selectedDataset) {
				return true;
			}
		}
		return false;
	}

	public void delete(int row) {
		datas.remove(row);
		fireTableRowsDeleted(row, row);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == COLUMN_DATASET) {
			return Dataset.class;
		} else if (columnIndex == COLUMN_DATA_SOURCE || columnIndex == column_ResultDatasource) {
			return Datasource.class;
		} else if (columnIndex == COLUMN_SAVE_AS || columnIndex == COLUMN_ENABLE) {
			return Boolean.class;
		} else if (columnIndex == column_ResultDataset) {
			return String.class;
		}
		return super.getColumnClass(columnIndex);
	}

	public TransformationAddObjectBean getDataAtRow(int row) {
		return datas.get(row);
	}

	public void setResampleEnable(int selectedModelRow, boolean selected) {
		datas.get(selectedModelRow).setResample(selected);
	}

	public void setResampleMode(int selectedModelRow, TransformationResampleMode resampleMode) {
		datas.get(selectedModelRow).setTransformationResampleMode(resampleMode);
	}

	public void setPixel(int selectedModelRow, Double aDouble) {
		datas.get(selectedModelRow).setCellSize(aDouble);
	}


	public TransformationAddObjectBean[] getEnableDatas() {
		ArrayList<TransformationAddObjectBean> transformationAddObjectBeens = new ArrayList<>();
		for (TransformationAddObjectBean data : datas) {
			if (data.isEnable()) {
				transformationAddObjectBeens.add(data);
			}
		}
		return transformationAddObjectBeens.toArray(new TransformationAddObjectBean[transformationAddObjectBeens.size()]);
	}

	public void addDatas(TransformationAddObjectBean[] transformationAddObjectBeen) {
		Collections.addAll(datas, transformationAddObjectBeen);
		fireTableRowsInserted(datas.size() - transformationAddObjectBeen.length, datas.size() - 1);
	}
}
