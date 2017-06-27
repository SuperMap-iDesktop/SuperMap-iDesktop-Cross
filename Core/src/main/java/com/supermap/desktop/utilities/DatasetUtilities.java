package com.supermap.desktop.utilities;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.StatisticMode;
import com.supermap.data.Tolerance;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3Ds;
import com.supermap.realspace.Scene;
import com.supermap.realspace.TerrainLayers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DatasetUtilities {

	private DatasetUtilities() {
		// 工具类，不提供构造方法
	}

	/**
	 * 判断数据集名称是否合法
	 *
	 * @param newDatasetName
	 * @param datasource
	 * @return
	 */
	public static boolean isAvailableDatasetName(String newDatasetName, Datasource datasource) {
		if (!datasource.getDatasets().isAvailableDatasetName(newDatasetName)) {
			return false;
		}
		if (datasource.getDatasets().contains(newDatasetName)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取数据集的默认容限
	 *
	 * @param dataset
	 * @return
	 */
	public static Tolerance getDefaultTolerance(DatasetVector dataset) {
		Tolerance tolerance = null;
		try {
			if (dataset != null) {
				tolerance = new Tolerance();
				Double extent = Math.max(dataset.getBounds().getHeight(), dataset.getBounds().getWidth());

				// tolerance.NodeSnap = extent / 1000000.0f;
				tolerance.setNodeSnap(dataset.getTolerance().getNodeSnap());
				if (Double.compare(0.0, tolerance.getNodeSnap()) == 0) {

					if (dataset != null) {
						if (dataset.getPrjCoordSys().getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
							tolerance.setNodeSnap(0.00001);
						} else if (dataset.getPrjCoordSys().getType() == PrjCoordSysType.PCS_NON_EARTH) {
							tolerance.setNodeSnap(extent / 1000000.0f);
						} else {
							tolerance.setNodeSnap(1);
						}
					}
				}
				// 修改长短悬线容限初始值
				tolerance.setDangle((Math.abs(dataset.getTolerance().getDangle()) < 1E-6) ? dataset.getTolerance().getNodeSnap() * 100 : dataset.getTolerance()
						.getDangle());
				tolerance.setExtend((Math.abs(dataset.getTolerance().getExtend()) < 1E-6) ? dataset.getTolerance().getNodeSnap() * 100 : dataset.getTolerance()
						.getExtend());
				tolerance.setSmallPolygon(0.0);
				tolerance.setGrain(extent / 1000.0f);
				if (dataset.getType() == DatasetType.REGION) {
					Boolean isOpen = dataset.isOpen();
					int fieldIndex = dataset.getFieldInfos().indexOf("SMAREA");
					if (fieldIndex >= 0) {
						Double maxArea = dataset.statistic(fieldIndex, StatisticMode.MAX);
						tolerance.setSmallPolygon(maxArea / 1000000.0f);
					}
					if (!isOpen) {
						dataset.close();
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return tolerance;
	}

	/**
	 * 判断数据集是否已经打开
	 *
	 * @param dataset 需要判断的数据集
	 * @return true-数据集已打开 false-数据集未打开
	 */
	public static boolean isDatasetOpened(Dataset dataset) {
		if (null == dataset) {
			return false;
		}
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int i = 0; i < formManager.getCount(); i++) {
			IForm form = formManager.get(i);
			if (form instanceof IFormMap) {
				// 判断地图有没有
				Layers layers = ((IFormMap) form).getMapControl().getMap().getLayers();
				if (isIncludeDataset(layers, dataset)) {
					return true;
				}
			} else if (form instanceof IFormScene) {
				// 判断场景有没有
				Scene scene = ((IFormScene) form).getSceneControl().getScene();

				// 地形图层
				TerrainLayers terrainLayers = scene.getTerrainLayers();
				for (int j = 0; j < terrainLayers.getCount(); j++) {
					if (terrainLayers.get(j).getDataset().equals(dataset)) {
						return true;
					}
				}
				// 普通图层
				Layer3Ds layer3Ds = scene.getLayers();
				for (int j = 0; j < layer3Ds.getCount(); j++) {
					if (layer3Ds.get(j) instanceof Layer3DDataset && ((Layer3DDataset) layer3Ds.get(j)).getDataset().equals(dataset)) {
						return true;
					}
				}
			} else if (form instanceof IFormTabular && ((IFormTabular) form).getRecordset().getDataset().equals(dataset)) {
				// 属性表
				return true;
			}
		}
		// 遍历完还活着返回false
		return false;
	}

	public static boolean isIncludeDataset(Layers layers, Dataset dataset) {
		for (int i = 0; i < layers.getCount(); i++) {
			if (isIncludeDataset(layers.get(i), dataset)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isIncludeDataset(Layer layer, Dataset dataset) {
		if (layer instanceof LayerGroup) {
			for (int i = 0; i < ((LayerGroup) layer).getCount(); i++) {
				if (isIncludeDataset(((LayerGroup) layer).get(i), dataset)) {
					return true;
				}
			}
		} else if (layer.getDataset() == dataset) {
			return true;
		}
		return false;
	}

	public static boolean removeLayerGroupDataset(LayerGroup layerGroup, Dataset dataset) {
		boolean result = false;
		for (int i = layerGroup.getCount(); i > 0; i--) {
			Layer layer = layerGroup.get(i - 1);
			if (layer instanceof LayerGroup) {
				result = removeLayerGroupDataset((LayerGroup) layer, dataset) || result;
			} else if (layer.getDataset() == dataset) {
				result = layerGroup.remove(layer) || result;
			}
		}
		return result;
	}

	public static boolean removeByDataset(Layers layers, Dataset closeDataset) {
		boolean result = false;
		for (int i = layers.getCount() - 1; i >= 0; i--) {
			Layer layer = layers.get(i);
			if (layer instanceof LayerGroup) {
				result = removeLayerGroupDataset((LayerGroup) layer, closeDataset) || result;
			} else if (layer.getDataset() == closeDataset) {
				result = layers.remove(i) || result;
			}
		}
		return result;
	}

	/**
	 * 删除图层中包含对应数据集的图层。 组件的方法有缺陷而且不改，所以自行实现。
	 *
	 * @param layers        需要删除地图的layers对象
	 * @param closeDatasets 关闭的数据集集合
	 * @return
	 */
	public static boolean removeByDatasets(Layers layers, Dataset... closeDatasets) {
		boolean result = false;
		for (Dataset datasetTemp : closeDatasets) {
			try {
				// layer移除之后可能关闭窗口，做个判断
				if (layers.getCount() <= 0) {
					return result;
				}
			} catch (Exception e) {
				return result;
			}
			if (datasetTemp.getType() == DatasetType.NETWORK || datasetTemp.getType() == DatasetType.NETWORK3D) {
				result = removeByDataset(layers, ((DatasetVector) datasetTemp).getChildDataset()) || result;
			}
			result = removeByDataset(layers, datasetTemp) || result;
		}
		return result;
	}

	/**
	 * 删除数据集
	 *
	 * @param datasets
	 */
	public static void deleteDataset(Dataset[] datasets) {
		closeDataset(datasets);
		for (int i = 0; i < datasets.length; i++) {
			String resultInfo = MessageFormat.format(CommonProperties.getString("String_DelectDatasetSuccessfulInfo"), datasets[i]
					.getDatasource().getAlias(), datasets[i].getName());
			datasets[i].getDatasource().getDatasets().delete(datasets[i].getName());
			Application.getActiveApplication().getOutput().output(resultInfo);
		}
		Application.getActiveApplication().setActiveDatasets(null);
	}

	/**
	 * 关闭数据集
	 *
	 * @param closeDataset 需要关闭的数据集
	 */
	public static void closeDataset(Dataset... closeDataset) {
		try {
			if (closeDataset == null || closeDataset.length <= 0) {
				return;
			}

			List<Dataset> datasets = new ArrayList<>();
			for (int i = 0; i < closeDataset.length; i++) {
				Dataset dataset = closeDataset[i];
				datasets.add(dataset);
				if (dataset instanceof DatasetVector && null != ((DatasetVector) dataset).getChildDataset()) {
					datasets.add(((DatasetVector) dataset).getChildDataset());
				}
			}
			closeDataset = (Dataset[]) datasets.toArray(new Dataset[datasets.size()]);
			if (null != Application.getActiveApplication().getMainFrame().getFormManager()
					&& 0 < Application.getActiveApplication().getMainFrame().getFormManager().getCount()) {
				// 删除时考虑地图与场景

				int formNumber = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
				for (int i = formNumber - 1; i >= 0; i--) {
					IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(i);
					if (form instanceof IFormMap) {

						((IFormMap) form).removeActiveLayersByDatasets(closeDataset);
						if (form.getWindowType() == WindowType.MAP) {

							Map map = ((IFormMap) form).getMapControl().getMap();
							Layers layers = map.getLayers();
							if (removeByDatasets(layers, closeDataset) && Application.getActiveApplication().getMainFrame().getFormManager().isContain(((IFormMap) form))) {
								map.refresh();
							}
						} else if (form.getWindowType() == WindowType.TRANSFORMATION) {
							// 不需要，已经在上方处理
						}
					} else if (form instanceof IFormScene) {
						Scene scene = ((IFormScene) form).getSceneControl().getScene();
						TerrainLayers terrainLayers = scene.getTerrainLayers();
						Layer3Ds layer3Ds = scene.getLayers();
						for (int j = 0; j < closeDataset.length; j++) {
							// 移除地形图层
							for (int k = terrainLayers.getCount() - 1; k >= 0; k--) {
								if (closeDataset[j] == terrainLayers.get(k).getDataset()) {
									terrainLayers.remove(k);
								}
							}
							// 移除普通图层
							for (int k = layer3Ds.getCount() - 1; k >= 0; k--) {
								if (layer3Ds.get(k) instanceof Layer3DDataset && (closeDataset[j] == ((Layer3DDataset) layer3Ds.get(k)).getDataset())) {
									layer3Ds.remove(k);
								}
							}
						}
						scene.refresh();
					} else if (form instanceof IFormTabular) {
						Dataset dataset = ((IFormTabular) form).getRecordset().getDataset();
						for (int j = 0; j < closeDataset.length; j++) {
							if (closeDataset[j] == dataset) {
								Application.getActiveApplication().getMainFrame().getFormManager().close(form);
								break;
							}
						}
					}
				}
			}
			clearDataset(closeDataset);
		} catch (Exception e) {
			// do nothing
		}
	}

	public static void clearDataset(Dataset... datasets) {
		for (int i = datasets.length - 1; i >= 0; i--) {
			datasets[i].close();
		}
	}

	/**
	 * 关闭数据集
	 *
	 * @param closeDatasets ：需要关闭的数据集集合类
	 */
	public static void closeDataset(Datasets closeDatasets) {
		if (null == closeDatasets || 0 == closeDatasets.getCount()) {
			return;
		}
		List<Dataset> datasets = new ArrayList<>();
		// Dataset[] datasets = new Dataset[closeDatasets.getCount()];
		for (int i = 0; i < closeDatasets.getCount(); i++) {
			Dataset dataset = closeDatasets.get(i);
			datasets.add(dataset);
			// if (dataset instanceof DatasetVector) {
			// if (null != ((DatasetVector) dataset).getChildDataset()) {
			// datasets.add(((DatasetVector) dataset).getChildDataset());
			// }
			// }
		}
		closeDataset((Dataset[]) datasets.toArray(new Dataset[datasets.size()]));
	}

	//

	/**
	 * 根据已有的数据集名，获取指定前缀字符串的唯一数据集名
	 *
	 * @param datasetName     指定的数据集名称
	 * @param allDatasetNames 即将增加的数据集的名称
	 * @return 可用数据集名称
	 */
	public static String getAvailableDatasetName(String datasetName, String[] allDatasetNames) {
		String datasetNameTemp = datasetName;
		String availableMapName = "";
		try {
			if (datasetNameTemp.length() == 0) {
				datasetNameTemp = "Dataset";
			}

			String tempName = "";
			tempName = datasetNameTemp.toLowerCase();
			if (allDatasetNames.length > 0) {
				ArrayList<String> datasetNames = new ArrayList<String>();
				for (int index = 0; index < allDatasetNames.length; index++) {
					datasetNames.add(allDatasetNames[index].toLowerCase());
				}

				if (!datasetNames.contains(tempName)) {
					availableMapName = datasetNameTemp;
				} else {
					int indexMapName = 1;
					while (true) {
						availableMapName = String.format("%s_%d", datasetNameTemp, indexMapName);
						tempName = availableMapName.toLowerCase();
						if (!datasetNames.contains(tempName)) {
							break;
						}

						indexMapName += 1;
					}
				}
			} else {
				availableMapName = datasetNameTemp;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return availableMapName;
	}

	/**
	 * 根据已有的数据源和即将创建的数据集，获取指定前缀字符串的唯一数据集名
	 *
	 * @param datasource  保存数据集的数据源
	 * @param datasetName 指定的数据集名称
	 * @return 可用数据集名称
	 */
	public static String getAvailableDatasetName(Datasource datasource, String datasetName, String[] newDatasetNames) {
		String datasetNameInfo = datasetName;
		String availableDatasetName = "";
		try {
			if (datasetNameInfo == null || datasetNameInfo.length() <= 0) {
				datasetNameInfo = "Dataset";
			}
			datasetNameInfo = datasource.getDatasets().getAvailableDatasetName(datasetNameInfo);

			ArrayList<String> datasetNames = new ArrayList<String>();
			for (int index = 0; index < datasource.getDatasets().getCount(); index++) {
				Dataset dataset = datasource.getDatasets().get(index);
				datasetNames.add(dataset.getName().toLowerCase());
			}

			if (newDatasetNames != null) {
				for (int index = 0; index < newDatasetNames.length; index++) {
					datasetNames.add(newDatasetNames[index].toLowerCase());
				}
			}
			String tempName = "";
			tempName = datasetNameInfo.toLowerCase();
			if (!datasetNames.isEmpty()) {
				if (!datasetNames.contains(tempName)) {
					availableDatasetName = datasetNameInfo;
				} else {
					int indexMapName = 1;
					while (true) {
						availableDatasetName = String.format("%s_%d", datasetNameInfo, indexMapName);
						tempName = availableDatasetName.toLowerCase();
						if (!datasetNames.contains(tempName)) {
							break;
						}

						indexMapName += 1;
					}
				}
			} else {
				availableDatasetName = datasetNameInfo;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return availableDatasetName;
	}

	public static DatasetVector getDefaultDatasetVector() {
		DatasetVector datasetVector = null;
		if(Application.getActiveApplication().getWorkspace().getDatasources().getCount()>0){
			Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
			for (Dataset activeDataset : activeDatasets) {
				if (activeDataset instanceof DatasetVector) {
					datasetVector = (DatasetVector) activeDataset;
					return datasetVector ;
				}
			}
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if(activeDatasources.length>0){
				Datasets datasets = activeDatasources[0].getDatasets();
				for (int i = 0; i < datasets.getCount(); i++) {
					if (datasets.get(i) instanceof DatasetVector) {
						datasetVector = (DatasetVector) datasets.get(i);
						return datasetVector ;
					}
				}
			}
			Datasets datasets = Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (datasets.get(i) instanceof DatasetVector) {
					datasetVector = (DatasetVector) datasets.get(i);
					break;
				}
			}
			return datasetVector ;
		}
		return datasetVector;
	}

	public static DatasetGrid getDefaultDatasetGrid() {
		DatasetGrid datasetGrid = null;
		if(Application.getActiveApplication().getWorkspace().getDatasources().getCount()>0){
			Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
			for (Dataset activeDataset : activeDatasets) {
				if (activeDataset instanceof DatasetGrid) {
					datasetGrid = (DatasetGrid) activeDataset;
					return datasetGrid ;
				}
			}
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if(activeDatasources.length>0){
				Datasets datasets = activeDatasources[0].getDatasets();
				for (int i = 0; i < datasets.getCount(); i++) {
					if (datasets.get(i) instanceof DatasetGrid) {
						datasetGrid = (DatasetGrid) datasets.get(i);
						return datasetGrid ;
					}
				}
			}
			Datasets datasets = Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (datasets.get(i) instanceof DatasetGrid) {
					datasetGrid = (DatasetGrid) datasets.get(i);
					break;
				}
			}
			return datasetGrid ;
		}
		return datasetGrid;
	}

	public static Dataset getDefaultBigDataStoreDataset() {
		if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
			if (activeDatasets != null) {
				for (Dataset activeDataset : activeDatasets) {
					if (activeDataset.getDatasource().getEngineType() == EngineType.DATASERVER && !activeDataset.getDatasource().isReadOnly()) {
						return activeDataset;
					}
				}
			}
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if (activeDatasources != null) {
				for (Datasource activeDatasource : activeDatasources) {
					if (activeDatasource.getEngineType() == EngineType.DATASERVER && activeDatasource.getDatasets().getCount() > 0 && !activeDatasource.isReadOnly()) {
						return activeDatasource.getDatasets().get(0);
					}
				}
			}

			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource datasource = datasources.get(i);
				if (datasource.getEngineType() == EngineType.DATASERVER && datasource.getDatasets().getCount() > 0 && !datasource.isReadOnly()) {
					return datasource.getDatasets().get(0);
				}
			}
		}
		return null;
	}

	public static Dataset getDefaultDataset() {
		if(Application.getActiveApplication().getWorkspace().getDatasources().getCount()>0){
			Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
			if(activeDatasets.length > 0){
				return Application.getActiveApplication().getActiveDatasets()[0];
			}
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if(activeDatasources.length > 0 && activeDatasources[0].getDatasets().getCount()>0){
				return activeDatasources[0].getDatasets().get(0);
			}
			Datasets datasets = Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets();
			if(datasets.getCount()>0){
				return datasets.get(0);
			}
		}
		return null;
	}
}
