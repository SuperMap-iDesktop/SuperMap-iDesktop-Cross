package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CancellationException;

public class DatasetCopyCallable extends UpdateProgressCallable {
	private static final int COLUMN_INDEX_Dataset = 0;
	private static final int COLUMN_INDEX_CurrentDatasource = 1;
	private static final int COLUMN_INDEX_TargetDatasource = 2;
	private static final int COLUMN_INDEX_TargetDataset = 3;
	private static final int COLUMN_INDEX_EncodeType = 4;
	private static final int COLUMN_INDEX_Charset = 5;
	private MutiTable table;
	private Datasource datasource;
	private PercentListener percentListener;
	private EngineType[] UN_SUPPORT_TYPE = new EngineType[]{EngineType.OGC, EngineType.ISERVERREST,
		EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS, EngineType.BAIDUMAPS, EngineType.OPENSTREETMAPS, EngineType.MAPWORLD};

	public DatasetCopyCallable(MutiTable table) {
		this.table = table;
	}

	public DatasetCopyCallable(Datasource datasource) {
		this.datasource = datasource;
	}

	@Override
	public Boolean call() throws Exception {
		try {
			if (null != table) {
				copyDatasetForMapEditor();
			} else {
				copyDatasets(this.datasource);
			}
		} catch (Exception ex) {
			
		}finally{
			this.datasource.removeSteppedListener(percentListener);
		}
		return true;
	}

	private void copyDatasetForMapEditor() {
		MutiTableModel tableModel = (MutiTableModel) table.getModel();

		final int count = table.getRowCount();
		List<Object> contents = tableModel.getContents();
		Workspace workspace = Application.getActiveApplication().getWorkspace();

		for (int i = 0; i < count; i++) {
			@SuppressWarnings("unchecked")
			Vector<Object> vector = (Vector<Object>) contents.get(i);

			String currentDatasourceStr = vector.get(COLUMN_INDEX_CurrentDatasource).toString();
			String datasetStr = vector.get(COLUMN_INDEX_Dataset).toString();
			String targetDatasourceStr = vector.get(COLUMN_INDEX_TargetDatasource).toString();
			String targetDatasetName = vector.get(COLUMN_INDEX_TargetDataset).toString();
			String encodingType = vector.get(COLUMN_INDEX_EncodeType).toString();
			String charset = vector.get(COLUMN_INDEX_Charset).toString();
			Datasource currentDatasource = workspace.getDatasources().get(currentDatasourceStr);
			Dataset dataset = DatasourceUtilities.getDataset(datasetStr, currentDatasource);
			if (!isSupportEngineType(dataset.getDatasource().getEngineType())) {
				break;
			}
			datasource = workspace.getDatasources().get(targetDatasourceStr);
			Dataset resultDataset = null;
			if (StringUtilities.isNullOrEmpty(targetDatasetName) || !isAviliableName(targetDatasetName)
					|| !datasource.getDatasets().isAvailableDatasetName(targetDatasetName)) {
				targetDatasetName = datasource.getDatasets().getAvailableDatasetName(targetDatasetName);
			}

			if (!datasource.isReadOnly()) {
				percentListener = new PercentListener(i, count, targetDatasetName);
				datasource.addSteppedListener(percentListener);
				if (null != CharsetUtilities.valueOf(charset)) {
					resultDataset = datasource.copyDataset(dataset, targetDatasetName, CommonToolkit.EncodeTypeWrap.findType(encodingType),
							CharsetUtilities.valueOf(charset));
				} else {
					resultDataset = datasource.copyDataset(dataset, targetDatasetName, CommonToolkit.EncodeTypeWrap.findType(encodingType));
				}
			} else {
				String info = MessageFormat.format(ControlsProperties.getString("String_PluginDataEditor_MessageCopyDatasetOne"), targetDatasourceStr);
				Application.getActiveApplication().getOutput().output(info);
			}
			if (null != resultDataset) {

				String copySuccess = MessageFormat.format(ControlsProperties.getString("String_CopyDataset_Success"), currentDatasourceStr, datasetStr,
						targetDatasourceStr, targetDatasetName);
				Application.getActiveApplication().getOutput().output(copySuccess);
			} else {
				String copyFailed = MessageFormat.format(ControlsProperties.getString("String_CopyDataset_Failed"), currentDatasourceStr, datasetStr,
						targetDatasourceStr);
				Application.getActiveApplication().getOutput().output(copyFailed);
			}

			if (percentListener != null && percentListener.isCancel()) {
				break;
			}
		}
	}

