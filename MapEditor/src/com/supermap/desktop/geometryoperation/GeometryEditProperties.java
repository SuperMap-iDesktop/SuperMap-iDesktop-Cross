package com.supermap.desktop.geometryoperation;

import java.util.ArrayList;
import java.util.List;

import com.supermap.data.DatasetType;
import com.supermap.data.GeometryType;

/**
 * 记录对象编辑功能需要知道的地图相关状态信息
 * 
 * @author highsad
 *
 */
public class GeometryEditProperties {
	private int selectedGeometryCount = 0; // 当前选中对象的数目
	private int editableSelectedGeometryCount = 0; // 获取在可编辑图层上选中的几何对象个数

	private List<DatasetType> selectedDatasetTypes = new ArrayList<>(); // 选中对象所在的数据集类型集合
	private List<DatasetType> editableDatasetTypes = new ArrayList<>(); // 可编辑的数据集类型集合
	private List<GeometryType> selectedGeometryTypes = new ArrayList<>(); // 选中的几何对象类型集合
	private List<GeometryType> editableSelectedGeometryTypes = new ArrayList<>(); // 选中的可编辑图层集合对象类型集合
	private List<Class<?>> selectedGeometryFeatures = new ArrayList<>(); // 选中的几何对象 Feature 集合。Class<IPointFeature>/Class<ILineFeature>/Class<IRegionFeature>等。
	private List<Class<?>> editableSelectedGeometryFeatures = new ArrayList<>(); // 选中的可编辑图层几何对象 Feature
																					// 集合。。Class<IPointFeature>/Class<ILineFeature>/Class<IRegionFeature>等。

	public int getSelectedGeometryCount() {
		return this.selectedGeometryCount;
	}

	public void setSelectedGeometryCount(int selectedGeometryCount) {
		this.selectedGeometryCount = selectedGeometryCount;
	}

	public int getEditableSelectedGeometryCount() {
		return this.editableSelectedGeometryCount;
	}

	public void setEditableSelectedGeometryCount(int editableSelectedGeometryCount) {
		this.editableSelectedGeometryCount = editableSelectedGeometryCount;
	}

	public List<DatasetType> getSelectedDatasetTypes() {
		return this.selectedDatasetTypes;
	}

	public List<DatasetType> getEditableDatasetTypes() {
		return this.editableDatasetTypes;
	}

	public List<GeometryType> getSelectedGeometryTypes() {
		return this.selectedGeometryTypes;
	}

	public List<GeometryType> getEditableSelectedGeometryTypes() {
		return this.editableSelectedGeometryTypes;
	}

	public List<Class<?>> getSelectedGeometryFeatures() {
		return this.selectedGeometryFeatures;
	}

	public List<Class<?>> getEditableSelectedGeometryFeatures() {
		return this.editableSelectedGeometryFeatures;
	}
}
