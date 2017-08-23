package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.data.Colors;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerHeatmap;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/7/18.
 */
public class LayerHeatmapPropertyModel extends LayerPropertyModel {

	public static final String KERNEL_RADIUS = "kernelRadius";                    // 核半径
	public static final String WEIGHT_FIELD = "weightField";                      // 权重字段
	public static final String COLOR_PLAN = "colorPlan";                          // 颜色方案
	public static final String MAX_COLOR = "maxColor";                            // 最大颜色
	public static final String MAX_COLOR_TRANSPARENCE = "maxColorTransparence";   // 最大颜色透明度
	public static final String MIN_COLOR = "minColor";                            // 最小颜色
	public static final String MIN_COLOR_TRANSPARENCE = "minColorTransparence";   // 最小颜色透明度
	public static final String FUZZY_DEGREE = "fuzzyDegree";                      // 颜色渐变模糊度
	public static final String INTENSITY = "intensity";                           // 颜色权重
	public static final String MAX_VALUE = "maxValue";                            // 最大值
	public static final String MIN_VALUE = "minValue";                            // 最小值
	public static final String IS_SYSTEM_OR_CUSTOM = "isSystemOrCustom";


	private Integer kernelRadius = 0;
	private String weightField = "";
	private Color maxColor = null;
	private Integer maxColorTransparence = 0;
	private Color minColor = null;
	private Integer minColorTransparence = 0;
	private Double fuzzyDegree = 0.0;
	private Double intensity = 0.0;
	private Double customMaxValue = Double.MIN_VALUE;
	private Double customMinValue = Double.MIN_VALUE;
	private Double systemMaxValue = 0.0;
	private Double systemMinValue = 0.0;
	private Double maxValue = 0.0;
	private Double minValue = 0.0;
	private Boolean isUserDef = false;
	private Boolean isCurrentView=false;
	private FieldInfos fieldInfos = null;
	private Colors colors = null;

	public LayerHeatmapPropertyModel() {
		// do nothing
	}

	public LayerHeatmapPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public Integer getKernelRadius() {
		return kernelRadius;
	}

	public void setKernelRadius(Integer kernelRadius) {
		this.kernelRadius = kernelRadius;
	}

