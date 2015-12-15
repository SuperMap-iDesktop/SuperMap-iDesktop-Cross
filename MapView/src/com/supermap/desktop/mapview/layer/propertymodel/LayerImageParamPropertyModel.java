package com.supermap.desktop.mapview.layer.propertymodel;

import java.awt.Color;
import java.util.HashMap;

import com.supermap.data.ColorSpaceType;
import com.supermap.data.DatasetImage;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingImage;
import com.supermap.mapping.Map;

public class LayerImageParamPropertyModel extends LayerPropertyModel {

	public static final String BRIGHTNESS = "brightness";
	public static final String CONTRAST = "contrast";
	public static final String TRANSPARENT_COLOR = "transparentColor";
	public static final String IS_TRANSPARENT = "isTransparent";
	public static final String TRANSPARENT_COLOR_TOLERANCE = "transparentColorTolerance";
	public static final String DISPLAY_COLOR_SPACE = "displayColorSpace";

	private Integer layerImageBrightness = 0;
	private Integer layerImageContrast = 0;
	private Color transparentColor = Color.WHITE;
	private Boolean isTransparent = false;
	private Integer transparentColorTolerance = 0;
	private ColorSpaceType displayColorSpace = ColorSpaceType.RGB;

	public LayerImageParamPropertyModel() {
		// TODO
	}

	public LayerImageParamPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public Integer getBrightness() {
		return layerImageBrightness;
	}

	public void setBrightness(Integer brightness) {
		this.layerImageBrightness = brightness;
	}

	public Integer getContrast() {
		return layerImageContrast;
	}

	public void setContrast(Integer contrast) {
		this.layerImageContrast = contrast;
	}

	public Color getTransparentColor() {
		return transparentColor;
	}

	public void setTransparentColor(Color transparentColor) {
		this.transparentColor = transparentColor;
	}

	public Boolean isTransparent() {
		return isTransparent;
	}

	public void setTransparent(Boolean isTransparent) {
		this.isTransparent = isTransparent;
		checkPropertyEnabled();
	}

	public Integer getTransparentColorTolerance() {
		return transparentColorTolerance;
	}

	public void setTransparentColorTolerance(Integer transparentColorTolerance) {
		this.transparentColorTolerance = transparentColorTolerance;
	}

	public ColorSpaceType getDisplayColorSpace() {
		return displayColorSpace;
	}

	public void setDisplayColorSpace(ColorSpaceType displayColorSpace) {
		this.displayColorSpace = displayColorSpace;
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerImageParamPropertyModel imageParamPropertyModel = (LayerImageParamPropertyModel) model;

		if (imageParamPropertyModel != null) {
			this.layerImageBrightness = imageParamPropertyModel.getBrightness();
			this.layerImageContrast = imageParamPropertyModel.getContrast();
			this.isTransparent = imageParamPropertyModel.isTransparent();
			this.transparentColor = imageParamPropertyModel.getTransparentColor();
			this.transparentColorTolerance = imageParamPropertyModel.getTransparentColorTolerance();
			this.displayColorSpace = imageParamPropertyModel.getDisplayColorSpace();
			this.propertyEnabled = imageParamPropertyModel.propertyEnabled;
		}
	}

	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerImageParamPropertyModel imageParamPropertyModel = (LayerImageParamPropertyModel) model;

