package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

public class BufferProgressCallable extends UpdateProgressCallable {
	private Object sourceData;
	private DatasetVector resultDatasetVector;
	private BufferAnalystParameter bufferAnalystParameter;
	private boolean union;
	private boolean isAttributeRetained;
	private boolean createBufferAnalyst;
	private boolean isSucceed = true;
	private IFormMap formMap = null;
	private boolean isShowInMap;

	private SteppedListener steppedListener = new SteppedListener() {

		@Override
		public void stepped(SteppedEvent arg0) {
			try {
				updateProgress(arg0.getPercent(), String.valueOf(arg0.getRemainTime()), arg0.getMessage());
			} catch (CancellationException e) {
				arg0.setCancel(true);
			}
		}
	};

	public BufferProgressCallable(Object sourceData, DatasetVector resultDatasetVector, BufferAnalystParameter bufferAnalystParameter, boolean union,
	                              boolean isAttributeRetained, boolean isShowInMap) {
		this.sourceData = sourceData;
		this.resultDatasetVector = resultDatasetVector;
		this.bufferAnalystParameter = bufferAnalystParameter;
		this.union = union;
		this.isAttributeRetained = isAttributeRetained;
		this.isShowInMap = isShowInMap;

	}

	@Override
	public Boolean call() throws Exception {
		Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreating"));
		try {
			this.createBufferAnalyst = false;
			// 根据源数据集设置不同的成功提示信息--yuanR 2017.3.10
			String CreatedSuccessMessage = "";
			String CreatedFailedMessage = "";

			BufferAnalyst.addSteppedListener(steppedListener);
			if (this.sourceData instanceof DatasetVector && resultDatasetVector != null) {
				long startTime = System.currentTimeMillis();
				this.createBufferAnalyst = BufferAnalyst.createBuffer((DatasetVector) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
				long endTime = System.currentTimeMillis();
				String time = String.valueOf((endTime - startTime) / 1000.0);

				CreatedSuccessMessage =
						((DatasetVector) sourceData).getName() + "@" + ((DatasetVector) sourceData).getDatasource().getAlias() + " -> " +
								resultDatasetVector.getName() + "@" + resultDatasetVector.getDatasource().getAlias() +
								"," + SpatialAnalystProperties.getString("String_BufferCreatedSuccess")
								+ MessageFormat.format(SpatialAnalystProperties.getString("String_OverlayAnalyst_CostSeconds"), time);

				CreatedFailedMessage =
						((DatasetVector) sourceData).getName() + "@" + ((DatasetVector) sourceData).getDatasource().getAlias() + " -> " +
								resultDatasetVector.getName() + "@" + resultDatasetVector.getDatasource().getAlias() +
								"," + SpatialAnalystProperties.getString("String_BufferCreatedFailed");

			} else if (this.sourceData instanceof Recordset && resultDatasetVector != null) {
				long startTime = System.currentTimeMillis();
				this.createBufferAnalyst = BufferAnalyst.createBuffer((Recordset) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
				long endTime = System.currentTimeMillis();
				String time = String.valueOf((endTime - startTime) / 1000.0);
				CreatedSuccessMessage =
						SpatialAnalystProperties.getString("String_CheckeBox_BufferSelectedRecordset") + ":" +
								((Recordset) sourceData).getDataset().getName() + "@" + ((Recordset) sourceData).getDataset().getDatasource().getAlias() + " -> " +
								resultDatasetVector.getName() + "@" + resultDatasetVector.getDatasource().getAlias() +
								"," + SpatialAnalystProperties.getString("String_BufferCreatedSuccess")
								+ MessageFormat.format(SpatialAnalystProperties.getString("String_OverlayAnalyst_CostSeconds"), time);

				CreatedFailedMessage =
						SpatialAnalystProperties.getString("String_CheckeBox_BufferSelectedRecordset") + ":" +
								((Recordset) sourceData).getDataset().getName() + "@" + ((Recordset) sourceData).getDataset().getDatasource().getAlias() + " -> " +
								resultDatasetVector.getName() + "@" + resultDatasetVector.getDatasource().getAlias() +
								"," + SpatialAnalystProperties.getString("String_BufferCreatedFailed");
			}
			if (createBufferAnalyst) {
				setSucceed(true);
				Application.getActiveApplication().getOutput().output(CreatedSuccessMessage);
				if (isShowInMap) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							addDatasettoMap();
						}
					});
				}
			} else {
				setSucceed(false);
				Application.getActiveApplication().getOutput().output(CreatedFailedMessage);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
		} finally {
			BufferAnalyst.removeSteppedListener(steppedListener);
			if (this.sourceData != null && this.sourceData instanceof Recordset) {
				((Recordset) this.sourceData).dispose();
			}
		}
		return createBufferAnalyst;
	}

	/**
	 * 数据集添加到地图，如果对数据集处理时，直接将数据集添加到新地图，对选中对象进行缓冲区分析时，将数据集添加到当前地图
	 */
	private void addDatasettoMap() {
		if (this.sourceData instanceof DatasetVector) {
			Dataset dataset = (Dataset) this.resultDatasetVector;
			if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
				if (formMap == null) {
					String name = MapUtilities.getAvailableMapName(String.format("%s@%s", dataset.getName(), dataset.getDatasource().getAlias()), true);
					formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
				}
				if (formMap != null) {
					Map map = formMap.getMapControl().getMap();
					MapUtilities.addDatasetToMap(map, dataset, true);
					map.refresh();
					UICommonToolkit.getLayersManager().setMap(map);
					// 新建的地图窗口，修改默认的Action为漫游
					formMap.getMapControl().setAction(Action.PAN);
				}
			}
		}
		// 将由记录集生成的数据集添加到地图操作移到方法外，防止重复添加相同数据集到地图--yuanR 2017.3.13
//		else if (this.sourceData instanceof Recordset) {
//			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
//			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
//			Map map = formMap.getMapControl().getMap();
//			MapViewUIUtilities.addDatasetsToMap(map, datasets, false);
//		}
	}

	public boolean isSucceed() {
		return isSucceed;
	}

	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
}
