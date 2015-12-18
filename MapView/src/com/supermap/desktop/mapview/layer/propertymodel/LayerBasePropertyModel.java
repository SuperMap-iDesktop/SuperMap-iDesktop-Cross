package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerCaptionChangedEvent;
import com.supermap.mapping.LayerCaptionChangedListener;
import com.supermap.mapping.LayerChart;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.LayerSelectableChangedEvent;
import com.supermap.mapping.LayerSelectableChangedListener;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.LayerSnapableChangedEvent;
import com.supermap.mapping.LayerSnapableChangedListener;
import com.supermap.mapping.LayerVisibleChangedEvent;
import com.supermap.mapping.LayerVisibleChangedListener;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeType;

public class LayerBasePropertyModel extends LayerPropertyModel {

	public static final String IS_VISIBLE = "isVisible";
	public static final String IS_EDITABLE = "isEditable";
	public static final String IS_SELECTABLE = "isSelectable";
	public static final String IS_SNAPABLE = "isSnapable";
	public static final String CAPTION = "caption";
	public static final String NAME = "name";
	public static final String TRANSPARENCE = "transparence";
	public static final String MIN_VISIBLE_SCALE = "minVisibleScale";
	public static final String MAX_VISIBLE_SCALE = "maxVisibleScale";
	private Boolean isVisible = true;
	private Boolean isEditable = false;
	private Boolean isSelectable = true;
	private Boolean isSnapable = true;
	private String propertyCaption = "";
	private String propertyName = "";
	private Integer propertyTransparence = 0;
	private Double minVisibleScale = 0.0;
	private Double maxVisibleScale = 0.0;

	public LayerBasePropertyModel() {
		// TODO
	}

	public LayerBasePropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public Boolean isVisible() {
		return isVisible;
	}

	public void setVisible(Boolean isVisible) {
		this.isVisible = isVisible;
		checkPropertyEnbled();
	}

	public Boolean isEditable() {
		return isEditable;
	}

	public void setEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public Boolean isSelectable() {
		return isSelectable;
	}