	private void copyDatasets(Datasource datasource) {
		try {
			String targetDatasourceStr = datasource.getAlias();
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			// 只读数据源不支持复制
			if (datasource.isReadOnly()) {
				String info = MessageFormat.format(ControlsProperties.getString("String_PluginDataEditor_MessageCopyDatasetOne"), targetDatasourceStr);
				Application.getActiveApplication().getOutput().output(info);
				return;
			}
			if (1 == datasets.length) {
				// 只复制一个数据集
				Dataset targetDataset = datasets[0];
				// web数据源不能复制到udb数据源中
				if (!isSupportEngineType(targetDataset.getDatasource().getEngineType())) {
					Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_DatasetCopy_WarningForWebToUDB"));
					return;
				}
				// 提示是否进行复制操作
				percentListener = new PercentListener(1, 1, targetDataset.getName());
				datasource.addSteppedListener(percentListener);
				copyDatasetToDatasource(targetDataset, targetDatasourceStr, datasource);
				if (percentListener.isCancel()) {
					return;
				}

			} else if (1 < datasets.length) {
				// 复制多个数据集

				for (int i = 0; i < datasets.length; i++) {
					if (!isSupportEngineType(datasets[i].getDatasource().getEngineType())) {
						Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_DatasetCopy_WarningForWebToUDB"));
						break;
					}
					percentListener = new PercentListener(i, datasets.length, datasets[i].getName());
					datasource.addSteppedListener(percentListener);
					copyDatasetToDatasource(datasets[i], targetDatasourceStr, datasource);
					if (percentListener.isCancel()) {
						break;
					}
				}
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private Dataset copyDatasetToDatasource(Dataset targetDataset, String targetDatasourceStr, Datasource datasource) {
		Dataset resultDataset = null;
		String targetDatasetName = datasource.getDatasets().getAvailableDatasetName(targetDataset.getName());

		if (targetDataset instanceof DatasetVector) {
			// 如果是矢量数据集调用带有字符集的复制方法
			DatasetVector datasetVector = (DatasetVector) targetDataset;
			resultDataset = datasource.copyDataset(targetDataset, targetDatasetName, datasetVector.getEncodeType(), datasetVector.getCharset());
		} else {
			resultDataset = datasource.copyDataset(targetDataset, targetDatasetName, targetDataset.getEncodeType());
		}
		String datasetStr = targetDataset.getName();
		String currentDatasourceStr = targetDataset.getDatasource().getAlias();
		if (null != resultDataset) {
			String copySuccess = MessageFormat.format(ControlsProperties.getString("String_CopyDataset_Success"), currentDatasourceStr, datasetStr,
					targetDatasourceStr, targetDatasetName);
			Application.getActiveApplication().getOutput().output(copySuccess);
			return resultDataset;
		} else {
			String copyFailed = MessageFormat.format(ControlsProperties.getString("String_CopyDataset_Failed"), currentDatasourceStr, datasetStr,
					targetDatasourceStr);
			Application.getActiveApplication().getOutput().output(copyFailed);
		}
		return null;
	}

	private boolean isAviliableName(String datasetName) {
		boolean flag = false;
		char c = datasetName.charAt(0);
		if ('_' == c || ('0' < c && c < '9')) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	private boolean isSupportEngineType(EngineType engineType) {
		boolean result = true;

		for (EngineType type : UN_SUPPORT_TYPE) {
			if (engineType == type) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	class PercentListener implements SteppedListener {
		private boolean isCancel = false;
		private int count;
		private int i;
		private String datasetName;

		PercentListener(int i, int count, String datasetName) {
			this.count = count;
			this.i = i;
			this.datasetName = datasetName;
		}

		public boolean isCancel() {
			return this.isCancel;
		}

		@Override
		public void stepped(SteppedEvent arg0) {
			try {
				int totalPercent = (100 * (this.i - 1) + arg0.getPercent()) / count;
				updateProgressTotal(arg0.getPercent(), arg0.getMessage(), totalPercent,
						MessageFormat.format(ControlsProperties.getString("String_DatasetCopy_Info"), datasetName));
			} catch (CancellationException e) {
				arg0.setCancel(true);
				this.isCancel = true;
			}

		}

	}
}
