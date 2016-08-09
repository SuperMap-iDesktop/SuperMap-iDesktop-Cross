package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.ImageStretchOption;
import com.supermap.mapping.ImageStretchType;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingImage;

import java.util.HashMap;

public class LayerStretchOptionPropertyModel extends LayerPropertyModel {

	/**
	 * 拉伸类型参数名
	 */
	public static final String STRETCH_TYPE = "stretchType";
	/**
	 * 标准差拉伸系数参数名
	 */
	public static final String STANDARD_DEVIATION_STRETCH_FACTOR = "standardDeviationStretchFactor";
	/**
	 * 高斯拉伸系数参数名
	 */
	public static final String GAUSSIAN_STRETCH_RATIO_FACTOR = "gaussianStretchRatioFactor";
	/**
	 * 是否使用高斯拉伸中间值参数名
	 */
	public static final String IS_GAUSSIAN_STRETCH_MIDDLE_FACTOR = "isGaussianStretchMiddleFactor";

	private ImageStretchType stretchType = ImageStretchType.MINIMUMMAXIMUM; // 影像拉伸类型
	private Double standardDeviationStretchFactor = 0.0; // 标准差拉伸系数
	private Double gaussianStretchRatioFactor = 0.0; // 高斯拉伸系数
	private Boolean isGaussianStretchMiddleFactor = true; // 高斯拉伸时使用中间值

	public LayerStretchOptionPropertyModel() {
		// TODO
	}

	public LayerStretchOptionPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public ImageStretchType getStretchType() {
		return stretchType;
	}

	public void setStretchType(ImageStretchType stretchType) {
		this.stretchType = stretchType;
		checkPropertyEnabled();
	}

	public Double getStandardDeviationStretchFactor() {
		return standardDeviationStretchFactor;
	}

	public void setStandardDeviationStretchFactor(Double standardDeviationStretchFactor) {
		this.standardDeviationStretchFactor = standardDeviationStretchFactor;
	}

	public Double getGaussianStretchRatioFactor() {
		return gaussianStretchRatioFactor;
	}

	public void setGaussianStretchRatioFactor(Double gaussianStretchRatioFactor) {
		this.gaussianStretchRatioFactor = gaussianStretchRatioFactor;
	}

	public Boolean isGaussianStretchMiddleFactor() {
		return isGaussianStretchMiddleFactor;
	}

	public void setGaussianStretchMiddleFactor(Boolean isGaussianStretchMiddleFactor) {
		this.isGaussianStretchMiddleFactor = isGaussianStretchMiddleFactor;
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerStretchOptionPropertyModel stretchOptionPropertyModel = (LayerStretchOptionPropertyModel) model;

		if (stretchOptionPropertyModel != null) {
			this.stretchType = stretchOptionPropertyModel.getStretchType();
			this.standardDeviationStretchFactor = stretchOptionPropertyModel.getStandardDeviationStretchFactor();
			this.gaussianStretchRatioFactor = stretchOptionPropertyModel.getGaussianStretchRatioFactor();
			this.isGaussianStretchMiddleFactor = stretchOptionPropertyModel.isGaussianStretchMiddleFactor();
			this.propertyEnabled = stretchOptionPropertyModel.propertyEnabled;
		}
	}

	/*
	 * 子类必须重写这个方法
	 * 
	 * @see com.supermap.desktop.mapview.layer.propertymodel.LayerPropertyModel#equals(com.supermap.desktop.mapview.layer.propertymodel.LayerPropertyModel)
	 */
	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerStretchOptionPropertyModel stretchOptionPropertyModel = (LayerStretchOptionPropertyModel) model;

