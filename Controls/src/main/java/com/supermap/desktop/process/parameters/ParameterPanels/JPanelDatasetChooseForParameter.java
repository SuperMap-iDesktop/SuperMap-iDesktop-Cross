package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.controls.CollectionDataset.JPanelDatasetChoose;
import com.supermap.desktop.ui.controls.DataCell;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by lixiaoyao on 2017/8/31.
 */
public class JPanelDatasetChooseForParameter extends JPanelDatasetChoose {
	private final int COLUMN_INDEX = 0;
	private final int COLUMN_DATASET = 1;
	private final int COLUMN_DATASOURCE = 2;
	private final int MAX_SIZE = 40;
	private boolean isOnly= true;// 加入唯一性验证，即已经加入到table中的数据集，再次加入时如果isOnly= true，则进行验证，如果当前数据集已经存在
	                             // 则不加入，如果不存在则加入，默认是开启了唯一性验证

	public JPanelDatasetChooseForParameter(ArrayList<Dataset> datasets, String[] columnName, boolean[] enableColumn) {
		super(datasets, columnName, enableColumn);
		this.tableDatasetDisplay.getColumnModel().getColumn(COLUMN_INDEX).setMaxWidth(MAX_SIZE);
	}

	@Override
	protected void exchangeItem(int sourceRow, int targetRow) {
		Object targetDatasetCell = tableModel.getValueAt(targetRow, COLUMN_DATASET);
		Object sourceDatasetCell = tableModel.getValueAt(sourceRow, COLUMN_DATASET);
		Object targetDatasourceCell = tableModel.getValueAt(targetRow, COLUMN_DATASOURCE);
		Object sourceDatasourceCell = tableModel.getValueAt(sourceRow, COLUMN_DATASOURCE);

		tableModel.setValueAt(targetDatasetCell, sourceRow, COLUMN_DATASET);
		tableModel.setValueAt(sourceDatasetCell, targetRow, COLUMN_DATASET);
		tableModel.setValueAt(targetDatasourceCell, sourceRow, COLUMN_DATASOURCE);
		tableModel.setValueAt(sourceDatasourceCell, targetRow, COLUMN_DATASOURCE);
	}

	@Override
	protected Object[] transFormData(Dataset dataset) {
		Object[] datasetInfo = null;
		if (dataset.equals(getIllegalDataset())) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_AppendRowError"), dataset.getName()));
		} else {
			if (this.isOnly) {
				if (!onlyProcessResult(dataset)) {
					return null;
				}
			}
			datasetInfo = new Object[3];
			datasetInfo[COLUMN_INDEX] = tableModel.getRowCount() + 1+" ";
			datasetInfo[COLUMN_DATASET] = new DataCell(dataset);
			DataCell cell = new DataCell();
			cell.initDatasourceType(dataset.getDatasource());
			datasetInfo[COLUMN_DATASOURCE] = cell;
		}
		return datasetInfo;
	}

	@Override
	public void setSupportDatasetTypes(DatasetType[] supportDatasetTypes) {
		this.supportDatasetTypes = supportDatasetTypes;
	}

	@Override
	protected Object[][] getData(ArrayList<Dataset> datasets) {
		if (null == datasets || (null != datasets && datasets.size() == 0)) {
			return new Object[0][0];
		}
		int size = datasets.size();
		Object[][] result = new Object[size][];
		DataCell datasetCell;
		DataCell datasourceCell;
		for (int i = 0; i < size; i++) {
			result[i][COLUMN_INDEX] = i + 1;
			datasetCell = new DataCell();
			datasetCell.initDatasetType(datasets.get(i));
			result[i][COLUMN_DATASET] = datasetCell;
			datasourceCell = new DataCell();
			datasourceCell.initDatasourceType(datasets.get(i).getDatasource());
			result[i][COLUMN_DATASOURCE] = datasourceCell;
		}
		return result;
	}

	private boolean onlyProcessResult(Dataset dataset){
		boolean result=true;
		Vector vector= tableModel.getDataVector();
		for (int i=0;i<vector.size();i++){
			DataCell tempDataCell= (DataCell)((Vector) vector.get(i)).get(COLUMN_DATASET);
			if (((Dataset)tempDataCell.getData()).equals(dataset)){
				result=false;
				break;
			}
		}
		return result;
	}

	public boolean isOnly() {
		return this.isOnly;
	}

	public void setOnly(boolean only) {
		this.isOnly = only;
	}

}
