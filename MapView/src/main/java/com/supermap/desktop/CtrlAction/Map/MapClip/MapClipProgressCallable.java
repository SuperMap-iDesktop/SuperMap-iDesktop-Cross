package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.analyst.spatialanalyst.RasterClip;
import com.supermap.analyst.spatialanalyst.VectorClip;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CancellationException;

import static com.supermap.desktop.CtrlAction.Map.MapClip.DialogMapClip.*;

/**
 * @author YuanR
 *         地图裁剪进度条类
 */
public class MapClipProgressCallable extends UpdateProgressCallable {

	private Vector VectorInfo;
	private Dataset resultDataset;
	private Map resultMap;

	ArrayList<Dataset> datasetsArrayList;

	public Dataset[] getResultDatasets() {
		this.resultDatasets = new Dataset[datasetsArrayList.size()];
		for (int i = 0; i < datasetsArrayList.size(); i++) {
			this.resultDatasets[i] = datasetsArrayList.get(i);
		}
		return resultDatasets;
	}

	Dataset[] resultDatasets;
	private boolean createMapClip;
	private PercentListener percentListener;

	/**
	 *
	 */
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
						MessageFormat.format(MapViewProperties.getString("String_MapClip_ProgressCurrentLayer"), datasetName));
			} catch (CancellationException e) {
				arg0.setCancel(true);
				this.isCancel = true;
			}
		}
	}

	/**
	 * @param vector
	 */
	public MapClipProgressCallable(Vector vector, Map saveMap) {
		this.VectorInfo = vector;
		this.resultMap = saveMap;
	}

	@Override
	public Boolean call() throws Exception {
		try {
			Application.getActiveApplication().getOutput().output((MapViewProperties.getString("String_MapClip_Start")));
			this.createMapClip = false;
			this.VectorInfo.size();

			long startTime = System.currentTimeMillis();
			this.datasetsArrayList = new ArrayList<>();//结果数据集集合，用于取消删除回退
			ArrayList<Dataset> datasetsClipped = new ArrayList<>();//裁剪过的数据集集合，避免一个数据集多次裁剪

			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			Thread.currentThread().sleep(1000);
			for (int i = 0; i < this.VectorInfo.size(); i++) {
				try {
					Dataset dataset = (Dataset) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_SOURCEDATASET);
					if (percentListener != null && percentListener.isCancel) {
						this.resultMap = null;
						if (datasetsArrayList != null && datasetsArrayList.size() > 0) {
							for (int j = 0; j < datasetsArrayList.size(); j++) {
								Datasets datasets = datasetsArrayList.get(j).getDatasource().getDatasets();
								datasets.delete(datasetsArrayList.get(j).getName());
							}
							datasetsArrayList = null;
						}
						break;
					}
					if (percentListener == null) {
						percentListener = new PercentListener(i, this.VectorInfo.size(), dataset.getName());
						RasterClip.addSteppedListener(percentListener);
						VectorClip.addSteppedListener(percentListener);
					} else {
						percentListener.i = i;
						percentListener.datasetName = dataset.getName();
					}
					String targetDatasetName;
					Layer layer = (Layer) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_LAYER);
					Dataset sourceDataset = (Dataset) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_SOURCEDATASET);
					PrjCoordSys prjCoordSys = sourceDataset.getPrjCoordSys();
					GeoRegion userRegion = (GeoRegion) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_USERREGION);
					//组件：根据源投影坐标系与目标投影坐标系对几何对象进行投影转换，结果将直接改变源几何对象。
					GeoRegion copyRegion = new GeoRegion(userRegion);
					if (formMap.getMapControl().getMap().isDynamicProjection() &&
							!sourceDataset.getPrjCoordSys().equals(formMap.getMapControl().getMap().getPrjCoordSys())) {
						CoordSysTranslator.convert(copyRegion,
								formMap.getMapControl().getMap().getPrjCoordSys(),
								sourceDataset.getPrjCoordSys(),
								formMap.getMapControl().getMap().getDynamicPrjTransParameter(),
								formMap.getMapControl().getMap().getDynamicPrjTransMethond());
					}
					boolean isClipInRegion = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISCLIPINREGION);
					boolean isEraseSource = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE);
					boolean isExactClip = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE);
					Datasource targetDatasource = (Datasource) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETSOURCE);
					targetDatasetName = (String) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETNAME);

					if (!datasetsClipped.contains(sourceDataset)) {
						if (targetDatasource.getDatasets().isAvailableDatasetName(targetDatasetName)) {
							targetDatasetName = DatasetUtilities.getAvailableDatasetName(targetDatasource, targetDatasetName, null);
						}
						if (sourceDataset instanceof DatasetVector) {
							this.resultDataset = VectorClip.clipDatasetVector((DatasetVector) sourceDataset, copyRegion, isClipInRegion,
									isEraseSource, targetDatasource, targetDatasetName);
						} else {
							this.resultDataset = RasterClip.clip(sourceDataset, copyRegion, isClipInRegion,
									isExactClip, targetDatasource, targetDatasetName);
						}
						if (resultDataset != null) {
							datasetsClipped.add(sourceDataset);
						}
						this.resultDataset.setPrjCoordSys(prjCoordSys);
					} else {
						if (this.resultMap != null) {
							MapUtilities.findLayerByName(this.resultMap, layer.getName()).setDataset(targetDatasource.getDatasets().get(targetDatasetName));
						}
						this.resultDataset = null;
					}
					sourceDataset.setPrjCoordSys(prjCoordSys);
					if (this.resultDataset != null) {
						datasetsArrayList.add(this.resultDataset);
						if (this.resultMap != null) {
							MapUtilities.findLayerByName(this.resultMap, layer.getName()).setDataset(this.resultDataset);
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
			if (this.resultMap != null) {
				formMap.getMapControl().getMap().getWorkspace().getMaps().add(this.resultMap.getName(), this.resultMap.toXML());
			}
			long endTime = System.currentTimeMillis();
			String time = String.valueOf((endTime - startTime) / 1000.0);
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_MapClip_Finished"), time));
			this.createMapClip = true;
		} catch (Exception e) {
			this.createMapClip = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			VectorClip.removeSteppedListener(percentListener);
			RasterClip.removeSteppedListener(percentListener);
		}
		return this.createMapClip;
	}

}
