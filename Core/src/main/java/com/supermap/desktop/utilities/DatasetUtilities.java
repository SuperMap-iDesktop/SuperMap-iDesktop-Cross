package com.supermap.desktop.utilities;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.StatisticMode;
import com.supermap.data.Tolerance;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3Ds;
import com.supermap.realspace.Scene;
import com.supermap.realspace.TerrainLayers;

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

	private static boolean isIncludeDataset(Layers layers, Dataset dataset) {
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
				result = result || removeLayerGroupDataset((LayerGroup) layer, dataset);
			} else if (layer.getDataset() == dataset) {
				result = result || layerGroup.remove(layer);
			}
		}
		return result;
	}

	public static boolean removeByDataset(Layers layers, Dataset closeDataset) {
		boolean result = false;
		for (int i = layers.getCount() - 1; i >= 0; i--) {
			Layer layer = layers.get(i);
			if (layer instanceof LayerGroup) {
				result = result || removeLayerGroupDataset((LayerGroup) layer, closeDataset);
			} else if (layer.getDataset() == closeDataset) {
				result = result || layers.remove(i);
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
				result = result || removeByDataset(layers, ((DatasetVector) datasetTemp).getChildDataset());
			}
			result = result || removeByDataset(layers, datasetTemp);
		}
		return result;
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
						Map map = ((IFormMap) form).getMapControl().getMap();
						Layers layers = map.getLayers();
						if (removeByDatasets(layers, closeDataset) && Application.getActiveApplication().getMainFrame().getFormManager().isContain(((IFormMap) form))) {
							map.refresh();
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
								i--;
								formNumber--;
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

			for (int index = 0; index < newDatasetNames.length; index++) {
				datasetNames.add(newDatasetNames[index].toLowerCase());
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

	// /// <summary>
	// /// 创建矢量数据集，并采用源数据的参数设置
	// /// </summary>
	// /// <param name="srcDataset">源数据集</param>
	// /// <param name="desDatasource">目标数据源</param>
	// /// <param name="strDTName">目标数据集名称</param>
	// /// <param name="type">要创建的数据集类型</param>
	// /// <returns></returns>
	// public static DatasetVector CreatDatasetVector(DatasetVector
	// srcDataset, Datasource desDatasource, String strDTName, DatasetType
	// type)
	// {
	// DatasetVector resultDataset = null;
	//
	// try
	// {
	// DatasetVectorInfo info = new DatasetVectorInfo();
	// info.Name =
	// desDatasource.Datasets.getAvailableDatasetName(strDTName);
	// info.getType() = type;
	// //考虑一下不支持编码的数据集类型的感受 added by zengwh 2012/6/13
	// if (type == DatasetType.Point || type == DatasetType.Tabular || type
	// == DatasetType.CAD ||
	// type == DatasetType.Line3D || type == DatasetType.Point3D || type ==
	// DatasetType.Region3D)
	// {
	// info.EncodeType = EncodeType.None;
	// }
	// else
	// {
	// if (srcDataset != null)
	// {
	//
	// info.EncodeType = srcDataset.EncodeType;
	// }
	// }
	//
	// resultDataset = desDatasource.Datasets.Create(info);
	// if (resultDataset != null)
	// {
	// if (srcDataset != null)
	// {
	// resultDataset.PrjCoordSys = srcDataset.PrjCoordSys;
	// resultDataset.Charset = srcDataset.Charset;
	// // 非系统字段处理
	// FieldInfo field = new FieldInfo();
	// foreach (FieldInfo fieldinfo in srcDataset.FieldInfos)
	// {
	// if (!fieldinfo.IsSystemField)
	// {
	// if (!resultDataset.FieldInfos.Contains(fieldinfo.Caption))
	// {
	// field.Name = resultDataset.getAvailableFieldName(fieldinfo.Caption);
	// field.getType() = fieldinfo.getType();
	// field.DefaultValue = fieldinfo.DefaultValue;
	// field.IsRequired = fieldinfo.IsRequired;
	// field.MaxLength = fieldinfo.MaxLength;
	// field.Caption = fieldinfo.Caption;
	// resultDataset.FieldInfos.Add(field);
	// }
	// }
	// }
	// }
	// }
	//
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	// return resultDataset;
	// }
	//
	// private static FunctionID getFunID(DatasetType type)
	// {
	// FunctionID funID = FunctionID.None;
	// switch (type)
	// {
	// case DatasetType.CAD:
	// funID = FunctionID.DeleteCADDataset;
	// break;
	// case DatasetType.Grid:
	// funID = FunctionID.DeleteGridDataset;
	// break;
	// case DatasetType.GridCollection:
	// funID = FunctionID.DeleteGridCollectionDataset;
	// break;
	// case DatasetType.Image:
	// funID = FunctionID.DeleteImageDataset;
	// break;
	// case DatasetType.ImageCollection:
	// funID = FunctionID.DeleteImageCollectionDataset;
	// break;
	// case DatasetType.Line:
	// funID = FunctionID.DeleteLineDataset;
	// break;
	// case DatasetType.Line3D:
	// funID = FunctionID.DeleteLine3DDataset;
	// break;
	// case DatasetType.LineM:
	// funID = FunctionID.DeleteLineMDataset;
	// break;
	// case DatasetType.LinkTable:
	// funID = FunctionID.DeleteLinkTableDataset;
	// break;
	// case DatasetType.Network:
	// funID = FunctionID.DeleteNetworkDataset;
	// break;
	// case DatasetType.ParametricLine:
	// funID = FunctionID.DeleteParametricLineDataset;
	// break;
	// case DatasetType.ParametricRegion:
	// funID = FunctionID.DeleteParametricRegionDataset;
	// break;
	// case DatasetType.Point:
	// funID = FunctionID.DeletePointDataset;
	// break;
	// case DatasetType.Point3D:
	// funID = FunctionID.DeletePoint3DDataset;
	// break;
	// case DatasetType.Region:
	// funID = FunctionID.DeleteRegionDataset;
	// break;
	// case DatasetType.Region3D:
	// funID = FunctionID.DeleteRegion3DDataset;
	// break;
	// case DatasetType.Tabular:
	// funID = FunctionID.DeleteTabularDataset;
	// break;
	// case DatasetType.Text:
	// funID = FunctionID.DeleteTextDataset;
	// break;
	// case DatasetType.Topology:
	// funID = FunctionID.DeleteTopologyDataset;
	// break;
	// case DatasetType.WCS:
	// funID = FunctionID.DeleteTopologyDataset;
	// break;
	// case DatasetType.WMS:
	// funID = FunctionID.DeleteWMSDataset;
	// break;
	// }
	// return funID;
	// }
	//
	// /// <summary>
	// /// 删除指定的数据集
	// /// </summary>
	// /// <param
	// name="datasets">要删除的数据集。如果datasets中包含数据集集合对象，如影像数据集集合对象，那么将删除该集合对象中的所有子数据集。如果要使用删除数据集合中指定的数据集子项，请使用DeleteDatasetCollectionItems</param>
	// public static boolean DeleteDatasets(Dataset[] datasets)
	// {
	// boolean isDeleted = false;
	// FunctionID funID = FunctionID.None;
	// try
	// {
	// String message = String.Empty;
	// String text = String.Empty;
	//
	// message = CoreResources.String_DatasetDelete_Confirm;
	// if (datasets.Length == 1)
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_DatasetDelete_ConfirOne,
	// datasets[0].Datasource.Alias, datasets[0].Name);
	// }
	// else
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_DatasetDelete_ConfirMulti,
	// datasets.Length);
	// }
	// if (datasets[0].Datasource.IsReadOnly)
	// {
	// //只读数据源就不要弹出提示窗口了
	// message = String.format(CoreResources.String_DatasetDelete_ReadOnly,
	// datasets[0].Datasource.Alias);
	// MessageBox.Show(message, CoreResources.String_DatasetDelete,
	// MessageBoxButtons.OK, MessageBoxIcon.Error);
	// }
	// else
	// {
	// boolean isDynamicSegment = false;
	// List<Dataset> dynamicSegmentDatsets = new List<Dataset>();
	// for (int i = 0; i < datasets.Length; i++)
	// {
	// DynamicSegmentInfo[] info =
	// DynamicSegmentManager.getDynamicSegmentInfos(datasets.get(i) as
	// DatasetVector);
	// //info[0].getDatasetRole()
	// if (datasets.get(i) is DatasetVector &&
	// DynamicSegmentManager.getDynamicSegmentInfos(datasets.get(i) as
	// DatasetVector).Length > 0)
	// {
	// isDynamicSegment = true;
	// dynamicSegmentDatsets.Add(datasets.get(i));
	// }
	// }
	// if (isDynamicSegment)
	// {
	// String datasetsName = String.Empty;
	// for (int i = 0; i < dynamicSegmentDatsets.Count; i++)
	// {
	// datasetsName = datasetsName + "“" + dynamicSegmentDatsets.get(i).Name
	// + "”";
	// }
	// message =
	// String.format(CoreResources.String_IsDeleteDynamicSegmentDatasets,
	// datasetsName);
	// if (MessageBox.Show(message, CoreResources.String_MessageBox_Title,
	// MessageBoxButtons.YesNo) == DialogResult.Yes)
	// {
	// for (int i = 0; i < dynamicSegmentDatsets.Count; i++)
	// {
	// DynamicSegmentManager.RemoveDynamicSegmentInfos(dynamicSegmentDatsets.get(i)
	// as DatasetVector);
	// }
	// for (int i = datasets.Length - 1; i >= 0; i--)
	// {
	// String strAlias = datasets.get(i).Datasource.Alias;
	// String datasetName = datasets.get(i).Name;
	// funID = getFunID(datasets.get(i).getType());
	// if (datasets.get(i).Datasource.Datasets.Delete(datasets.get(i).Name))
	// {
	// message =
	// String.format(CoreResources.String_Delete_Dataset_Successed,
	// strAlias, datasetName);
	// }
	// else
	// {
	// message = String.format(CoreResources.String_Delete_Dataset_Failed,
	// strAlias, datasetName);
	//
	// }
	// Application.getActiveApplication().Output.Output(message);
	// }
	// }
	// }
	// else
	// {
	// if (MessageBox.Show(message, CoreResources.String_DatasetDelete,
	// MessageBoxButtons.OKCancel, MessageBoxIcon.Warning) ==
	// DialogResult.OK)
	// {
	// text = String.Empty;
	// for (int i = datasets.Length - 1; i >= 0; i--)
	// {
	// Dataset dataset = datasets.get(i);
	// if (dataset != null)
	// {
	// //text = dataset.Name + "@" + dataset.Datasource.Alias;
	// String strAlias = dataset.Datasource.Alias;
	// String datasetName = dataset.Name;
	// funID = getFunID(dataset.getType());
	// if (dataset.Datasource.Datasets.Delete(dataset.Name))
	// {
	// message =
	// String.format(CoreResources.String_Delete_Dataset_Successed,
	// strAlias, datasetName);
	//
	// }
	// else
	// {
	// message = String.format(CoreResources.String_Delete_Dataset_Failed,
	// strAlias, datasetName);
	//
	// }
	// Application.getActiveApplication().Output.Output(message);
	// }
	// }
	// }
	// }
	// }
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	// return isDeleted;
	// }
	//
	// /// <summary>
	// /// 删除指定数据集集合对象中的子项
	// /// </summary>
	// /// <param
	// name="dataset">指定的数据集集合对象，目前组件提供的两种数据集集合：栅格数据集集合和影像数据集集合</param>
	// /// <param name="itemAlis">集合中需要删除的子项名称</param>
	// public static boolean DeleteDatasetCollectionItems(Dataset dataset,
	// String[] itemAlias)
	// {
	// boolean result = false;
	// try
	// {
	// if (dataset != null && itemAlias != null && itemAlias.Length > 0)
	// {
	// String message = CoreResources.String_DatasetDelete_Confirm;
	// if (dataset.Datasource.IsReadOnly)
	// {
	// //只读数据源就不要弹出提示窗口了
	// message = String.format(CoreResources.String_DatasetDelete_ReadOnly,
	// dataset.Datasource.Alias);
	// MessageBox.Show(message, CoreResources.String_DatasetDelete,
	// MessageBoxButtons.OK, MessageBoxIcon.Error);
	// }
	// else
	// {
	// DatasetGridCollection gridCollection = dataset as
	// DatasetGridCollection;
	// String datasetName = dataset.Name;
	// if (gridCollection != null)
	// {
	// if (itemAlias.Length == 1)
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_MSG_GRIDCOLLECTION_ITEREMOVE,
	// datasetName, itemAlias[0]);
	// }
	// else
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_MSG_GRIDCOLLECTION_ITEMS_REMOVE,
	// datasetName, itemAlias.Length);
	// }
	// if (MessageBox.Show(message, CoreResources.String_DatasetDelete,
	// MessageBoxButtons.OKCancel, MessageBoxIcon.Warning) ==
	// DialogResult.OK)
	// {
	// result = true;
	// for (int i = 0; i < itemAlias.Length; i++)
	// {
	// if (gridCollection.Remove(itemAlias.get(i)))
	// {
	// message =
	// String.format(CoreResources.String_Delete_DatasetCollectionIteSuccessed,
	// datasetName, itemAlias.get(i));
	// }
	// else
	// {
	// result = false;
	// message =
	// String.format(CoreResources.String_Delete_DatasetCollectionIteFailed,
	// datasetName, itemAlias.get(i));
	// }
	// Application.getActiveApplication().Output.Output(message);
	// }
	// }
	// }
	// else
	// {
	// DatasetImageCollection imageCollection = dataset as
	// DatasetImageCollection;
	// if (imageCollection != null)
	// {
	// if (itemAlias.Length == 1)
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_MSG_IMAGECOLLECTION_ITEREMOVE,
	// datasetName, itemAlias[0]);
	// }
	// else
	// {
	// message = message + "\r\n" +
	// String.format(CoreResources.String_MSG_IMAGECOLLECTION_ITEMS_REMOVE,
	// datasetName, itemAlias.Length);
	// }
	// if (MessageBox.Show(message, CoreResources.String_DatasetDelete,
	// MessageBoxButtons.OKCancel, MessageBoxIcon.Warning) ==
	// DialogResult.OK)
	// {
	// result = true;
	// for (int i = 0; i < itemAlias.Length; i++)
	// {
	// if (imageCollection.Remove(itemAlias.get(i)))
	// {
	// message =
	// String.format(CoreResources.String_Delete_DatasetCollectionIteSuccessed,
	// datasetName, itemAlias.get(i));
	// }
	// else
	// {
	// result = false;
	// message =
	// String.format(CoreResources.String_Delete_DatasetCollectionIteFailed,
	// datasetName, itemAlias.get(i));
	// }
	// Application.getActiveApplication().Output.Output(message);
	// }
	// }
	// }
	// }
	//
	// }
	// }
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	// return result;
	// }
	//
	// /// <summary>
	// /// 获取不同引擎下不同数据集类型支持的空间索引类型
	// /// </summary>
	// /// <param name="datasetVector"></param>
	// /// <returns></returns>
	// public static List<SpatialIndexType>
	// getDatasetSupportSpatialIndexTypes(DatasetVector datasetVector)
	// {
	// List<SpatialIndexType> result = new List<SpatialIndexType>();
	// try
	// {
	// if (datasetVector.IsSpatialIndexTypeSupported(SpatialIndexType.None))
	// {
	// result.Add(SpatialIndexType.None);
	// }
	// if
	// (datasetVector.IsSpatialIndexTypeSupported(SpatialIndexType.RTree))
	// {
	// result.Add(SpatialIndexType.RTree);
	// }
	// if
	// (datasetVector.IsSpatialIndexTypeSupported(SpatialIndexType.QTree))
	// {
	// result.Add(SpatialIndexType.QTree);
	// }
	// if
	// (datasetVector.IsSpatialIndexTypeSupported(SpatialIndexType.MultiLevelGrid))
	// {
	// result.Add(SpatialIndexType.MultiLevelGrid);
	// }
	// if (datasetVector.IsSpatialIndexTypeSupported(SpatialIndexType.Tile))
	// {
	// result.Add(SpatialIndexType.Tile);
	// }
	// }catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	// return result;
	// }
	//
	// public static DatasetGroup getParentGroup(Dataset dataset)
	// {
	// DatasetGroup result = null;
	// if (dataset != null)
	// {
	// DatasetGroups groups = dataset.Datasource.RootGroup.ChildGroups;
	// for (int i = 0; i < groups.Count; i++)
	// {
	// if (groups.get(i).IndexOf(dataset.Name) >= 0)
	// {
	// result = groups.get(i);
	// break;
	// }
	// }
	// }
	// return result;
	// }
	// #endregion
	//
	// #region Function_Event
	//
	// #endregion
	//
	// #region Function_Private
	//
	// #endregion
	//
	// #region Event
	//
	// #endregion
	//
	// #region InterfaceMembers
	//
	// #endregion
	//
	// #region NestedTypes
	//
	// #endregion
	// }
	//
	// public static event BuildPyramidEventHandler BuildPyramidEvent;
	// public delegate void BuildPyramidEventHandler(Object sender,
	// BuildPyramidEventArgs e);
	// public class BuildPyramidEventArgs : EventArgs
	// {
	// private Dataset dataset;
	// public Dataset Dataset
	// {
	// get
	// {
	// return dataset;
	// }
	// }
	//
	// private boolean handled;
	// public boolean Handled
	// {
	// get
	// {
	// return handled;
	// }
	// set
	// {
	// handled = value;
	// }
	// }
	//
	// public BuildPyramidEventArgs(Dataset dataset)
	// {
	// dataset = dataset;
	// }
	// }
}
