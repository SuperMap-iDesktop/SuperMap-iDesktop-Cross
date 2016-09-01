package com.supermap.desktop.utilities;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoStyle3D;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometry;
import com.supermap.data.Geometry3D;
import com.supermap.data.Point2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MapUtilities {
	private MapUtilities() {
		// 工具类不提供构造函数
	}

	/**
	 * 获取MapControl
	 *
	 * @return
	 */
	public static MapControl getMapControl() {
		MapControl mapControl = null;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (null != formMap.getMapControl()) {
				mapControl = formMap.getMapControl();
			}
		}
		return mapControl;
	}

	/**
	 * 获取当前地图
	 *
	 * @return
	 */
	public static Map getActiveMap() {
		return getMapControl().getMap();
	}

	/**
	 * 通过图层表达式获取当前地图
	 *
	 * @param map
	 * @param caption
	 * @return
	 */
	public static Layer findLayerByCaption(Map map, String caption) {
		Layer resultLayer = null;
		try {
			Layers layers = map.getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer tempLayer = layers.get(i);
				resultLayer = getLayer(tempLayer, caption);
				if (null != resultLayer) {
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultLayer;
	}

	/**
	 * 递归实现通过标题查找专题图图层
	 *
	 * @param layer   当前查找图层
	 * @param caption 目标图层标题
	 * @return 目标图层
	 */
	public static Layer getLayer(Layer layer, String caption) {
		Layer resultLayer = null;
		if (layer instanceof LayerGroup) {
			LayerGroup tempLayer = (LayerGroup) layer;
			for (int i = 0; i < tempLayer.getCount(); i++) {
				resultLayer = getLayer(tempLayer.get(i), caption);
				if (null != resultLayer) {
					break;
				}
			}
		} else if (caption.equals(layer.getCaption())) {
			resultLayer = layer;
		}
		return resultLayer;
	}

	/**
	 * 通过给定的map和图层名称得到指定的图层
	 *
	 * @param map  当前查找的地图
	 * @param name 目标图层名称
	 * @return 目标图层
	 */
	public static Layer findLayerByName(Map map, String name) {
		Layer layer = null;
		try {
			if (null != name && null != map) {
				Layers layers = map.getLayers();
				for (int i = 0; i < layers.getCount(); i++) {
					Layer tempLayer = layers.get(i);
					layer = findLayer(tempLayer, name);
					if (null != layer) {
						break;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return layer;
	}

	/**
	 * 递归获取指定名称的layer
	 *
	 * @param layer 当前查找图层
	 * @param name  目标图层名称
	 * @return 目标图层
	 */
	public static Layer findLayer(Layer layer, String name) {
		Layer resultLayer = null;
		if (layer instanceof LayerGroup) {
			LayerGroup tempLayerGroup = (LayerGroup) layer;
			for (int i = 0; i < tempLayerGroup.getCount(); i++) {
				resultLayer = findLayer(tempLayerGroup.get(i), name);
				if (null != resultLayer) {
					break;
				}
			}
		} else if (name.equals(layer.getName())) {
			resultLayer = layer;
		}
		return resultLayer;
	}

	public static boolean removeLayer(Map map, String name) {
		boolean result = false;
		try {
			Layer layer = findLayerByName(map, name);
			if (layer.getParentGroup() == null) {
				result = map.getLayers().remove(name);
			} else {
				layer.getParentGroup().remove(layer);
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	/**
	 * 获取地图的所有子图层
	 *
	 * @param map
	 * @return
	 */
	public static ArrayList<Layer> getLayers(Map map) {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		try {
			for (int i = 0; i < map.getLayers().getCount(); i++) {
				Layer layer = map.getLayers().get(i);
				if (layer instanceof LayerGroup) {
					layers.addAll(getLayers((LayerGroup) layer));
				} else {
					layers.add(layer);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return layers;
	}

	/**
	 * 获取地图的所有子图层
	 *
	 * @param map
	 * @param isCoverGroup 是否包含图层分组
	 * @return
	 */
	public static ArrayList<Layer> getLayers(Map map, boolean isCoverGroup) {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		try {
			for (int i = 0; i < map.getLayers().getCount(); i++) {
				Layer layer = map.getLayers().get(i);
				if (layer instanceof LayerGroup) {
					layers.addAll(getLayers((LayerGroup) layer, isCoverGroup));
					if (isCoverGroup) {
						layers.add(layer);
					}
				} else {
					layers.add(layer);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return layers;
	}

	/**
	 * 获取图层分组的所有子图层
	 *
	 * @param layerGroup
	 * @return
	 */
	public static ArrayList<Layer> getLayers(LayerGroup layerGroup) {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		try {
			for (int i = 0; i < layerGroup.getCount(); i++) {
				Layer layer = layerGroup.get(i);
				if (layer instanceof LayerGroup) {
					layers.addAll(getLayers((LayerGroup) layer));
				} else {
					layers.add(layer);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return layers;
	}

	/**
	 * 获取图层分组的所有子图层
	 *
	 * @param layerGroup
	 * @param isCoverGroup 是否包含图层分组
	 * @return
	 */
	public static ArrayList<Layer> getLayers(LayerGroup layerGroup, boolean isCoverGroup) {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		try {
			for (int i = 0; i < layerGroup.getCount(); i++) {
				Layer layer = layerGroup.get(i);
				if (layer instanceof LayerGroup) {
					layers.addAll(getLayers((LayerGroup) layer, isCoverGroup));
					if (isCoverGroup) {
						layers.add(layer);
					}
				} else {
					layers.add(layer);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return layers;
	}

	// 有点问题，不是根图层时返回空字符串
	// /**
	// * 获取可用的图层的标题
	// *
	// * @param map
	// * 地图
	// * @param parent
	// * 所在的分组，如果是根图层，则为null
	// * @param caption
	// * 指定的图层标题
	// * @return
	// */
	// public static String getAvailableLayerCaption(Map map, LayerGroup parent, String caption) {
	// String layerCaption = "";
	//
	// try {
	// if (parent == null) {
	// layerCaption = map.getLayers().getAvailableCaption(caption);
	// }
	// } catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	//
	// return layerCaption;
	// }
	public static void setDynamic(Dataset[] datasets, Map map) {

		if (map.getLayers().getCount() == 0 && datasets.length == 1) {
			// 打开新地图时，如果只有一个数据集添加上来，不需要设置动态投影
			return;
		} else {
			// 其他情况下都需要判断是否设置动态投影
			resetDynamic(datasets, map);
		}
	}

	private static boolean resetDynamic(Dataset[] datasets, Map map) {
		// 设置动态投影
		boolean dynamicHasReset = false;
		for (Dataset dataset : datasets) {
			if (!map.isDynamicProjection() && !dataset.getPrjCoordSys().getType().equals(map.getPrjCoordSys().getType())) {
				if (JOptionPane.OK_OPTION == JOptionPaneUtilities.showConfirmDialog(CoreProperties.getString("String_DiffrentCoordSys"),
						CoreProperties.getString("String_TitleCoordSys"))) {
					map.setDynamicProjection(true);
					dynamicHasReset = true;
					break;
				} else {
					dynamicHasReset = true;
					break;
				}
			}
		}
		return dynamicHasReset;
	}

	/**
	 * 添加指定数据集到地图中
	 *
	 * @param map
	 * @param dataset
	 * @param addToHead
	 * @return
	 */
	public static Layer addDatasetToMap(Map map, Dataset dataset, boolean addToHead) {
		Layer layer = null;
		try {
			if (dataset != null) {
				layer = map.getLayers().add(dataset, addToHead);
				if (layer == null || layer.isDisposed()) {
					Application.getActiveApplication().getOutput()
							.output(String.format(CoreProperties.getString("String_DatasetOpenFaild"), dataset.getName()));
				} else {
					if (dataset.getType() == DatasetType.REGION || dataset.getType() == DatasetType.REGION3D
							|| dataset.getType() == DatasetType.PARAMETRICREGION) {
						LayerSettingVector setting = (LayerSettingVector) layer.getAdditionalSetting();
						setting.getStyle().setFillForeColor(GeoStyleUtilities.getFillColor());
						setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
					} else if (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D
							|| dataset.getType() == DatasetType.PARAMETRICLINE || dataset.getType() == DatasetType.LINEM
							|| dataset.getType() == DatasetType.LINE3D) {
						LayerSettingVector setting = (LayerSettingVector) layer.getAdditionalSetting();
						setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
						if (dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D) {
							map.getLayers().add(((DatasetVector) dataset).getChildDataset(), true);
						}
					} else if (dataset.getType() == DatasetType.POINT || dataset.getType() == DatasetType.POINT3D) {
						LayerSettingVector setting = (LayerSettingVector) layer.getAdditionalSetting();
						setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
					}
					if (layer.getSelection() != null) {
						layer.getSelection().setDefaultStyleEnabled(true);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return layer;
	}

	// 直接添加到LayerGroup中在对网络数据集操作时子数据集图层没有移动
	// /**
	// * 添加指定数据集到地图中
	// *
	// * @param map
	// * @param dataset
	// * @param parent
	// * @param addToHead
	// * @return
	// */
	// public static Layer addDatasetToMap(Map map, Dataset dataset, LayerGroup parent, boolean addToHead) {
	// Layer layer = null;
	// try {
	// if (dataset != null) {
	// layer = addDatasetToMap(map, dataset, addToHead);
	//
	// // 将图层移动到分组中
	// if (parent != null) {
	// parent.add(layer);
	// }
	// }
	// } catch (Exception ex) {
	// Application.getActiveApplication().getOutput().output(ex);
	// }
	//
	// return layer;
	// }

	/*
	 * 没有使用 public static void AddLayer(Map map, LayerGroup parent, Layer layer) { try { if (parent == null) { map.getLayers().add(layer); } else {
	 * parent.add(layer); } } catch (Exception ex) { Application.getActiveApplication().getOutput().output(ex); } }
	 */

	public static void deleteMaps(String... mapNames) {
		try {
			String message = CoreProperties.getString("String_MapDelete_Confirm");
			if (mapNames.length == 1) {
				message = message + "\r\n" + String.format(CoreProperties.getString("String_MapDelete_Confirm_One"), mapNames[0]);
			} else {
				message = message + "\r\n" + String.format(CoreProperties.getString("String_MapDelete_Confirm_Multi"), mapNames.length);
			}
			if (JOptionPaneUtilities.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
				for (String mapName : mapNames) {
					IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
					for (int i = formManager.getCount() - 1; i >= 0; i--) {
						if (formManager.get(i) instanceof IFormMap && formManager.get(i).getText().equals(mapName)) {
							formManager.close(formManager.get(i));
						}
					}
					Application.getActiveApplication().getWorkspace().getMaps().remove(mapName);
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 判断保存地图时名称是否可用
	 *
	 * @param newMapName 新地图名称
	 * @param oldMapName 原来的地图名称
	 * @return true-可用 false-已存在
	 */
	public static boolean checkAvailableMapName(String newMapName, String oldMapName) {
		boolean flag = false;
		String newAvailableMapName = newMapName.toLowerCase();
		String oldAvailableMapName = oldMapName.toLowerCase();
		if (newAvailableMapName == null || newAvailableMapName.length() <= 0) {
			flag = false;
		} else {
			ArrayList<String> allMapNames = new ArrayList<String>();
			int mapsCount = Application.getActiveApplication().getWorkspace().getMaps().getCount();
			for (int index = 0; index < mapsCount; index++) {
				allMapNames.add(Application.getActiveApplication().getWorkspace().getMaps().get(index).toLowerCase());
			}
			int formManagerCount = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
			for (int index = 0; index < formManagerCount; index++) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
				if (form.getWindowType() == WindowType.MAP && !form.getText().equalsIgnoreCase(oldAvailableMapName)) {
					allMapNames.add(form.getText().toLowerCase());
				}
			}
			if (!allMapNames.contains(newAvailableMapName)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取具有指定前缀的可用地图名称
	 *
	 * @param mapName     地图名称前缀
	 * @param isNewWindow 是否为新窗体
	 * @return
	 */
	public static String getAvailableMapName(String mapName, boolean isNewWindow) {
		// 1、获取被检查的地图名称
		String mapNmaeTemp = mapName;
		String availableMapName = mapNmaeTemp.toLowerCase();
		if (mapNmaeTemp == null || mapNmaeTemp.length() <= 0) {
			mapNmaeTemp = CoreProperties.getString("String_WorkspaceNodeCaptionMap");
		}

		ArrayList<String> allMapNames = new ArrayList<String>();
		int mapsCount = Application.getActiveApplication().getWorkspace().getMaps().getCount();
		for (int index = 0; index < mapsCount; index++) {
			allMapNames.add(Application.getActiveApplication().getWorkspace().getMaps().get(index).toLowerCase());
		}
		int formManagerCount = Application.getActiveApplication().getMainFrame().getFormManager().getCount();
		for (int index = 0; index < formManagerCount; index++) {
			IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
			if (form.getWindowType() == WindowType.MAP) {
				// 对于新窗口，不能用地图名字作为窗口标题，地图名和窗口标题可能会不一样，直接使用窗口标题
				if (isNewWindow) {
					allMapNames.add(form.getText().toLowerCase());
				} else if (!"".equalsIgnoreCase(form.getText())) {
					allMapNames.add(form.getText().toLowerCase());
				}
			}
		}

		// 3、检查已有的地图名称中是否包含了被检查的地图名称，如果包含则处理一下
		if (!allMapNames.contains(availableMapName)) {
			availableMapName = mapName;
		} else {
			int indexMapName = 1;
			while (true) {
				availableMapName = String.format("%s%d", mapName, indexMapName);
				if (!allMapNames.contains(availableMapName.toLowerCase())) {
					break;
				}

				indexMapName += 1;
			}
		}
		return availableMapName;
	}

	/**
	 * 根据数据集查找非标签专题图的图层
	 *
	 * @param map     查找的地图
	 * @param dataset 结果数据集
	 * @return 找到的图层
	 */
	public static Layer findLayerByDatasetWithoutLabelTheme(Map map, Dataset dataset) {
		ArrayList<Layer> layers = MapUtilities.getLayers(map);
		for (Layer layer : layers) {
			if (layer != null && layer.getDataset() == dataset) {
				if (layer.getTheme() == null || !(layer.getTheme() instanceof ThemeLabel)) {
					return layer;
				}
			}
		}
		return null;
	}

	/**
	 * 计算一个屏幕像素在地图上的长度
	 *
	 * @param mapControl
	 * @return
	 */
	public static double pixelLength(MapControl mapControl) {
		Point2D pnt1 = mapControl.getMap().pixelToMap(new Point(1, 0));
		Point2D pnt2 = mapControl.getMap().pixelToMap(new Point(0, 0));
		double x = Math.abs(pnt1.getX() - pnt2.getX());
		double y = Math.abs(pnt1.getY() - pnt2.getY());
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * 移除 map 的 TrackingLayer 上指定 tag 的所有对象
	 *
	 * @param map
	 * @param tag
	 */
	public static void clearTrackingObjects(Map map, String tag) {
		if (map != null && !StringUtilities.isNullOrEmpty(tag)) {
			TrackingLayer trackingLayer = map.getTrackingLayer();

			int index = trackingLayer.indexOf(tag);
			while (index >= 0) {
				trackingLayer.remove(index);
				index = trackingLayer.indexOf(tag);
			}
		}
	}

	/**
	 * 获取高亮的对象
	 *
	 * @param geometry 需要高亮的对象
	 * @return 高亮风格的对象
	 */
	public static Geometry getHeightGeometry(Geometry geometry) {
		geometry = geometry.clone();
		if (geometry instanceof Geometry3D) {
			if (!(geometry instanceof GeoText3D)) {
				GeoStyle3D geoStyle3D = new GeoStyle3D();
				geoStyle3D.setLineColor(Color.RED);
				geoStyle3D.setLineWidth(1);
				((Geometry3D) geometry).setStyle3D(geoStyle3D);
			}
		} else {
			if (!(geometry instanceof GeoText)) {
				GeoStyle geoStyle = new GeoStyle();
				geoStyle.setLineColor(Color.RED);
				geoStyle.setLineWidth(1);
				geometry.setStyle(geoStyle);
			}
		}
		return geometry;
	}


}