	public Colors getColors() {
		return this.colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public Color getMaxColor() {
		return maxColor;
	}

	public Integer getMaxColorTransparence() {
		return maxColorTransparence;
	}

	public Color getMinColor() {
		return minColor;
	}

	public Integer getMinColorTransparence() {
		return minColorTransparence;
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

	public void setMaxColorTransparence(Integer maxColorTransparence) {
		this.maxColorTransparence = maxColorTransparence;
	}

	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public void setMinColorTransparence(Integer minColorTransparence) {
		this.minColorTransparence = minColorTransparence;
	}

	public FieldInfos getFieldInfos() {
		return fieldInfos;
	}

	public void setFieldInfos(FieldInfos fieldInfos) {
		this.fieldInfos = fieldInfos;
	}

	public String getWeightField() {
		return weightField;
	}

	public void setWeightField(String weightField) {
		this.weightField = weightField;
	}

	public Double getFuzzyDegree() {
		return fuzzyDegree;
	}

	public Double getIntensity() {
		return intensity;
	}

	public void setFuzzyDegree(Double fuzzyDegree) {
		this.fuzzyDegree = fuzzyDegree;
	}

	public void setIntensity(Double intensity) {
		this.intensity = intensity;
	}

	public Double getCustomMaxValue() {
		return customMaxValue;
	}

	public Double getCustomMinValue() {
		return customMinValue;
	}

	public Double getSystemMaxValue() {
		return systemMaxValue;
	}

	public void setSystemMaxValue(Double systemMaxValue) {
		this.systemMaxValue = systemMaxValue;
	}

	public Double getSystemMinValue() {
		return systemMinValue;
	}

	public void setSystemMinValue(Double systemMinValue) {
		this.systemMinValue = systemMinValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Boolean getIsUserDef() {
		return isUserDef;
	}

	public void setIsUserDef(Boolean userDefOrSystem) {
		isUserDef = userDefOrSystem;
	}

	public Boolean getCurrentView() {
		return isCurrentView;
	}

	public void setCurrentView(Boolean currentView) {
		isCurrentView = currentView;
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerHeatmapPropertyModel layerHeatmapPropertyModel = (LayerHeatmapPropertyModel) model;
		if (layerHeatmapPropertyModel != null) {
			this.kernelRadius = layerHeatmapPropertyModel.getKernelRadius();
			this.weightField = layerHeatmapPropertyModel.getWeightField();
			this.colors = layerHeatmapPropertyModel.getColors();
			this.maxColor = layerHeatmapPropertyModel.getMaxColor();
			this.maxColorTransparence = layerHeatmapPropertyModel.getMaxColorTransparence();
			this.minColor = layerHeatmapPropertyModel.getMinColor();
			this.minColorTransparence = layerHeatmapPropertyModel.getMinColorTransparence();
			this.fieldInfos = layerHeatmapPropertyModel.getFieldInfos();
			this.fuzzyDegree = layerHeatmapPropertyModel.getFuzzyDegree();
			this.intensity = layerHeatmapPropertyModel.getIntensity();
			this.customMaxValue = layerHeatmapPropertyModel.getCustomMaxValue();
			this.customMinValue = layerHeatmapPropertyModel.getCustomMinValue();
			this.systemMaxValue = layerHeatmapPropertyModel.getSystemMaxValue();
			this.systemMinValue = layerHeatmapPropertyModel.getSystemMinValue();
			this.maxValue=layerHeatmapPropertyModel.getMaxValue();
			this.minValue=layerHeatmapPropertyModel.getMinValue();
			this.isCurrentView=layerHeatmapPropertyModel.getCurrentView();
			this.isUserDef =layerHeatmapPropertyModel.getIsUserDef();
		}
	}

	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerHeatmapPropertyModel layerHeatmapPropertyModel = (LayerHeatmapPropertyModel) model;

		return layerHeatmapPropertyModel != null && this.kernelRadius == layerHeatmapPropertyModel.getKernelRadius()
				&& this.fuzzyDegree == layerHeatmapPropertyModel.getFuzzyDegree() && this.intensity == layerHeatmapPropertyModel.getIntensity()
				&& this.weightField.equals(layerHeatmapPropertyModel.getWeightField()) && this.colors.equals(layerHeatmapPropertyModel.getColors());
	}


	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer instanceof LayerHeatmap) {
			LayerHeatmap layerHeatmap = (LayerHeatmap) layer;

			if (this.propertyEnabled.get(KERNEL_RADIUS) && this.kernelRadius != null) {
				layerHeatmap.setKernelRadius(this.kernelRadius);
			}

			if (this.propertyEnabled.get(WEIGHT_FIELD) && this.weightField != null) {
				layerHeatmap.setWeightField(this.weightField);
			}

			if (this.propertyEnabled.get(MAX_COLOR) && this.maxColor != null) {
				layerHeatmap.setMaxColor(this.maxColor);
			}

			if (this.propertyEnabled.get(MIN_COLOR) && this.minColor != null) {
				layerHeatmap.setMinColor(this.minColor);
			}

			if (this.propertyEnabled.get(FUZZY_DEGREE) && this.fuzzyDegree != null) {
				layerHeatmap.setFuzzyDegree(this.fuzzyDegree);
			}

			if (this.propertyEnabled.get(INTENSITY) && this.intensity != null) {
				layerHeatmap.setIntensity(this.intensity);
			}

//			if (this.propertyEnabled.get(MAX_VALUE) && this.maxValue != null) {
//				layerHeatmap.setMaxValue(this.maxValue);
//			}
//
//			if (this.propertyEnabled.get(MIN_VALUE) && this.minValue != null) {
//				layerHeatmap.setMinValue(this.minValue);
//			}
			//layerHeatmap.updateData();
		}
	}

	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap();
		if (layers != null && formMap != null && formMap.getMapControl() != null && formMap.getMapControl().getMap() != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null || layer.isDisposed()) {
					break;
				}

