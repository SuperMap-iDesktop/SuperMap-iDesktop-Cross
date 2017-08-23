package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.data.*;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGridAggregation;
import com.supermap.mapping.LayerGridAggregationType;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/7/21.
 */
public class LayerGridAggregationPropertyModel extends LayerPropertyModel {

	public static final String GRID_FIELD = "gridField";                          //格网字段
	public static final String GRID_TYPE = "gridType";                            // 格网类型
	public static final String LENGTH = "length";                                 // 边长
	public static final String COLOR_SCHEME = "colorScheme";                      // 颜色方案
	public static final String MAX_COLOR = "maxColor";                            // 最大颜色
	public static final String MAX_COLOR_TRANSPARENCE = "maxColorTransparence";   // 最大颜色透明度
	public static final String MIN_COLOR = "minColor";                            // 最小颜色
	public static final String MIN_COLOR_TRANSPARENCE = "minColorTransparence";   // 最小颜色透明度
	public static final String IS_SHOW_GRID_LABEL = "isShowGridLabel";            //是否显示网格单元标签
	public static final String LINE_TYPE = "lineType";                            // 线段类型
	public static final String LINE_COLOR = "lineColor";                          // 线段颜色
	public static final String LINE_COLOR_TRANSPARENCE = "lineColorTransparence"; // 线段透明度
	public static final String LINE_WIDTH = "lineWidth";                           // 线段宽度
	public static final String LINE_STYLE = "lineStyle";                           // 线段风格
	public static final String TEXT_STYLE = "textStyle";                           // 文本风格

	private String gridField = "";
	private LayerGridAggregationType layerGridAggregationType = null;
	private Integer length = 0;
	private Colors colorScheme = null;
	private Color maxColor = null;
	private Integer maxColorTransparence = 0;
	private Color minColor = null;
	private Integer minColorTransparence = 0;
	private Boolean isShowGridLabel = false;
	private GeoStyle lineStyle = null;
	private TextStyle textStyle = null;
	private FieldInfos fieldInfos = null;
	private GeoStyle tempGeostyle=null;
	private TextStyle tempTextStyle=null;

	public LayerGridAggregationPropertyModel() {
		// do nothing
		// why do nothing，Can you guess ah
	}

	public String getGridField() {
		return gridField;
	}

	public void setGridField(String gridField) {
		this.gridField = gridField;
	}

	public LayerGridAggregationType getLayerGridAggregationType() {
		return layerGridAggregationType;
	}