		return this.stretchType == stretchOptionPropertyModel.getStretchType()
				&& Double.compare(this.standardDeviationStretchFactor, stretchOptionPropertyModel.getStandardDeviationStretchFactor()) == 0
				&& Double.compare(this.gaussianStretchRatioFactor, stretchOptionPropertyModel.getGaussianStretchRatioFactor()) == 0
				&& this.isGaussianStretchMiddleFactor == stretchOptionPropertyModel.isGaussianStretchMiddleFactor();
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer.getAdditionalSetting() instanceof LayerSettingImage) {
			ImageStretchOption stretchOption = ((LayerSettingImage) layer.getAdditionalSetting()).getImageStretchOption();

			if (this.propertyEnabled.get(STRETCH_TYPE) && this.stretchType != null) {
				stretchOption.setStretchType(this.stretchType);
			}

			if (this.propertyEnabled.get(STANDARD_DEVIATION_STRETCH_FACTOR) && this.stretchType == ImageStretchType.STANDARDDEVIATION
					&& this.standardDeviationStretchFactor != null) {
				stretchOption.setStandardDeviationStretchFactor(this.standardDeviationStretchFactor);
			}

			if (this.propertyEnabled.get(GAUSSIAN_STRETCH_RATIO_FACTOR) && this.stretchType == ImageStretchType.GAUSSIAN) {
				if (this.gaussianStretchRatioFactor != null) {
					stretchOption.setGaussianStretchRatioFactor(this.gaussianStretchRatioFactor);
				}

				if (this.propertyEnabled.get(IS_GAUSSIAN_STRETCH_MIDDLE_FACTOR) && this.isGaussianStretchMiddleFactor != null) {
					stretchOption.setGaussianStretchMiddleFactor(this.isGaussianStretchMiddleFactor);
				}
			}
		}
	}

	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap(this.propertyEnabled);

		if (layers != null && formMap != null && formMap.getMapControl() != null && formMap.getMapControl().getMap() != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null || layer.isDisposed()) {
					break;
				}

				ImageStretchOption stretchOption = ((LayerSettingImage) layer.getAdditionalSetting()).getImageStretchOption();
				this.stretchType = ComplexPropertyUtilties.union(this.stretchType, stretchOption.getStretchType());
				this.standardDeviationStretchFactor = ComplexPropertyUtilties.union(this.standardDeviationStretchFactor,
						stretchOption.getStandardDeviationStretchFactor());
				this.gaussianStretchRatioFactor = ComplexPropertyUtilties.union(this.gaussianStretchRatioFactor, stretchOption.getGaussianStretchRatioFactor());
				this.isGaussianStretchMiddleFactor = ComplexPropertyUtilties.union(this.isGaussianStretchMiddleFactor,
						stretchOption.isGaussianStretchMiddleFactor());
			}
		}

		checkPropertyEnabled();
	}

	private void resetProperties() {
		this.stretchType = ImageStretchType.MINIMUMMAXIMUM;
		this.standardDeviationStretchFactor = 0.0;
		this.gaussianStretchRatioFactor = 0.0;
		this.isGaussianStretchMiddleFactor = true;

		if (getLayers() != null && getLayers().length > 0) {
			this.stretchType = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getImageStretchOption().getStretchType();
			this.standardDeviationStretchFactor = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getImageStretchOption()
					.getStandardDeviationStretchFactor();
			this.gaussianStretchRatioFactor = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getImageStretchOption()
					.getGaussianStretchRatioFactor();
			this.isGaussianStretchMiddleFactor = ((LayerSettingImage) getLayers()[0].getAdditionalSetting()).getImageStretchOption()
					.isGaussianStretchMiddleFactor();
		}
	}

	private void initializeEnabledMap(HashMap<String, Boolean> hashMap) {
		hashMap.put(STRETCH_TYPE, true);
		hashMap.put(STANDARD_DEVIATION_STRETCH_FACTOR, true);
		hashMap.put(GAUSSIAN_STRETCH_RATIO_FACTOR, true);
		hashMap.put(IS_GAUSSIAN_STRETCH_MIDDLE_FACTOR, true);
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
			Boolean enabled = true;
			// 标准差系数，拉伸方式非标准差拉伸时不可用
			enabled = isPropertyEnabled(STRETCH_TYPE) && this.stretchType == ImageStretchType.STANDARDDEVIATION;
			checkPropertyEnabled(STANDARD_DEVIATION_STRETCH_FACTOR, enabled);
			// 高斯系数、高斯拉伸使用中间值，拉伸方式非高斯拉伸时不可用
			enabled = isPropertyEnabled(STRETCH_TYPE) && this.stretchType == ImageStretchType.GAUSSIAN;
			checkPropertyEnabled(GAUSSIAN_STRETCH_RATIO_FACTOR, enabled);
			checkPropertyEnabled(IS_GAUSSIAN_STRETCH_MIDDLE_FACTOR, enabled);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
