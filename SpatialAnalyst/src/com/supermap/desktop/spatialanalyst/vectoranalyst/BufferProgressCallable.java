package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.util.concurrent.CancellationException;

import javax.swing.SwingUtilities;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

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
			createBufferAnalyst = false;
			BufferAnalyst.addSteppedListener(steppedListener);
			if (this.sourceData instanceof DatasetVector && resultDatasetVector != null) {
				createBufferAnalyst = BufferAnalyst.createBuffer((DatasetVector) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
			} else if (this.sourceData instanceof Recordset && resultDatasetVector != null) {
				createBufferAnalyst = BufferAnalyst.createBuffer((Recordset) sourceData, resultDatasetVector, bufferAnalystParameter, union,
						isAttributeRetained);
			}
			if (createBufferAnalyst) {
				setSucceed(true);
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedSuccess"));
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
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));

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
		} else if (this.sourceData instanceof Recordset) {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map map = formMap.getMapControl().getMap();
			MapViewUIUtilities.addDatasetsToMap(map, datasets, false);
		}
	}

	public boolean isSucceed() {
		return isSucceed;
	}

	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
}