	public void setSelectable(Boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	public Boolean isSnapable() {
		return isSnapable;
	}

	public void setSnapable(Boolean isSnapable) {
		this.isSnapable = isSnapable;
	}

	public String getCaption() {
		return propertyCaption;
	}

	public void setCaption(String caption) {
		this.propertyCaption = caption;
	}

	public Integer getTransparence() {
		return propertyTransparence;
	}

	public void setTransparence(Integer transparence) {
		this.propertyTransparence = transparence;
	}

	public Double getMinVisibleScale() {
		return minVisibleScale;
	}

	public void setMinVisibleScale(Double minVisibleScale) {
		this.minVisibleScale = minVisibleScale;
	}

	public Double getMaxVisibleScale() {
		return maxVisibleScale;
	}

	public void setMaxVisibleScale(Double maxVisibleScale) {
		this.maxVisibleScale = maxVisibleScale;
	}

	public String getName() {
		return propertyName;
	}

	/**
	 * 使用指定 model 的数据设置自己的属性 子类必须重写这个方法
	 * 
	 * @param model
	 */
	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerBasePropertyModel basePropertyModel = (LayerBasePropertyModel) model;

		if (basePropertyModel != null) {
			this.isVisible = basePropertyModel.isVisible();
			this.isEditable = basePropertyModel.isEditable();
			this.isSelectable = basePropertyModel.isSelectable();
			this.isSnapable = basePropertyModel.isSnapable();
			this.propertyCaption = basePropertyModel.getCaption();
			this.propertyTransparence = basePropertyModel.getTransparence();
			this.minVisibleScale = basePropertyModel.getMinVisibleScale();
			this.maxVisibleScale = basePropertyModel.getMaxVisibleScale();
			this.propertyEnabled = basePropertyModel.propertyEnabled;
		} else {
			throw new IllegalArgumentException();
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
		LayerBasePropertyModel basePropertyModel = (LayerBasePropertyModel) model;
		return super.equals(basePropertyModel) && this.isVisible == basePropertyModel.isVisible() && this.isEditable == basePropertyModel.isEditable()
				&& this.isSelectable == basePropertyModel.isSelectable() && this.isSnapable == basePropertyModel.isSnapable()
				&& (StringUtilties.stringEquals(this.propertyCaption, basePropertyModel.getCaption()))
				&& this.propertyTransparence == basePropertyModel.getTransparence()
				&& Double.compare(this.minVisibleScale, basePropertyModel.getMinVisibleScale()) == 0
				&& Double.compare(this.maxVisibleScale, basePropertyModel.getMaxVisibleScale()) == 0;
	}

	@Override
	protected void apply(Layer layer) {
		if (this.propertyEnabled.get(IS_VISIBLE) && this.isVisible != null) {
			layer.setVisible(this.isVisible);
		}

		if (this.propertyEnabled.get(IS_EDITABLE) && this.isEditable != null) {
			layer.setEditable(this.isEditable);
		}

		if (this.propertyEnabled.get(IS_SELECTABLE) && this.isSelectable != null) {
			layer.setSelectable(this.isSelectable);
		}

		if (this.propertyEnabled.get(IS_SNAPABLE) && this.isSnapable != null) {
			layer.setSnapable(this.isSnapable);
		}

		if (this.propertyEnabled.get(CAPTION) && !StringUtilties.isNullOrEmpty(this.propertyCaption)) {
			layer.setCaption(this.propertyCaption);
		}

		if (this.propertyEnabled.get(TRANSPARENCE) && this.propertyTransparence != null) {
			layer.setOpaqueRate(100 - this.propertyTransparence);
		}

		if (this.propertyEnabled.get(MIN_VISIBLE_SCALE) && this.minVisibleScale != null
				&& (Double.compare(layer.getMaxVisibleScale(), this.minVisibleScale) >= 0 || Double.compare(layer.getMaxVisibleScale(), 0) == 0)) {
			layer.setMinVisibleScale(this.minVisibleScale);
		}

		if (this.propertyEnabled.get(MAX_VISIBLE_SCALE) && this.maxVisibleScale != null
				&& Double.compare(this.maxVisibleScale, layer.getMinVisibleScale()) >= 0) {
			layer.setMaxVisibleScale(this.maxVisibleScale);
		}
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.getLayers().length; i++) {
			removeLayerListeners(this.getLayers()[i]);
		}
	}

	/**
	 * 根据指定的图层初始化数据
	 * 
	 * @param layers
	 * @param map
	 */
	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap();

		if (layers != null && formMap != null && formMap.getMapControl() != null && formMap.getMapControl().getMap() != null && layers.length > 0) {
			for (Layer layer : layers) {
				if (layer == null) {
					break;
				}

				this.isVisible = ComplexPropertyUtilties.union(this.isVisible, layer.isVisible());
				this.isEditable = ComplexPropertyUtilties.union(this.isEditable, layer.isEditable());
				this.isSelectable = ComplexPropertyUtilties.union(this.isSelectable, layer.isSelectable());
				this.isSnapable = ComplexPropertyUtilties.union(this.isSnapable, layer.isSnapable());
				this.propertyCaption = ComplexPropertyUtilties.union(this.propertyCaption, layer.getCaption());
				this.propertyName = ComplexPropertyUtilties.union(this.propertyName, layer.getName());
				this.propertyTransparence = ComplexPropertyUtilties.union(this.propertyTransparence, 100 - layer.getOpaqueRate());
				this.minVisibleScale = ComplexPropertyUtilties.union(this.minVisibleScale, layer.getMinVisibleScale());
				this.maxVisibleScale = ComplexPropertyUtilties.union(this.maxVisibleScale, layer.getMaxVisibleScale());
			}
		}

