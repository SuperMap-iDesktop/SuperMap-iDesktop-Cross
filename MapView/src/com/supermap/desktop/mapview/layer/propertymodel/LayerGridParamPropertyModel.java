package com.supermap.desktop.mapview.layer.propertymodel;

import java.awt.Color;

import com.supermap.data.Colors;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingGrid;
import com.supermap.mapping.Map;

public class LayerGridParamPropertyModel extends LayerPropertyModel {

	public static final String BRIGHTNESS = "brightness";
	public static final String CONTRAST = "contrast";
	public static final String COLORS = "colors";
	public static final String SPECIAL_VALUE = "specialValue";
	public static final String SPECIAL_VALUE_COLOR = "specialValueColor";
	public static final String IS_SPECIAL_VALUE_TRANSPARENT = "isSpecialValueTransparent";

	private Integer layerGridBrightness = 0;
	private Integer layerGridContrast = 0;
	private Colors layerGridColors;
	private Double layerGridSpecialValue = 0.0;
	private Color specialValueColor = Color.WHITE;
	private Boolean isSpecialValueTransparent = false;

	public LayerGridParamPropertyModel() {
		// do nothing
	}

	public LayerGridParamPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers);
	}

	public Integer getBrightness() {
		return layerGridBrightness;
	}

	public void setBrightness(Integer brightness) {
		this.layerGridBrightness = brightness;
	}

	public Integer getContrast() {
		return layerGridContrast;
	}

	public void setContrast(Integer contrast) {
		this.layerGridContrast = contrast;
	}

	public Colors getColors() {
		return layerGridColors;
	}

	public void setColors(Colors colors) {
		this.layerGridColors = colors;
	}

	public Double getSpecialValue() {
		return layerGridSpecialValue;
	}

	public void setSpecialValue(Double specialValue) {
		this.layerGridSpecialValue = specialValue;
	}

	public Color getSpecialValueColor() {
		return specialValueColor;
	}

	public void setSpecialValueColor(Color specialValueColor) {
		this.specialValueColor = specialValueColor;
	}

	public Boolean isSpecialValueTransparent() {
		return isSpecialValueTransparent;
	}

	public void setSpecialValueTransparent(Boolean isSpecialValueTransparent) {
		this.isSpecialValueTransparent = isSpecialValueTransparent;
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerGridParamPropertyModel gridParamPropertyModel = (LayerGridParamPropertyModel) model;

		if (gridParamPropertyModel != null) {
			this.layerGridBrightness = gridParamPropertyModel.getBrightness();
			this.layerGridContrast = gridParamPropertyModel.getContrast();
			this.layerGridColors = gridParamPropertyModel.getColors();
			this.layerGridSpecialValue = gridParamPropertyModel.getSpecialValue();
			this.specialValueColor = gridParamPropertyModel.getSpecialValueColor();
			this.isSpecialValueTransparent = gridParamPropertyModel.isSpecialValueTransparent();
			this.propertyEnabled = gridParamPropertyModel.propertyEnabled;
		}
	}

	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerGridParamPropertyModel gridParamPropertyModel = (LayerGridParamPropertyModel) model;

		return gridParamPropertyModel != null && this.layerGridBrightness == gridParamPropertyModel.getBrightness()
				&& this.layerGridContrast == gridParamPropertyModel.getContrast() && this.layerGridColors == gridParamPropertyModel.getColors()
				&& Double.compare(this.layerGridSpecialValue, gridParamPropertyModel.getSpecialValue()) == 0
				&& this.specialValueColor == gridParamPropertyModel.getSpecialValueColor()
				&& this.isSpecialValueTransparent == gridParamPropertyModel.isSpecialValueTransparent();
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer.getAdditionalSetting() instanceof LayerSettingGrid) {
			LayerSettingGrid setting = (LayerSettingGrid) layer.getAdditionalSetting();

			if (this.propertyEnabled.get(BRIGHTNESS) && this.layerGridBrightness != null) {
				setting.setBrightness(this.layerGridBrightness);
			}

			if (this.propertyEnabled.get(CONTRAST) && this.layerGridContrast != null) {
				setting.setContrast(this.layerGridContrast);
			}

			if (this.propertyEnabled.get(COLORS) && this.layerGridColors != null) {
				setting.setColorTable(this.layerGridColors);
			}

			if (this.propertyEnabled.get(SPECIAL_VALUE) && this.layerGridSpecialValue != null) {
				setting.setSpecialValue(this.layerGridSpecialValue);
			}

			if (this.propertyEnabled.get(SPECIAL_VALUE_COLOR) && this.specialValueColor != null) {
				setting.setSpecialValueColor(this.specialValueColor);
			}

			if (this.propertyEnabled.get(IS_SPECIAL_VALUE_TRANSPARENT) && this.isSpecialValueTransparent != null) {
				setting.setSpecialValueTransparent(this.isSpecialValueTransparent);
			}
		}
	}

	private void initializeProperties(Layer[] layers) {
		resetProperties();
		initializeEnabledMap();

		for (Layer layer : layers) {
			if (layer == null) {
				break;
			}

			LayerSettingGrid setting = (LayerSettingGrid) layer.getAdditionalSetting();
			this.layerGridBrightness = ComplexPropertyUtilties.union(this.layerGridBrightness, setting.getBrightness());
			this.layerGridContrast = ComplexPropertyUtilties.union(this.layerGridContrast, setting.getContrast());
			this.layerGridColors = ComplexPropertyUtilties.union(this.layerGridColors, setting.getColorTable());
			this.layerGridSpecialValue = ComplexPropertyUtilties.union(this.layerGridSpecialValue, setting.getSpecialValue());
			this.specialValueColor = ComplexPropertyUtilties.union(this.specialValueColor, setting.getSpecialValueColor());
			this.isSpecialValueTransparent = ComplexPropertyUtilties.union(this.isSpecialValueTransparent, setting.isSpecialValueTransparent());
		}
	}

	// 使用指定的 Layer 初始化值
	private void resetProperties() {
		this.layerGridBrightness = 0;
		this.layerGridContrast = 0;
		this.layerGridSpecialValue = 0.0;
		this.specialValueColor = Color.WHITE;
		this.isSpecialValueTransparent = false;

		if (getLayers() != null && getLayers().length > 0) {
			this.layerGridBrightness = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).getBrightness();
			this.layerGridContrast = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).getContrast();
			this.layerGridSpecialValue = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).getSpecialValue();
			this.specialValueColor = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).getSpecialValueColor();
			this.isSpecialValueTransparent = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).isSpecialValueTransparent();
			this.layerGridColors = ((LayerSettingGrid) getLayers()[0].getAdditionalSetting()).getColorTable();
		}
	}

	private void initializeEnabledMap() {
		this.propertyEnabled.put(BRIGHTNESS, true);
		this.propertyEnabled.put(CONTRAST, true);
		this.propertyEnabled.put(COLORS, true);
		this.propertyEnabled.put(SPECIAL_VALUE, true);
		this.propertyEnabled.put(SPECIAL_VALUE_COLOR, true);
		this.propertyEnabled.put(IS_SPECIAL_VALUE_TRANSPARENT, true);
	}
}
