package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class TransformationTableModel extends DefaultTableModel {

	private List<TransformationAddObjectBean> datas = new ArrayList<>();
	private String[] columnNames = new String[]{
			CommonProperties.getString("String_ColumnHeader_SourceDataset"),
			CommonProperties.getString("String_ColumnHeader_SourceDatasource"),
			DataEditorProperties.getString("String_Transformation_ColumnNeedResave"),
			CommonProperties.getString("String_Label_ResultDatasource"),
			CommonProperties.getString("String_Label_ResultDataset"),
	};

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
		if (column == 0 || column == 1) {
			return false;
		}
		if (column == 2) {
			if (datas.get(row).getDataset().getDatasource().isReadOnly()) {
				Application.getActiveApplication().getOutput().output(
						MessageFormat.format(DataEditorProperties.getString("String_Transformation_DatasetReadonly"), datas.get(row).getDataset().getName()));
			}
			return false;
		}
		if (column == 3 || column == 4) {
			return datas.get(row).isSaveAs();
		}
		return super.isCellEditable(row, column);
	}

	@Override
	public Object getValueAt(int row, int column) {

		TransformationAddObjectBean data = datas.get(row);
		switch (column) {
			case 0:
				data.getDataset();
			case 1:
				data.getDataset().getDatasource();
			case 2:
				data.isSaveAs();
			case 3:
				data.getResultDatasource();
			case 4:
				data.getResultDatasetName();
		}
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 3) {
			datas.get(row).setSaveAs((Boolean) aValue);
		} else if (column == 4) {
			datas.get(row).setResultDatasource((Datasource) aValue);
			datas.get(row).setResultDatasetName(
					datas.get(row).getResultDatasource().getDatasets().getAvailableDatasetName(datas.get(row).getResultDatasetName()));
		} else if (column == 5) {
			datas.get(row).setResultDatasetName((String) aValue);
		}
		fireTableRowsUpdated(row, row);
		super.setValueAt(aValue, row, column);
	}

	public void addDataset(Dataset selectedDataset, Datasource saveAsDatasources, String datasetName) {
		datas.add(new TransformationAddObjectBean(selectedDataset, saveAsDatasources, datasetName));
		fireTableRowsInserted(datas.size() - 2, datas.size() - 1);
	}
}