				if (layer instanceof LayerHeatmap) {
					LayerHeatmap layerHeatmap = (LayerHeatmap) layer;
					this.kernelRadius = ComplexPropertyUtilties.union(this.kernelRadius, layerHeatmap.getKernelRadius());
					this.weightField = ComplexPropertyUtilties.union(this.weightField, layerHeatmap.getWeightField());
					this.maxColor = ComplexPropertyUtilties.union(this.maxColor, layerHeatmap.getMaxColor());
					this.maxColorTransparence = ComplexPropertyUtilties.union(this.maxColorTransparence, (int)Math.round((1.0 - this.maxColor.getAlpha() / 255.0) * 100));
					this.minColor = ComplexPropertyUtilties.union(this.minColor, layerHeatmap.getMinColor());
					this.minColorTransparence = ComplexPropertyUtilties.union(this.minColorTransparence, (int)Math.round((1.0 - this.minColor.getAlpha() / 255.0) * 100));
					this.fuzzyDegree = ComplexPropertyUtilties.union(this.fuzzyDegree, layerHeatmap.getFuzzyDegree());
					this.intensity = ComplexPropertyUtilties.union(this.intensity, layerHeatmap.getIntensity());
					this.systemMaxValue = ComplexPropertyUtilties.union(this.systemMaxValue, layerHeatmap.getInternalMaxValue());
					this.systemMinValue = ComplexPropertyUtilties.union(this.systemMinValue, layerHeatmap.getInternalMinValue());
					this.isUserDef = ComplexPropertyUtilties.union(this.isUserDef, layerHeatmap.getIsUserDef());
					DatasetVector datasetVector = (DatasetVector) layer.getDataset();
					this.fieldInfos = datasetVector.getFieldInfos();

					if (Double.compare(this.customMaxValue, Double.MIN_VALUE) == 0 && Double.compare(this.customMinValue, Double.MIN_VALUE) == 0) {
						this.customMaxValue = this.systemMaxValue;
						this.customMinValue = this.systemMinValue;
					}
				}
			}
		}
	}


	private void resetProperties() {
		this.kernelRadius = 0;
		this.weightField = "";
		this.maxColor = null;
		this.maxColorTransparence = 0;
		this.minColor = null;
		this.minColorTransparence = 0;
		this.fuzzyDegree = 0.0;
		this.intensity = 0.0;
		this.systemMaxValue = 0.0;
		this.systemMinValue = 0.0;
		this.fieldInfos = null;
		this.isUserDef =false;

		if (getLayers() != null && getLayers().length > 0) {
			this.kernelRadius = ((LayerHeatmap) getLayers()[0]).getKernelRadius();
			this.weightField = ((LayerHeatmap) getLayers()[0]).getWeightField();
			this.maxColor = ((LayerHeatmap) getLayers()[0]).getMaxColor();
			this.maxColorTransparence = (int)Math.round((1.0 - this.maxColor.getAlpha() / 255.0) * 100);
			this.minColor = ((LayerHeatmap) getLayers()[0]).getMinColor();
			this.minColorTransparence = (int)Math.round((1.0 - this.minColor.getAlpha() / 255.0) * 100);
			this.fuzzyDegree = ((LayerHeatmap) getLayers()[0]).getFuzzyDegree();
			this.intensity = ((LayerHeatmap) getLayers()[0]).getIntensity();
			this.systemMaxValue = ((LayerHeatmap) getLayers()[0]).getInternalMaxValue();
			this.systemMinValue = ((LayerHeatmap) getLayers()[0]).getInternalMinValue();
			DatasetVector datasetVector = (DatasetVector) getLayers()[0].getDataset();
			this.fieldInfos = datasetVector.getFieldInfos();
			this.isUserDef =((LayerHeatmap) getLayers()[0]).getIsUserDef();
		}

		if (Double.compare(this.customMaxValue, Double.MIN_VALUE) == 0 && Double.compare(this.customMinValue, Double.MIN_VALUE) == 0) {
			this.customMaxValue = this.systemMaxValue;
			this.customMinValue = this.systemMinValue;
		}
	}

	private void initializeEnabledMap() {
		this.propertyEnabled.put(KERNEL_RADIUS, true);
		this.propertyEnabled.put(WEIGHT_FIELD, true);
		this.propertyEnabled.put(COLOR_PLAN, true);
		this.propertyEnabled.put(MAX_COLOR, true);
		this.propertyEnabled.put(MAX_COLOR_TRANSPARENCE, true);
		this.propertyEnabled.put(MIN_COLOR, true);
		this.propertyEnabled.put(MIN_COLOR_TRANSPARENCE, true);
		this.propertyEnabled.put(FUZZY_DEGREE, true);
		this.propertyEnabled.put(INTENSITY, true);
		this.propertyEnabled.put(IS_SYSTEM_OR_CUSTOM, true);
	}
}