	public void setLayerGridAggregationType(LayerGridAggregationType layerGridAggregationType) {
		this.layerGridAggregationType = layerGridAggregationType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Colors getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(Colors colorScheme) {
		this.colorScheme = colorScheme;
	}

	public Color getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

	public Integer getMaxColorTransparence() {
		return maxColorTransparence;
	}

	public void setMaxColorTransparence(Integer maxColorTransparence) {
		this.maxColorTransparence = maxColorTransparence;
	}

	public Color getMinColor() {
		return minColor;
	}

	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public Integer getMinColorTransparence() {
		return minColorTransparence;
	}

	public void setMinColorTransparence(Integer minColorTransparence) {
		this.minColorTransparence = minColorTransparence;
	}

	public Boolean getShowGridLabel() {
		return isShowGridLabel;
	}

	public void setShowGridLabel(Boolean showGridLabel) {
		isShowGridLabel = showGridLabel;
	}

	public GeoStyle getLineStyle() {
//		if (this.lineStyle==null){
//			return this.tempGeostyle.clone();
//		}else {
			return lineStyle;
		//}
	}

	public void setLineStyle(GeoStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public TextStyle getTextStyle() {
//		if (this.textStyle==null){
//			return this.tempTextStyle.clone();
//		}else {
			return this.textStyle;
		//}
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

	public LayerGridAggregationPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}

	public FieldInfos getFieldInfos() {
		return fieldInfos;
	}

	public void setFieldInfos(FieldInfos fieldInfos) {
		this.fieldInfos = fieldInfos;
	}


	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerGridAggregationPropertyModel layerGridAggregationPropertyModel = (LayerGridAggregationPropertyModel) model;
		if (layerGridAggregationPropertyModel != null) {
			this.gridField = layerGridAggregationPropertyModel.getGridField();
			this.layerGridAggregationType = layerGridAggregationPropertyModel.getLayerGridAggregationType();
			this.length = layerGridAggregationPropertyModel.getLength();
			this.colorScheme = layerGridAggregationPropertyModel.getColorScheme();
			this.maxColor = layerGridAggregationPropertyModel.getMaxColor();
			this.maxColorTransparence = layerGridAggregationPropertyModel.getMaxColorTransparence();
			this.minColor = layerGridAggregationPropertyModel.getMinColor();
			this.minColorTransparence = layerGridAggregationPropertyModel.getMinColorTransparence();
			this.isShowGridLabel = layerGridAggregationPropertyModel.getShowGridLabel();
			this.lineStyle = layerGridAggregationPropertyModel.getLineStyle();
			this.textStyle = layerGridAggregationPropertyModel.getTextStyle();
		}
	}

	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerGridAggregationPropertyModel layerGridAggregationPropertyModel = (LayerGridAggregationPropertyModel) model;

		return layerGridAggregationPropertyModel != null && this.gridField == layerGridAggregationPropertyModel.getGridField()
				&& this.layerGridAggregationType==layerGridAggregationPropertyModel.getLayerGridAggregationType()
				&& this.maxColor==layerGridAggregationPropertyModel.getMaxColor() && this.minColor==layerGridAggregationPropertyModel.getMinColor();
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer instanceof LayerGridAggregation) {
			LayerGridAggregation layerGridAggregation = (LayerGridAggregation) layer;

			if (this.propertyEnabled.get(GRID_FIELD) && this.gridField!=null){
				layerGridAggregation.setWeightField(this.getGridField());
			}

			if (this.propertyEnabled.get(GRID_TYPE) && this.layerGridAggregationType!=null){
				layerGridAggregation.setGridAggregationType(this.getLayerGridAggregationType());
			}

			if (this.propertyEnabled.get(LENGTH) && this.length!=null){
				layerGridAggregation.setGridWidth(this.getLength());
				layerGridAggregation.setGridHeight(this.getLength());
			}

//			if (this.propertyEnabled.get(COLOR_SCHEME) && this.colorScheme!=null){
//				layerGridAggregation.setWeightField(this.getGridField());
//			}

			if (this.propertyEnabled.get(MAX_COLOR) && this.maxColor!=null){
				layerGridAggregation.setMaxColor(this.getMaxColor());
			}

//			if (this.propertyEnabled.get(MAX_COLOR_TRANSPARENCE) && this.maxColorTransparence!=null){
//				layerGridAggregation.setmax(this.getGridField());
//			}

			if (this.propertyEnabled.get(MIN_COLOR) && this.minColor!=null){
				layerGridAggregation.setMinColor(this.getMinColor());
			}

//			if (this.propertyEnabled.get(MIN_COLOR_TRANSPARENCE) && this.gridField!=null){
//				layerGridAggregation.setWeightField(this.getGridField());
//			}

			if (this.propertyEnabled.get(IS_SHOW_GRID_LABEL) && this.isShowGridLabel!=null){
				layerGridAggregation.setIsShowGridLabel(this.getShowGridLabel());
			}

			if (this.propertyEnabled.get(IS_SHOW_GRID_LABEL) && this.textStyle!=null){
				layerGridAggregation.setGridLabelStyle(this.getTextStyle());
			}

			if (this.propertyEnabled.get(LINE_STYLE) && this.lineStyle!=null){
				layerGridAggregation.setGridLineStyle(this.getLineStyle());
			}
			layerGridAggregation.updateData();
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

				if (layer instanceof LayerGridAggregation) {
					LayerGridAggregation layerGridAggregation = (LayerGridAggregation) layer;
					this.gridField = ComplexPropertyUtilties.union(this.gridField, layerGridAggregation.getWeightField());
					this.layerGridAggregationType = ComplexPropertyUtilties.union(this.layerGridAggregationType, layerGridAggregation.getGridAggregationType());
					this.length = ComplexPropertyUtilties.union(this.length, layerGridAggregation.getGridwidth());
					this.maxColor = ComplexPropertyUtilties.union(this.maxColor, layerGridAggregation.getMaxColor());
					this.maxColorTransparence = ComplexPropertyUtilties.union(this.maxColorTransparence, (int) Math.round((1.0 - this.maxColor.getAlpha() / 255.0) * 100));
					this.minColor = ComplexPropertyUtilties.union(this.minColor, layerGridAggregation.getMinColor());
					this.minColorTransparence = ComplexPropertyUtilties.union(this.minColorTransparence, (int) Math.round((1.0 - this.minColor.getAlpha() / 255.0) * 100));
					this.isShowGridLabel = ComplexPropertyUtilties.union(this.isShowGridLabel, layerGridAggregation.getIsShowGridLabel());
//					if (layerGridAggregation.getGridLabelStyle()==null){
//						System.out.println("textStyle is null");
//					}
//					if (layerGridAggregation.getGridLineStyle()==null){
//						System.out.println("lineStyle is null");
//					}
					this.lineStyle = ComplexPropertyUtilties.union(this.lineStyle, layerGridAggregation.getGridLineStyle());
					this.textStyle = ComplexPropertyUtilties.union(this.textStyle, layerGridAggregation.getGridLabelStyle());
//					this.lineStyle = layerGridAggregation.getGridLineStyle();
//					this.textStyle = layerGridAggregation.getGridLabelStyle();
//					if (this.textStyle==null){
//						System.out.println("this.textStyle is null");
//					}
					DatasetVector datasetVector = (DatasetVector) layer.getDataset();
					this.fieldInfos = datasetVector.getFieldInfos();
//					this.lineStyle=tempGeostyle.clone();
//					this.textStyle=tempTextStyle.clone();
				}
			}
		}
	}

	private void resetProperties() {
		this.gridField = "";
		this.layerGridAggregationType = null;
		this.length = 0;
		//this.colorScheme = null;
		this.maxColor = null;
		this.maxColorTransparence = 0;
		this.minColor = null;
		this.minColorTransparence = 0;
		this.isShowGridLabel = false;
		this.lineStyle = null;
		this.textStyle = null;
		this.fieldInfos = null;
		this.tempGeostyle=null;
		this.tempTextStyle=null;

		if (getLayers() != null && getLayers().length > 0) {
			this.gridField = ((LayerGridAggregation) getLayers()[0]).getWeightField();
			this.layerGridAggregationType = ((LayerGridAggregation) getLayers()[0]).getGridAggregationType();
			this.length = ((LayerGridAggregation) getLayers()[0]).getGridwidth();
			this.maxColor = ((LayerGridAggregation) getLayers()[0]).getMaxColor();
			this.maxColorTransparence = (int) Math.round((1.0 - this.maxColor.getAlpha() / 255.0) * 100);
			this.minColor = ((LayerGridAggregation) getLayers()[0]).getMinColor();
			this.minColorTransparence = (int) Math.round((1.0 - this.minColor.getAlpha() / 255.0) * 100);
			this.isShowGridLabel = ((LayerGridAggregation) getLayers()[0]).getIsShowGridLabel();
			this.lineStyle =((LayerGridAggregation) getLayers()[0]).getGridLineStyle();
			this.textStyle =((LayerGridAggregation) getLayers()[0]).getGridLabelStyle();
//			this.tempGeostyle=((LayerGridAggregation) getLayers()[0]).getGridLineStyle();
//			this.tempTextStyle=((LayerGridAggregation) getLayers()[0]).getGridLabelStyle();
//			if (this.textStyle==null){
//				System.out.println("cishi is null");
//			}else{
//				System.out.println("cishi is not null");
//			}
			DatasetVector datasetVector = (DatasetVector) getLayers()[0].getDataset();
			this.fieldInfos = datasetVector.getFieldInfos();
		}
	}

	private void initializeEnabledMap() {
		this.propertyEnabled.put(GRID_FIELD, true);
		this.propertyEnabled.put(GRID_TYPE, true);
		this.propertyEnabled.put(LENGTH, true);
		this.propertyEnabled.put(COLOR_SCHEME, true);
		this.propertyEnabled.put(MAX_COLOR, true);
		this.propertyEnabled.put(MAX_COLOR_TRANSPARENCE, true);
		this.propertyEnabled.put(MIN_COLOR, true);
		this.propertyEnabled.put(MIN_COLOR_TRANSPARENCE, true);
		this.propertyEnabled.put(IS_SHOW_GRID_LABEL, true);
		this.propertyEnabled.put(LINE_STYLE, true);
		//this.propertyEnabled.put(LINE_WIDTH, true);
	}
}
