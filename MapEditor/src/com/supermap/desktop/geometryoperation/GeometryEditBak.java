package com.supermap.desktop.geometryoperation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLine3D;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.MapControl;

public class GeometryEditBak {
	private IFormMap formMap;
	GeoStyle trackingStyle;
	int selectedGeometryCount; // 当前选中对象的数目
	int editableSelectedGeometryCount; // 获取在可编辑图层上选中的几何对象个数

	List<DatasetType> selectedDatasetTypes; // 选中对象所在的数据集类型集合
	List<DatasetType> editableDatasetTypes; // 可编辑的数据集类型集合
	List<GeometryType> selectedGeometryTypes; // 选中的几何对象类型集合
	List<Class<?>> selectedGeometryFeatures; // 选中的几何对象 Feature 集合。Class<IPointFeature>/Class<ILineFeature>/Class<IRegionFeature>等。
	List<Class<?>> editableSelectedGeometryFeatures; // 选中的可编辑图层几何对象 Feature 集合。。Class<IPointFeature>/Class<ILineFeature>/Class<IRegionFeature>等。
	List<GeometryType> editableSelectedGeometryTypes; // 选中的可编辑图层集合对象类型集合

	private EventListenerList listenerList = new EventListenerList();

	private GeometryEditBak(IFormMap formMap) {
		this.formMap = formMap;
		this.selectedDatasetTypes = new ArrayList<DatasetType>();
		this.editableDatasetTypes = new ArrayList<DatasetType>();
		this.selectedGeometryTypes = new ArrayList<GeometryType>();
		this.selectedGeometryFeatures = new ArrayList<Class<?>>();
		this.editableSelectedGeometryFeatures = new ArrayList<Class<?>>();
		this.editableSelectedGeometryTypes = new ArrayList<GeometryType>();
		this.selectedGeometryCount = 0;
		this.editableSelectedGeometryCount = 0;

		if (this.formMap != null) {

			// 选中对象状态改变
			this.formMap.getMapControl().addGeometrySelectChangedListener(new GeometrySelectChangedListener() {

				@Override
				public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
					geometryStatusChange();
				}
			});

			// 图层可编辑状态改变
			this.formMap.getMapControl().getMap().getLayers().addLayerEditableChangedListener(new LayerEditableChangedListener() {

				@Override
				public void editableChanged(LayerEditableChangedEvent arg0) {
					layersStatusChange();
				}
			});
		}
	}

	/**
	 * 获取所有图层
	 * 
	 * @return
	 */
	public List<Layer> getAllLayers() {
		return MapUtilties.getLayers(this.formMap.getMapControl().getMap());
	}

	public IFormMap getFormMap() {
		return this.formMap;
	}

	public MapControl getMapControl() {
		return this.formMap.getMapControl();
	}

	public Map getMap() {
		return this.formMap.getMapControl().getMap();
	}

	public Layer getActiveEditableLayer() {
		return this.formMap.getMapControl().getActiveEditableLayer();
	}

	/**
	 * 创建一个实例，由此可以保证实例中的 formMap 必定有效
	 * 
	 * @param formMap
	 * @return
	 */
	public static GeometryEditBak createInstance(IFormMap formMap) {
		if (formMap == null) {
			throw new IllegalArgumentException("formMap can not be null.");
		}

		return new GeometryEditBak(formMap);
	}

	public GeoStyle getTrackingStyle() {
		if (this.trackingStyle == null) {
			this.trackingStyle = new GeoStyle();
			this.trackingStyle.setLineSymbolID(2);
			this.trackingStyle.setFillOpaqueRate(0);
		}
		return this.trackingStyle;
	}

	public int getSelectedGeometryCount() {
		return this.selectedGeometryCount;
	}

	public List<DatasetType> getselectedDatasetTypes() {
		return this.selectedDatasetTypes;
	}

	public List<DatasetType> getEditableDatasetTypes() {
		return this.editableDatasetTypes;
	}

	public List<GeometryType> getSelectedGeometryTypes() {
		return this.editableSelectedGeometryTypes;
	}

	/**
	 * 获取当前地图窗口中，所需要的状态数据
	 */
	private void geometryStatusChange() {
		try {
			// 选中对象数目
			resetGeometryStatus();

			Layers layers = this.formMap.getMapControl().getMap().getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer layer = layers.get(i);

				if (layer.getDataset() == null) {
					continue;
				}

				if (layer.getSelection() == null || layer.getSelection().getCount() == 0) {
					continue;
				}

				this.selectedDatasetTypes.add(layer.getDataset().getType());
				statisticGeometryData(layer);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 统计指定图层 Geometry 的状态
	 * 
	 * @param layer
	 */
	private void statisticGeometryData(Layer layer) {
		Recordset recordset = null;

		try {
			ArrayList<Class<?>> features = new ArrayList<>();
			ArrayList<GeometryType> types = new ArrayList<>();

			Selection selection = layer.getSelection();
			recordset = selection.toRecordset();

			recordset.moveFirst();
			while (!recordset.isEOF()) {
				Geometry geometry = recordset.getGeometry();

				if (geometry == null) {
					recordset.moveNext();
				}

				try {
					GeometryType type = geometry.getType();
					if (!types.contains(type)) {
						types.add(type);
					}

					IGeometry dGeometry = DGeometryFactory.create(geometry);
					if (dGeometry instanceof IPointFeature && !features.contains(IPointFeature.class)) {
						features.add(IPointFeature.class);
					} else if (dGeometry instanceof ILineFeature && !features.contains(ILineFeature.class)) {
						features.add(ILineFeature.class);
					} else if (dGeometry instanceof IRegionFeature && !features.contains(IRegionFeature.class)) {
						features.add(IRegionFeature.class);
					} else if (dGeometry instanceof ITextFeature && !features.contains(ITextFeature.class)) {
						features.add(ITextFeature.class);
					} else if (dGeometry instanceof ICompoundFeature && !features.contains(ICompoundFeature.class)) {
						features.add(ICompoundFeature.class);
					}
				} finally {
					if (geometry != null) {
						geometry.dispose();
					}
				}
				recordset.moveNext();
			}

			this.selectedGeometryCount += selection.getCount();
			this.selectedGeometryFeatures.addAll(features);
			this.selectedGeometryTypes.addAll(types);
			if (layer.isEditable()) {
				this.editableSelectedGeometryCount += selection.getCount();
				this.editableSelectedGeometryFeatures.addAll(features);
				this.editableSelectedGeometryTypes.addAll(types);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private void resetGeometryStatus() {
		// 选中对象数目
		this.selectedGeometryCount = 0;
		this.editableSelectedGeometryCount = 0;
		this.selectedDatasetTypes.clear();
		this.selectedGeometryTypes.clear();
		this.selectedGeometryFeatures.clear();
		this.editableSelectedGeometryFeatures.clear();
		this.editableSelectedGeometryTypes.clear();
	}

	private void layersStatusChange() {
		try {
			resetLayersStatus();

			for (int i = 0; i < this.formMap.getMapControl().getEditableLayers().length; i++) {
				Layer layer = this.formMap.getMapControl().getEditableLayers()[i];
				if (layer.getDataset() != null) {
					this.editableDatasetTypes.add(layer.getDataset().getType());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void resetLayersStatus() {
		this.editableDatasetTypes.clear();
	}

	public boolean isEraseEnable() {
		return this.editableSelectedGeometryCount > 0
				&& ListUtilties.isListContainAny(this.editableSelectedGeometryFeatures, ILineFeature.class, IRegionFeature.class, ICompoundFeature.class);
	}

	public boolean isEraseOutPartEnable() {
		return this.selectedGeometryCount == 1 && ListUtilties.isListContainAny(this.selectedGeometryFeatures, IRegionFeature.class, ICompoundFeature.class);
	}

	public boolean isEditLineMEnable() {
		return this.editableSelectedGeometryCount == 1 && ListUtilties.isListContainAny(this.selectedGeometryFeatures, GeoLineM.class);
	}

	public boolean isDeleteLineMvalueEnable() {
		return this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.selectedGeometryFeatures, GeoLineM.class);

	}

	public boolean isSmoothEnable() {
		return ListUtilties.isListContainAny(this.editableSelectedGeometryTypes, GeometryType.GEOLINE,
		// GeometryType.GeoLineM,
				GeometryType.GEOREGION); // 排除参数化曲线
	}

	public boolean isUnionEnable() {
		Boolean enable = false;
		if (this.selectedGeometryCount > 1 && // 选中数至少2个
				// (this.has2DGeometrySelected != this.has3DGeometrySelected) && // 不能即有二维对象又有三维对象
				ListUtilties.isListOnlyContain(this.selectedGeometryFeatures, IRegionFeature.class, ILineFeature.class))// 只支持面
		{
			// 是会否存在可编辑的“可操作保存”图层
			DatasetType datasetType = DatasetType.CAD;
			if (this.selectedGeometryTypes.get(0) == GeometryType.GEOCIRCLE3D || this.selectedGeometryTypes.get(0) == GeometryType.GEOPIE3D
					|| this.selectedGeometryTypes.get(0) == GeometryType.GEOREGION3D) {
				datasetType = DatasetType.REGION3D;
			} else {
				datasetType = DatasetType.REGION;
			}

			if (this.editableDatasetTypes.size() > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, datasetType)) {
				enable = true;
			}
		}
		return enable;
	}

	public boolean isSplitByRegionEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isSplitByLineEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isSplitByGeometryEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isRotateEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListOnlyContain(this.editableSelectedGeometryFeatures, ICompoundFeature.class,
				ITextFeature.class, IPointFeature.class, IRegionFeature.class, ILineFeature.class));
	}

	public boolean isReshapeEnable() {
		return (this.editableSelectedGeometryCount == 1 && ListUtilties.isListOnlyContain(this.editableSelectedGeometryFeatures, ICompoundFeature.class));
	}

	public boolean isResampleEnable() {
		return ListUtilties.isListOnlyContain(this.editableSelectedGeometryTypes, GeometryType.GEOLINE, GeometryType.GEOREGION);
	}

	public boolean isOffsetEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.REGION, DatasetType.CAD);
	}

	// public boolean isMirrorEnable() {
	// return (this.editableSelectedGeometryCount > 0 && !ListUtilties.isListContainAny(this.editableSelectedGeometryKinds, ITextFeature.class,
	// GeometryKind.Other));
	// }

	public boolean isPartialUpdateEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.REGION, DatasetType.LINE);
	}

	public boolean isMoveObjEnable() {
		return (this.editableSelectedGeometryCount > 0);
	}

	public boolean isPointsAvgErrorEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.CAD);
	}

	public boolean isLineReverseEnable() {
		return ListUtilties.isListContainAny(this.editableSelectedGeometryTypes, GeometryType.GEOLINE, GeometryType.GEOLINEM);
	}

	public boolean isFilletEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 2) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				if (layer.isEditable()
						&& layer.getSelection().getCount() == 2
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.LINEM)) {
					recordset = layer.getSelection().toRecordset();
					GeoLine geometry1 = (GeoLine) recordset.getGeometry();
					recordset.moveNext();
					GeoLine geometry2 = (GeoLine) recordset.getGeometry();
					if (geometry1 != null && geometry2 != null && geometry1.getPartCount() == 1 && geometry1.getPart(0).getCount() == 2
							&& geometry2.getPartCount() == 1 && geometry2.getPart(0).getCount() == 2) {
						result = true;
					}
					if (geometry1 != null) {
						geometry1.dispose();
						geometry1 = null;
					}
					if (geometry2 != null) {
						geometry2.dispose();
						geometry2 = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isDecomposeEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 1) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				recordset = layer.getSelection().toRecordset();
				Geometry geometry = recordset.getGeometry();
				if (geometry != null) {
					if (geometry.getType() == GeometryType.GEOCOMPOUND) {
						result = true;
					} else if (geometry.getType() == GeometryType.GEOLINE) {
						result = (((GeoLine) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOLINE3D) {
						result = (((GeoLine3D) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOREGION) {
						result = (((GeoRegion) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOREGION3D) {
						result = (((GeoRegion3D) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOLINEM) {
						result = (((GeoLineM) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOTEXT) {
						result = (((GeoText) geometry).getPartCount() > 1);
					} else {
						// 其它类型默认不做处理
					}
					if (geometry != null) {
						geometry.dispose();
						geometry = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isCombinationEnable() {
		Boolean result = false;
		try {
			if (this.selectedGeometryCount > 1) {
				if (this.selectedDatasetTypes.size() > 1)// 多种数据集时，目标要为CAD
				{
					if (this.editableDatasetTypes.contains(DatasetType.CAD)) {
						result = true;
					}
				} else if (this.selectedDatasetTypes.size() == 1) // 只有一种时，目标相同或为CAD
				{
					if (!(this.selectedDatasetTypes.get(0) == DatasetType.POINT || this.selectedDatasetTypes.get(0) == DatasetType.POINT3D)) {
						if (this.editableDatasetTypes.contains(DatasetType.CAD) || this.editableDatasetTypes.contains(this.selectedDatasetTypes.get(0))) {
							result = true;
						}
					}
				} else {
					// 不做任何处理
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean isClipRegionByRegionEnable() {
		return (this.editableDatasetTypes.size() == 1 && this.editableDatasetTypes.get(0) == DatasetType.REGION);
	}

	public boolean isExplodeLineEnable() {
		Boolean result = false;
		try {
			if (this.editableSelectedGeometryCount > 0) {
				result = this.editableSelectedGeometryFeatures.contains(ILineFeature.class);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean isJointLineEnable() {
		Boolean result = false;
		if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.LINE) && this.selectedGeometryCount > 1) {
			if (ListUtilties.isListOnlyContain(this.selectedGeometryFeatures, ILineFeature.class)) {
				result = true;
			}
		}
		return result;
	}

	public boolean isCopyObjEnable() {
		return (this.editableSelectedGeometryCount > 0);
	}

	public boolean isHolyRegionEnable() {
		Boolean result = false;
		if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.REGION) && this.selectedGeometryCount > 1) {
			if (ListUtilties.isListOnlyContain(this.selectedGeometryFeatures, IRegionFeature.class)) {
				result = true;
			}
		}
		return result;
	}

	public boolean isChamferEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 2) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				if (layer != null
						&& layer.isEditable()
						&& layer.getSelection().getCount() == 2
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.LINEM)) {
					recordset = layer.getSelection().toRecordset();
					GeoLine geometry1 = (GeoLine) recordset.getGeometry();
					recordset.moveNext();
					GeoLine geometry2 = (GeoLine) recordset.getGeometry();
					if (geometry1 != null && geometry2 != null && geometry1.getPartCount() == 1 && geometry1.getPart(0).getCount() == 2
							&& geometry2.getPartCount() == 1 && geometry2.getPart(0).getCount() == 2) {
						result = true;
					}
					if (geometry1 != null) {
						geometry1.dispose();
						geometry1 = null;
					}
					if (geometry2 != null) {
						geometry2.dispose();
						geometry2 = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isBreakEnable() {
		Boolean enable = false;
		try {
			if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.LINEM, DatasetType.CAD)
					&& ListUtilties.isListOnlyContain(this.selectedGeometryTypes, GeometryType.GEOLINE)) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return enable;
	}
}
