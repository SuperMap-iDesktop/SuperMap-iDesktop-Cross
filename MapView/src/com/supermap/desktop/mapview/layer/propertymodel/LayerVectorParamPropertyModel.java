package com.supermap.desktop.mapview.layer.propertymodel;

import java.text.MessageFormat;
import java.util.HashMap;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.QueryParameter;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.utilties.DoubleUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Map;

/**
 * 显示过滤条件、对象显示顺序、设置图层关联属性表因为有额外的功能界面要开发实现，暂缓
 *
 * @author highsad
 */

/**
 * @author highsad
 */
public class LayerVectorParamPropertyModel extends LayerPropertyModel {

	private static final String ASC = "asc";
	private static final String DESC = "desc";
	private static final String SPACE = " ";
	private static final int ORDERFIELD_TAG = 0;
	private static final int ISDESC_TAG = 1;

	public static final String IS_COMPLETE_LINE_SYMBOL_DISPLAYED = "isCompleteLineSymbolDisplayed";
	public static final String IS_CROSSROAD_OPTIMIZED = "isCrossroadOptimized";
	public static final String IS_SYMBOL_SCALABLE = "isSymbolScalable";
	public static final String IS_ANTIALIAS = "isAntialias";
	public static final String IS_OVERLAP_DISPLAYED = "isOverlapDisplayed";
	public static final String SYMBOL_SCALE = "symbolScale";
	public static final String MIN_VISIBLE_GEOMETRY_SIZE = "minVisibleGeometrySize";
	public static final String IS_DESC = "desc";
	public static final String DISPLAY_ORDER_FIELD = "displayOrderField";
	public static final String DISPLAY_ATTRIBUTE_FILTER = "displayAttributeFilter";

	private Boolean isCompleteLineSymbolDisplayed = false;
	private Boolean isCrossroadOptimized = false;
	private Boolean isSymbolScalable = false;
	private Boolean isAntialias = true;
	private Boolean isOverlapDisplayed = true; // 显示压盖对象 java 桌面好像没有，待确认
	private Boolean isDesc = false;

	private Double symbolScale = 0.0;
	private Double minVisibleGeometrySize = 0.0;
	private String displayOrderField = "";
	private String displayAttributeFilter = "";

	public LayerVectorParamPropertyModel() {
		// TODO
	}

	public LayerVectorParamPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public DatasetVector getDataset() {
		DatasetVector result = null;

		if (getLayers() != null && getLayers().length == 1) {
			result = (DatasetVector) getLayers()[0].getDataset();
		}
		return result;
	}

	public Boolean isCompleteLineSymbolDisplayed() {
		return isCompleteLineSymbolDisplayed;
	}

	public void setCompleteLineSymbolDisplayed(Boolean isCompleteLineSymbolDisplayed) {
		this.isCompleteLineSymbolDisplayed = isCompleteLineSymbolDisplayed;
	}

	public Boolean isCrossroadOptimized() {
		return isCrossroadOptimized;
	}

	public void setCrossroadOptimized(Boolean isCrossroadOptimized) {
		this.isCrossroadOptimized = isCrossroadOptimized;
	}

	public Boolean isSymbolScalable() {
		return isSymbolScalable;
	}

	public void setSymbolScalable(Boolean isSymbolScalable) {
		this.isSymbolScalable = isSymbolScalable;
		checkPropertyEnabled();
	}

	public Boolean isAntialias() {
		return isAntialias;
	}

	public void setAntialias(Boolean isAntialias) {
		this.isAntialias = isAntialias;
	}

	public Boolean isOverlapDisplayed() {
		return isOverlapDisplayed;
	}

	public void setOverlapDisplayed(Boolean isOverlapDisplayed) {
		this.isOverlapDisplayed = isOverlapDisplayed;
	}

	public Double getSymbolScale() {
		return symbolScale;
	}

	public void setSymbolScale(Double symbolScale) {
		this.symbolScale = symbolScale;
	}

	public Double getMinVisibleGeometrySize() {
		return minVisibleGeometrySize;
	}

	public void setMinVisibleGeometrySize(Double minVisibleGeometrySize) {
		this.minVisibleGeometrySize = minVisibleGeometrySize;
	}

	public String getDisplayOrderField() {
		return this.displayOrderField;
	}