		checkPropertyEnbled();
	}

	/**
	 * 重置数据
	 */
	private void resetProperties() {
		this.isVisible = true;
		this.isEditable = false;
		this.isSelectable = true;
		this.isSnapable = true;
		this.propertyCaption = "";
		this.propertyName = "";
		this.propertyTransparence = 0;
		this.minVisibleScale = 0.0;
		this.maxVisibleScale = 0.0;

		if (getLayers() != null && getLayers().length > 0) {
			this.isVisible = getLayers()[0].isVisible();
			this.isEditable = getLayers()[0].isEditable();
			this.isSelectable = getLayers()[0].isSelectable();
			this.isSnapable = getLayers()[0].isSnapable();
			this.propertyCaption = getLayers()[0].getCaption();
			this.propertyName = getLayers()[0].getName();
			this.propertyTransparence = 100 - getLayers()[0].getOpaqueRate();
			this.minVisibleScale = getLayers()[0].getMinVisibleScale();
			this.maxVisibleScale = getLayers()[0].getMaxVisibleScale();
		}
	}

	private void initializeEnabledMap() {
		this.propertyEnabled.put(IS_VISIBLE, true);
		this.propertyEnabled.put(IS_EDITABLE, true);
		this.propertyEnabled.put(IS_SELECTABLE, true);
		this.propertyEnabled.put(IS_SNAPABLE, true);
		this.propertyEnabled.put(CAPTION, true);
		this.propertyEnabled.put(NAME, false);
		this.propertyEnabled.put(TRANSPARENCE, true);
		this.propertyEnabled.put(MIN_VISIBLE_SCALE, true);
		this.propertyEnabled.put(MAX_VISIBLE_SCALE, true);
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
	private void checkPropertyEnbled() {
		try {
			if (getLayers() != null && getFormMap() != null && getLayers().length > 0) {
				if (getLayers().length > 1) {
					checkPropertyEnabled(CAPTION, false);
					checkPropertyEnabled(IS_EDITABLE, getFormMap().getMapControl() != null && getFormMap().getMapControl().isMultiLayerEditEnabled());
				}

				boolean isEditableEnabled = true;
				boolean isSelectableEnabled = true;
				boolean isSnapableEnabled = true;
				boolean transparenceEnabled = true;

				// Model 里设置的 isVisible 是否为 true，需要根据这个处理相关属性的可用性
				boolean isVisibleSettingsValue = this.isVisible != null && this.isVisible;

				for (Layer layer : getLayers()) {
					if (layer == null) {
						break;
					}

					// 丢失数据集的图层，不可编辑
					boolean isDatasetNull = layer.getDataset() == null;

					boolean isDatasetVector = !isDatasetNull && layer.getDataset() instanceof DatasetVector;

					// 只读数据源下的数据集以及只读数据集不可编辑
					boolean isReadOnly = !isDatasetNull && (layer.getDataset().getDatasource().isReadOnly() || layer.getDataset().isReadOnly());

					// 海图数据集不可编辑
					boolean isLayerChart = layer instanceof LayerChart;

					// 标签、统计、等级符号专题图，不可选择，不可编辑，不可捕捉
					boolean isInvalidThemeLayer = layer.getTheme() != null
							&& (layer.getTheme().getType() == ThemeType.LABEL || layer.getTheme().getType() == ThemeType.GRADUATEDSYMBOL || layer.getTheme()
									.getType() == ThemeType.GRAPH);

					isEditableEnabled = isEditableEnabled && isVisibleSettingsValue && isDatasetVector && !isReadOnly && !isLayerChart && !isInvalidThemeLayer;
					isSelectableEnabled = isSelectableEnabled && isVisibleSettingsValue && isDatasetVector && !isInvalidThemeLayer;
					isSnapableEnabled = isSnapableEnabled && isVisibleSettingsValue && isDatasetVector && !isInvalidThemeLayer;
					transparenceEnabled = transparenceEnabled && !(layer instanceof LayerGroup);
				}

				checkPropertyEnabled(IS_EDITABLE, isEditableEnabled);
				checkPropertyEnabled(IS_SELECTABLE, isSelectableEnabled);
				checkPropertyEnabled(IS_SNAPABLE, isSnapableEnabled);
				checkPropertyEnabled(TRANSPARENCE, transparenceEnabled);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void removeLayerListeners(Layer layer) {
		// do nothing
	}
}
