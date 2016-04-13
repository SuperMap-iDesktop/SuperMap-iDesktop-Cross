package com.supermap.desktop.geometryoperation;

import java.util.ArrayList;
import java.util.List;

import com.supermap.data.DatasetType;
import com.supermap.data.GeometryType;
import com.supermap.mapping.Layer;

/**
 * 记录对象编辑功能需要知道的地图相关状态信息
 * 
 * @author highsad
 *
 */
public class EditProperties {
	private int selectedGeometryCount = 0; // 当前选中对象的数目
	private int editableSelectedGeometryCount = 0; // 获取在可编辑图层上选中的几何对象个数

	private List<DatasetType> selectedDatasetTypes = new ArrayList<>(); // 选中对象所在的数据集类型集合
	private List<DatasetType> editableDatasetTypes = new ArrayList<>(); // 可编辑的数据集类型集合
	private List<GeometryType> selectedGeometryTypes = new ArrayList<>(); // 选中的几何对象类型集合
	private List<GeometryType> editableSelectedGeometryTypes = new ArrayList<>(); // 选中的可编辑图层集合对象类型集合
	private List<Layer> selectedLayers = new ArrayList<>(); // 选中对象所在的图层
	/**
	 * 选中的几何对象特征 {@link}IGeometryFeature 集合。Class<{@link}IPointFeature>/Class<{@link}ILineFeature>/Class<{@link}IRegionFeature>等。
	 */
	private List<Class<?>> selectedGeometryFeatures = new ArrayList<>();
	/**
	 * 选中的可编辑图层几何对象 Feature集合。Class<{@link}IPointFeature>/Class<{@link}ILineFeature>/Class<{@link}IRegionFeature>等。
	 */
	private List<Class<?>> editableSelectedGeometryFeatures = new ArrayList<>();

	/**
	 * 获取选中对象的数目
	 */
	public int getSelectedGeometryCount() {
		return this.selectedGeometryCount;
	}

	/**
	 * 设置选中对象的数目，不开放
	 * 
	 * @param selectedGeometryCount
	 */
	void setSelectedGeometryCount(int selectedGeometryCount) {
		this.selectedGeometryCount = selectedGeometryCount;
	}

	/**
	 * 获取可编辑图层上选中对象的数目
	 */
	public int getEditableSelectedGeometryCount() {
		return this.editableSelectedGeometryCount;
	}

	/**
	 * 设置可编辑图层上选中对象的数目，不开放
	 * 
	 * @param editableSelectedGeometryCount
	 */
	void setEditableSelectedGeometryCount(int editableSelectedGeometryCount) {
		this.editableSelectedGeometryCount = editableSelectedGeometryCount;
	}

	/**
	 * 获取选中对象所在的数据集类型集合
	 */
	public List<DatasetType> getSelectedDatasetTypes() {
		return this.selectedDatasetTypes;
	}

	/**
	 * 获取可编辑的数据集类型集合
	 */
	public List<DatasetType> getEditableDatasetTypes() {
		return this.editableDatasetTypes;
	}

	/**
	 * 获取选中的集合对象类型集合
	 */
	public List<GeometryType> getSelectedGeometryTypes() {
		return this.selectedGeometryTypes;
	}

	/**
	 * 获取可编辑图层上选中的集合对象类型集合
	 */
	public List<GeometryType> getEditableSelectedGeometryTypes() {
		return this.editableSelectedGeometryTypes;
	}

	/**
	 * 获取选中几何对象所在图层的集合
	 */
	public List<Layer> getSelectedLayers() {
		return this.selectedLayers;
	}

	/**
	 * 获取选中的几何对象特征 {@link}IGeometryFeature 集合。Class<{@link}IPointFeature>/Class<{@link}ILineFeature>/Class<{@link}IRegionFeature>等。
	 */
	public List<Class<?>> getSelectedGeometryFeatures() {
		return this.selectedGeometryFeatures;
	}

	/**
	 * 获取选中的可编辑图层几何对象 Feature集合。Class<{@link}IPointFeature>/Class<{@link}ILineFeature>/Class<{@link}IRegionFeature>等。
	 */
	public List<Class<?>> getEditableSelectedGeometryFeatures() {
		return this.editableSelectedGeometryFeatures;
	}
}
