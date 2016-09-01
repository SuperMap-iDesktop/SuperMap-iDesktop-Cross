package com.supermap.desktop.CtrlAction.Dataset;

/**
 * @author Administrator 复制和删除数据集界面
 */

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.List;

public class DatasetChooserDataEditor {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean DIALOG_TYPE_COPY = false;
	private boolean DIALOG_TYPE_DELETE = false;
	private int COLUMN_INDEX_SOURCEDATASET = 0;
	private int COLUMN_INDEX_SOURCEDATASOURCE = 1;
	private int COLUMN_INDEX_TARGETDATASOURCE = 2;
	private int COLUMN_INDEX_TARGETDATASET = 3;
	private int COLUMN_INDEX_CODINGTYPE = 4;
	private int COLUMN_INDEX_CHARSET = 5;
	private MutiTable datasetCopyTable;

	DatasetChooser datasetChooser;

	public DatasetChooserDataEditor(JDialog owner, Datasource datasource, final MutiTable datasetCopyTable, boolean DIALOG_TYPE_COPY) {
		this.datasetCopyTable = datasetCopyTable;
		this.DIALOG_TYPE_COPY = DIALOG_TYPE_COPY;
		datasetChooser = new DatasetChooser(owner) {
			@Override
			protected boolean isSupportDatasource(Datasource datasource) {
				return CtrlActionCopyDataset.isSupportEngineType(datasource.getEngineType()) && super.isSupportDatasource(datasource);
			}
		};
		if (datasetChooser.showDialog() == DialogResult.OK) {
			addInfoToMainTable();
		}
	}

	public DatasetChooserDataEditor(JFrame owner, Datasource datasource, boolean DIALOG_TYPE_DELETE) {
		this.DIALOG_TYPE_DELETE = DIALOG_TYPE_DELETE;
		datasetChooser = new DatasetChooser() {
			@Override
			protected boolean isSupportDatasource(Datasource datasource) {
				return !datasource.isReadOnly() && super.isSupportDatasource(datasource);
			}
		};
		if (datasetChooser.showDialog() == DialogResult.OK) {
			DeleteThread thread = new DeleteThread();
			datasetChooser.dispose();
			thread.run();
		}
		datasetChooser.dispose();
	}

	class DeleteThread extends Thread {

		@Override
		public void run() {
			try {
				CursorUtilities.setWaitCursor();
				deleteFromDatasource();
			} finally {
				CursorUtilities.setDefaultCursor();
			}
		}
	}

	private void deleteFromDatasource() {
		List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
		int count = selectedDatasets.size();
		Datasource datasource = selectedDatasets.get(0).getDatasource();
		String datasourceName = datasource.getAlias();
		if (1 == count) {
			String datasetName = selectedDatasets.get(0).getName();

			if (JOptionPane.OK_OPTION == UICommonToolkit
					.showConfirmDialog(MessageFormat.format(DataEditorProperties.getString("String_DelectOneDataset"), datasourceName, datasetName))) {

				Dataset deleteDataset = datasource.getDatasets().get(datasetName);
				boolean result = datasource.getDatasets().delete(deleteDataset.getName());
				if (result) {
					deleteDataset = null;
					String successInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupSuccess"), datasourceName,
							datasetName);
					Application.getActiveApplication().getOutput().output(successInfo);
				} else {
					String failedInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupFailed"), datasourceName, datasetName);
					Application.getActiveApplication().getOutput().output(failedInfo);
				}
			}
		} else if (JOptionPane.OK_OPTION == UICommonToolkit
				.showConfirmDialog(MessageFormat.format(DataEditorProperties.getString("String_DelectMoreDataset"), count))) {
			// 删除选中的多条数据
			for (int i = selectedDatasets.size() - 1; i >= 0; i--) {
				Dataset deleteDataset = selectedDatasets.get(i);
				String deleteDatasetName = deleteDataset.getName();
				boolean result = deleteDataset.getDatasource().getDatasets().delete(deleteDatasetName);
				if (result) {
					deleteDataset = null;
					String successInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupSuccess"), datasourceName,
							deleteDatasetName);
					Application.getActiveApplication().getOutput().output(successInfo);
				} else {
					String failedInfo = MessageFormat.format(DataEditorProperties.getString("String_Message_DelGroupFailed"), datasourceName, deleteDatasetName);
					Application.getActiveApplication().getOutput().output(failedInfo);
				}
			}
		}
	}

	private void addInfoToMainTable() {
		try {
			List<Dataset> selectedDatasets = datasetChooser.getSelectedDatasets();
			int rowCount = datasetCopyTable.getRowCount();
			for (Dataset dataset : selectedDatasets) {
				Object[] temp = new Object[6];
				DataCell datasetCell = new DataCell();
				datasetCell.initDatasetType(dataset);
				temp[COLUMN_INDEX_SOURCEDATASET] = datasetCell;
				Datasource datasource = dataset.getDatasource();
				DataCell datasourceCell = new DataCell();
				datasourceCell.initDatasourceType(datasource);
				DataCell datasoureCell = datasourceCell;
				temp[COLUMN_INDEX_SOURCEDATASOURCE] = datasoureCell;
				temp[COLUMN_INDEX_TARGETDATASOURCE] = datasoureCell;
				temp[COLUMN_INDEX_TARGETDATASET] = datasource.getDatasets().getAvailableDatasetName(dataset.getName());
				temp[COLUMN_INDEX_CODINGTYPE] = CommonToolkit.EncodeTypeWrap.findName(dataset.getEncodeType());
				if (dataset instanceof DatasetVector) {
					temp[COLUMN_INDEX_CHARSET] = CharsetUtilities.toString(((DatasetVector) dataset).getCharset());
				} else {
					temp[COLUMN_INDEX_CHARSET] = CharsetUtilities.toString(null);
				}
				datasetCopyTable.addRow(temp);
			}
			if (rowCount < datasetCopyTable.getRowCount()) {
				datasetCopyTable.setRowSelectionInterval(rowCount, datasetCopyTable.getRowCount() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