		return imageParamPropertyModel != null && super.equals(imageParamPropertyModel) && this.layerImageBrightness == imageParamPropertyModel.getBrightness()
				&& this.layerImageContrast == imageParamPropertyModel.getContrast() && this.isTransparent == imageParamPropertyModel.isTransparent()
				&& this.transparentColor == imageParamPropertyModel.getTransparentColor()
				&& this.transparentColorTolerance == imageParamPropertyModel.getTransparentColorTolerance()
				&& this.displayColorSpace == imageParamPropertyModel.getDisplayColorSpace();
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer.getAdditionalSetting() instanceof LayerSettingImage) {

			LayerSettingImage layerSetting = (LayerSettingImage) layer.getAdditionalSetting();

			if (this.propertyEnabled.get(BRIGHTNESS) && this.layerImageBrightness != null) {
				layerSetting.setBrightness(this.layerImageBrightness);
			}

			if (this.propertyEnabled.get(CONTRAST) && this.layerImageContrast != null) {
				layerSetting.setContrast(this.layerImageContrast);
			}

			if (this.propertyEnabled.get(IS_TRANSPARENT) && this.isTransparent != null) {
				layerSetting.setTransparent(this.isTransparent);
			}

			try {
				// 根据是否透明开确定是否能设置透明色
				if (this.propertyEnabled.get(IS_TRANSPARENT) && this.transparentColor != null) {
					layerSetting.setTransparentColor(this.transparentColor);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			// 根据是否透明开确定是否能设置容限
			if (this.propertyEnabled.get(IS_TRANSPARENT) && this.transparentColorTolerance != null) {
				layerSetting.setTransparentColorTolerance(this.transparentColorTolerance);
			}

			if (this.propertyEnabled.get(DISPLAY_COLOR_SPACE) && this.displayColorSpace != null) {
				layerSetting.setDisplayColorSpace(this.displayColorSpace);
			}
		}
	}

	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap(this.propertyEnabled);

		if (layers != null && formMap != null && formMap.getMapControl() != null && formMap.getMapControl().getMap() != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null) {
					break;
				}

				LayerSettingImage layerSetting = (LayerSettingImage) layer.getAdditionalSetting();
				this.layerImageBrightness = ComplexPropertyUtilties.union(this.layerImageBrightness, layerSetting.getBrightness());
				this.layerImageContrast = ComplexPropertyUtilties.union(this.layerImageContrast, layerSetting.getContrast());
				this.isTransparent = ComplexPropertyUtilties.union(this.isTransparent, layerSetting.isTransparent());
				this.transparentColor = ComplexPropertyUtilties.union(this.transparentColor, layerSetting.getTransparentColor());
				this.transparentColorTolerance = ComplexPropertyUtilties.union(this.transparentColorTolerance, layerSetting.getTransparentColorTolerance());
				this.displayColorSpace = ComplexPropertyUtilties.union(this.displayColorSpace, layerSetting.getDisplayColorSpace());
			}
		}

		checkPropertyEnabled();
	}

	private void resetProperties() {
		this.layerImageBrightness = 0;
		this.layerImageContrast = 0;
		this.isTransparent = false;
		this.transparentColor = Color.WHITE;
		this.transparentColorTolerance = 0;
		this.displayColorSpace = ColorSpaceType.RGB;

		if (getLayers() != null && getLayers().length > 0) {
			this.layerImageBrightness = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getBrightness();
			this.layerImageContrast = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getContrast();
			this.isTransparent = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).isTransparent();
			this.transparentColor = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getTransparentColor();
			this.transparentColorTolerance = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getTransparentColorTolerance();
			this.displayColorSpace = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getDisplayColorSpace();
		}
	}

	private void initializeEnabledMap(HashMap<String, Boolean> hashMap) {
		hashMap.put(BRIGHTNESS, true);
		hashMap.put(CONTRAST, true);
		hashMap.put(TRANSPARENT_COLOR, true);
		hashMap.put(IS_TRANSPARENT, true);
		hashMap.put(TRANSPARENT_COLOR_TOLERANCE, true);
		hashMap.put(DISPLAY_COLOR_SPACE, true);
	}

	// @formatter:off
	/**
	 * 检查属性是否可用的过程，大致是，先初始化一个临时 HashMap，所有允许修改的属性都初始化为 true， 然后根据指定的算法得到最终的可用值，再与 PropertyModel 本身存储的 PropertyEnabled 进行比对。
	 * 有一些情况需要注意。
	 * 1--多选的时候，只要有一个图层不能满足属性可用的条件，那么图层属性面板上对应的属性控件不可用，严谨性原则。
	 * 2--多选的时候，如果属性的可用状态与图层的状态（图层类型、图层绑定的数据集类型等）相关，那么就需要遍历图层来确定属性是否可用。
	 * 3--根据第一点原则，在第二点遍历的时候，只要有一个图层不能满足可用状态，那么属性则不可用。
	 * 4--以上3点，可以知道，在需要对图层做遍历的时候，需要使用类似 enabled=enabled && expression 这样的等式来进行属性可用性的确定。
	 * 5--如果属性的可用状态与 PropertyModel 某些固定属性值相关，那么就需要考虑在固定属性值改变的时候如何获得正确的可用状态。
	 * 6--往往与固定属性值相关的可用状态，在操作过程中都是会改变的，而相反，则仅在 PropertyModel 初始化拿到图层集合的时候就确定，之后不会再改变。
	 * 7--如果属性的可用状态仅和 PropertyModel 某些固定属性值的设置相关，而与图层的图层类型、图层的数据集类型什么的无关，这种情况属性是否可用的判断无需对图层做遍历，
	 *     只用关联属性是否可用、关联属性的值即可确定属性可用状态。在关联属性值改变的时候检查可用状态，并发送事件。
	 * 8--如果属性的可用状态与 PropertyModel 某些固定属性值的设置相关，又与图层的状态（图层的类型、图层绑定的数据集类型等）相关，那么在做
	 *     遍历的时候，需要使用一个临时 HashMap（所有属性都初始化为 true，不允许编辑的特殊属性除外），来进行运算。在遍历结束之后，再与 PropertyModel 的 PropertyEabled
	 *     进行匹配。如果不使用临时 HashMap，而直接使用 PropertyEnabled 来进行遍历运算，由于需要使用 enabled=enabled && expression 这样的表达式，那么一旦关联的属性在某个
	 *     时候值改变导致属性可用状态变成了 false，在之后的操作中，不论怎么改，都无法再可用了。
	 */
	// @formatter:on
	private void checkPropertyEnabled() {
		try {
			HashMap<String, Boolean> hashMapTmp = new HashMap<String, Boolean>();
			initializeEnabledMap(hashMapTmp);

			Boolean enabled = true;
			// 透明色、透明色容限，不支持透明时不可用
			enabled = hashMapTmp.get(TRANSPARENT_COLOR) && hashMapTmp.get(IS_TRANSPARENT) && this.isTransparent != null && this.isTransparent;
			hashMapTmp.put(TRANSPARENT_COLOR, enabled);
			enabled = hashMapTmp.get(TRANSPARENT_COLOR_TOLERANCE) && hashMapTmp.get(IS_TRANSPARENT) && this.isTransparent != null && this.isTransparent;
			hashMapTmp.put(TRANSPARENT_COLOR_TOLERANCE, enabled);

			if (getLayers() != null && getFormMap() != null && getFormMap().getMapControl() != null && getFormMap().getMapControl().getMap() != null
					&& getLayers().length > 0) {
				for (Layer layer : getLayers()) {
					if (layer == null) {
						break;
					}

					DatasetImage dataset = (DatasetImage) layer.getDataset();
					// 颜色模式，波段大于1时可用
					enabled = hashMapTmp.get(DISPLAY_COLOR_SPACE) && dataset.getBandCount() > 1;
					hashMapTmp.put(DISPLAY_COLOR_SPACE, enabled);
				}
			}

			for (String propertyName : hashMapTmp.keySet()) {
				checkPropertyEnabled(propertyName, hashMapTmp.get(propertyName));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