	public void setDisplayOrderField(String displayOrderField) {
		this.displayOrderField = displayOrderField;
		checkPropertyEnabled();
	}

	public Boolean isDesc() {
		return this.isDesc;
	}

	public void setDesc(Boolean isDesc) {
		this.isDesc = isDesc;
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerVectorParamPropertyModel vectorParamPropertyModel = (LayerVectorParamPropertyModel) model;

		if (vectorParamPropertyModel != null) {
			this.isCompleteLineSymbolDisplayed = vectorParamPropertyModel.isCompleteLineSymbolDisplayed();
			this.isCrossroadOptimized = vectorParamPropertyModel.isCrossroadOptimized();
			this.isSymbolScalable = vectorParamPropertyModel.isSymbolScalable();
			this.isAntialias = vectorParamPropertyModel.isAntialias();
			this.isOverlapDisplayed = vectorParamPropertyModel.isOverlapDisplayed();
			this.symbolScale = vectorParamPropertyModel.getSymbolScale();
			this.minVisibleGeometrySize = vectorParamPropertyModel.getMinVisibleGeometrySize();
			this.displayOrderField = vectorParamPropertyModel.getDisplayOrderField();
			this.isDesc = vectorParamPropertyModel.isDesc();
			this.displayAttributeFilter = vectorParamPropertyModel.getDisplayAttributeFilter();
			this.propertyEnabled = vectorParamPropertyModel.propertyEnabled;
		}
	}

	/**
	 * 子类必须重写这个方法
	 *
	 * @param model
	 * @return
	 */
	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerVectorParamPropertyModel vectorParamPropertyModel = (LayerVectorParamPropertyModel) model;

