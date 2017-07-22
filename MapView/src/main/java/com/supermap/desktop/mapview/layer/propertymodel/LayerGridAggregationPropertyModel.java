package com.supermap.desktop.mapview.layer.propertymodel;

import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGridAggregation;
import com.supermap.mapping.LayerHeatmap;

/**
 * Created by lixiaoyao on 2017/7/21.
 */
public class LayerGridAggregationPropertyModel extends LayerPropertyModel {

	public LayerGridAggregationPropertyModel() {
		// do nothing
		// why do nothing，Can you guess ah
	}

	public LayerGridAggregationPropertyModel(Layer[] layers, IFormMap formMap) {
		super(layers, formMap);
		initializeProperties(layers, formMap);
	}
	private void initializeProperties(Layer[] layers, IFormMap formMap) {
		resetProperties();
		initializeEnabledMap();
	}

	@Override
	public void setProperties(LayerPropertyModel model) {
		LayerGridAggregationPropertyModel layerGridAggregationPropertyModel = (LayerGridAggregationPropertyModel) model;
		if (layerGridAggregationPropertyModel != null) {

		}
	}

	@Override
	public boolean equals(LayerPropertyModel model) {
		LayerGridAggregationPropertyModel layerGridAggregationPropertyModel = (LayerGridAggregationPropertyModel) model;

		return true;
	}

	@Override
	protected void apply(Layer layer) {
		if (layer != null && layer instanceof LayerHeatmap) {
			LayerGridAggregation layerGridAggregation = (LayerGridAggregation) layer;
		}
	}

	private void resetProperties() {
	}

	private void initializeEnabledMap() {
	}
}
