package com.supermap.desktop.mapview.layer.propertymodel;

import java.util.ArrayList;

import com.supermap.desktop.Interface.IFormMap;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.LayerSettingType;
import com.supermap.mapping.Map;

public class LayerPropertyModelFactory {

	private static final int LAYER_NONE = 0;
	private static final int LAYER_UNKNOWN = 1;
	private static final int LAYER_VECTOR = 2;
	private static final int LAYER_GRID = 4;
	private static final int LAYER_IMAGE = 8;
	private static final int LAYERGROUP = 16;

	private LayerPropertyModelFactory() {
		// 工具类，不提供构造方法
	}

	public static LayerPropertyModel[] getLayerPropertyModels(Layer[] layers, IFormMap formMap) {
		int layerType = LAYER_NONE;
		ArrayList<LayerPropertyModel> models = new ArrayList<LayerPropertyModel>();

		for (Layer layer : layers) {
			if (layer instanceof LayerGroup) {
				layerType = LAYERGROUP;
				break;
			} else {
				if (layer != null && layer.getAdditionalSetting() != null) {
					if (layer.getAdditionalSetting().getType() == LayerSettingType.VECTOR) {
						layerType |= LAYER_VECTOR;
					} else if (layer.getAdditionalSetting().getType() == LayerSettingType.GRID) {
						layerType |= LAYER_GRID;
					} else if (layer.getAdditionalSetting().getType() == LayerSettingType.IMAGE) {
						layerType |= LAYER_IMAGE;
					} else {
						layerType |= LAYER_UNKNOWN;
					}
				}
			}
		}

		if (layerType == LAYER_VECTOR) {
			models.add(new LayerBasePropertyModel(layers, formMap));
			models.add(new LayerRelocateDatasetPropertyModel(layers, formMap));
			models.add(new LayerVectorParamPropertyModel(layers, formMap));
		} else if (layerType == LAYER_IMAGE) {
			models.add(new LayerBasePropertyModel(layers, formMap));
			models.add(new LayerRelocateDatasetPropertyModel(layers, formMap));
			models.add(new LayerImageParamPropertyModel(layers, formMap));
			models.add(new LayerStretchOptionPropertyModel(layers, formMap));
		} else if (layerType == LAYER_GRID) {
			models.add(new LayerBasePropertyModel(layers, formMap));
			models.add(new LayerRelocateDatasetPropertyModel(layers, formMap));
			models.add(new LayerGridParamPropertyModel(layers, formMap));
		} else if (layerType == LAYERGROUP) {
			models.add(new LayerBasePropertyModel(layers, formMap));
		} else {
			models.add(new LayerBasePropertyModel(layers, formMap));
			models.add(new LayerRelocateDatasetPropertyModel(layers, formMap));
		}
		return models.toArray(new LayerPropertyModel[models.size()]);
	}
}