		return vectorParamPropertyModel != null && super.equals(vectorParamPropertyModel)
				&& this.isCompleteLineSymbolDisplayed == vectorParamPropertyModel.isCompleteLineSymbolDisplayed()
				&& this.isCrossroadOptimized == vectorParamPropertyModel.isCrossroadOptimized()
				&& this.isSymbolScalable == vectorParamPropertyModel.isSymbolScalable() && this.isAntialias == vectorParamPropertyModel.isAntialias()
				&& this.isOverlapDisplayed == vectorParamPropertyModel.isOverlapDisplayed()
				&& Double.compare(this.symbolScale, vectorParamPropertyModel.getSymbolScale()) == 0
				&& Double.compare(this.minVisibleGeometrySize, vectorParamPropertyModel.getMinVisibleGeometrySize()) == 0
				&& this.displayOrderField == vectorParamPropertyModel.getDisplayOrderField() && this.isDesc == vectorParamPropertyModel.isDesc()
				&& this.displayAttributeFilter == vectorParamPropertyModel.getDisplayAttributeFilter();
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null) {
			if (this.propertyEnabled.get(IS_COMPLETE_LINE_SYMBOL_DISPLAYED) && this.isCompleteLineSymbolDisplayed != null) {
				layer.setCompleteLineSymbolDisplayed(this.isCompleteLineSymbolDisplayed);
			}

			if (this.propertyEnabled.get(IS_CROSSROAD_OPTIMIZED) && this.isCrossroadOptimized != null) {
				layer.setCrossroadOptimized(this.isCrossroadOptimized);
			}

			if (this.propertyEnabled.get(IS_SYMBOL_SCALABLE) && this.isSymbolScalable != null) {
				layer.setSymbolScalable(this.isSymbolScalable);
			}

			if (this.propertyEnabled.get(IS_ANTIALIAS) && this.isAntialias != null) {
				layer.setAntialias(this.isAntialias);
			}

			if (this.propertyEnabled.get(IS_OVERLAP_DISPLAYED) && this.isOverlapDisplayed != null) {
				// 压盖显示，Java 组件还没有这个接口，预留
			}

			if (this.propertyEnabled.get(SYMBOL_SCALE) && this.symbolScale != null) {
				layer.setSymbolScale(this.symbolScale);
			}

			if (this.propertyEnabled.get(MIN_VISIBLE_GEOMETRY_SIZE) && this.minVisibleGeometrySize != null) {
				layer.setMinVisibleGeometrySize(this.minVisibleGeometrySize);
			}

			if (this.propertyEnabled.get(DISPLAY_ORDER_FIELD)) {
				if (StringUtilties.isNullOrEmpty(this.displayOrderField)) {
					layer.getDisplayFilter().setOrderBy(null);
				} else {
					layer.getDisplayFilter().setOrderBy(new String[]{MessageFormat.format("{0} {1}", this.displayOrderField, this.isDesc ? DESC : ASC)});
				}
			}

			if (this.propertyEnabled.get(DISPLAY_ATTRIBUTE_FILTER)) {
				if (StringUtilties.isNullOrEmpty(this.displayAttributeFilter)) {
					layer.getDisplayFilter().setAttributeFilter("");
				} else {
					layer.getDisplayFilter().setAttributeFilter(this.displayAttributeFilter);
				}
			}
		}
	}

	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap(this.propertyEnabled);

		if (layers != null && formMap != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null) {
					break;
				}

				this.isCompleteLineSymbolDisplayed = ComplexPropertyUtilties.union(this.isCompleteLineSymbolDisplayed, layer.isCompleteLineSymbolDisplayed());
				this.isCrossroadOptimized = ComplexPropertyUtilties.union(this.isCrossroadOptimized, layer.isCrossroadOptimized());
				this.isSymbolScalable = ComplexPropertyUtilties.union(this.isSymbolScalable, layer.isSymbolScalable());
				this.isAntialias = ComplexPropertyUtilties.union(this.isAntialias, layer.isAntialias());
				this.isOverlapDisplayed = ComplexPropertyUtilties.union(true, true); // java 桌面好像暂时没有这个属性
				this.symbolScale = ComplexPropertyUtilties.union(this.symbolScale, layer.getSymbolScale());
				this.minVisibleGeometrySize = ComplexPropertyUtilties.union(this.minVisibleGeometrySize, layer.getMinVisibleGeometrySize());
				this.displayOrderField = ComplexPropertyUtilties.union(this.displayOrderField, (String) getOrderByParts(layer)[ORDERFIELD_TAG]);
				this.isDesc = ComplexPropertyUtilties.union(this.isDesc, (Boolean) getOrderByParts(layer)[ISDESC_TAG]);
				this.displayAttributeFilter = layer.getDisplayFilter().getAttributeFilter();
			}
		}

		checkPropertyEnabled();
	}

	/**
	 * 重置数据
	 */
	private void resetProperties() {
		this.isCompleteLineSymbolDisplayed = false;
		this.isCrossroadOptimized = false;
		this.isSymbolScalable = true;
		this.isAntialias = true;
		this.isOverlapDisplayed = true;
		this.symbolScale = 0.0;
		this.minVisibleGeometrySize = 0.0;
		this.displayOrderField = "";

		if (getLayers() != null && getLayers().length > 0) {
			this.isCompleteLineSymbolDisplayed = getLayers()[0].isCompleteLineSymbolDisplayed();
			this.isCrossroadOptimized = getLayers()[0].isCrossroadOptimized();
			this.isSymbolScalable = getLayers()[0].isSymbolScalable();
			this.isAntialias = getLayers()[0].isAntialias();
			// this.isOverlapDisplayed=
			this.symbolScale = getLayers()[0].getSymbolScale();
			this.minVisibleGeometrySize = getLayers()[0].getMinVisibleGeometrySize();
			if (getLayers()[0].getDisplayFilter().getOrderBy() != null && getLayers()[0].getDisplayFilter().getOrderBy().length > 0) {
				this.displayOrderField = (String) getOrderByParts(getLayers()[0])[ORDERFIELD_TAG];
				this.isDesc = (Boolean) getOrderByParts(getLayers()[0])[ISDESC_TAG];
				this.displayAttributeFilter = getLayers()[0].getDisplayFilter().getAttributeFilter();
			}
		}
	}

	private void initializeEnabledMap(HashMap<String, Boolean> hashMap) {
		hashMap.put(IS_COMPLETE_LINE_SYMBOL_DISPLAYED, true);
		hashMap.put(IS_CROSSROAD_OPTIMIZED, true);
		hashMap.put(IS_SYMBOL_SCALABLE, true);
		hashMap.put(IS_ANTIALIAS, true);
		hashMap.put(IS_OVERLAP_DISPLAYED, true);
		hashMap.put(SYMBOL_SCALE, true);
		hashMap.put(MIN_VISIBLE_GEOMETRY_SIZE, true);
		hashMap.put(DISPLAY_ORDER_FIELD, true);
		hashMap.put(IS_DESC, true);
		hashMap.put(DISPLAY_ATTRIBUTE_FILTER, true);
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

			if (getLayers() != null && getFormMap() != null && getFormMap().getMapControl() != null && getFormMap().getMapControl().getMap() != null
					&& getLayers().length > 0) {
				if (getLayers().length > 1) {
					hashMapTmp.put(DISPLAY_ORDER_FIELD, false);
					hashMapTmp.put(DISPLAY_ATTRIBUTE_FILTER, false);
				}

				if (!hashMapTmp.get(DISPLAY_ORDER_FIELD) || StringUtilties.isNullOrEmpty(this.displayOrderField)) {
					hashMapTmp.put(IS_DESC, false);
				}

				for (Layer layer : getLayers()) {
					if (layer == null) {
						break;
					}

					Dataset dataset = layer.getDataset();
					Boolean enabled = true;
					// 显示完整线性，数据集非线面数据集则不可用
					enabled = hashMapTmp.get(IS_COMPLETE_LINE_SYMBOL_DISPLAYED)
							&& (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.REGION);
					hashMapTmp.put(IS_COMPLETE_LINE_SYMBOL_DISPLAYED, enabled);
					// 十字路口优化，数据集非线数据集则不可用
					enabled = hashMapTmp.get(IS_CROSSROAD_OPTIMIZED) && dataset.getType() == DatasetType.LINE;
					hashMapTmp.put(IS_CROSSROAD_OPTIMIZED, enabled);
					// 缩放基准比例尺，符号随图缩放设置为 false
					enabled = hashMapTmp.get(SYMBOL_SCALE) && hashMapTmp.get(IS_SYMBOL_SCALABLE)
							&& this.isSymbolScalable != null && this.isSymbolScalable;
					hashMapTmp.put(SYMBOL_SCALE, enabled);
					// 对象最小尺寸，数据集为点数据集则不可用
					enabled = hashMapTmp.get(MIN_VISIBLE_GEOMETRY_SIZE) && dataset.getType() != DatasetType.POINT;
					hashMapTmp.put(MIN_VISIBLE_GEOMETRY_SIZE, enabled);
				}
			}

			for (String propertyName : hashMapTmp.keySet()) {
				checkPropertyEnabled(propertyName, hashMapTmp.get(propertyName));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private String getLayerOrderBy(Layer layer) {
		String result = null;

		if (layer.getDisplayFilter().getOrderBy() != null && layer.getDisplayFilter().getOrderBy().length > 0) {
			result = layer.getDisplayFilter().getOrderBy()[0];
		}
		return result;
	}

	private boolean isOrderByDesc(String orderBy) {
		boolean result = false;

		if (orderBy != null && orderBy.trim().toLowerCase().endsWith(DESC)) {
			result = true;
		}
		return result;
	}

	private boolean isOorderByAsc(String orderBy) {
		boolean result = false;

		if (orderBy != null && orderBy.trim().toLowerCase().endsWith(ASC)) {
			result = true;
		}
		return result;
	}

	private Object[] getOrderByParts(Layer layer) {
		Object[] parts = new Object[2];

		String orderBy = getLayerOrderBy(layer);
		if (StringUtilties.isNullOrEmpty(orderBy)) {
			parts[ORDERFIELD_TAG] = null;
			parts[ISDESC_TAG] = false;
		} else {
			String[] strParts = orderBy.split(SPACE);
			if (strParts.length == 1) {
				parts[ORDERFIELD_TAG] = strParts[0];
				parts[ISDESC_TAG] = false;
			} else if (strParts.length > 1) {
				parts[ORDERFIELD_TAG] = strParts[0];
				if (strParts[1].toLowerCase().trim().contains(DESC)) {
					parts[ISDESC_TAG] = true;
				} else {
					parts[ISDESC_TAG] = false;
				}
			}
		}
		return parts;
	}

	public String getDisplayAttributeFilter() {
		return this.displayAttributeFilter;
	}

	public void setDisplayAttributeFilter(String displayAttributeFilter) {
		this.displayAttributeFilter = displayAttributeFilter;
		checkPropertyEnabled();
	}
}
