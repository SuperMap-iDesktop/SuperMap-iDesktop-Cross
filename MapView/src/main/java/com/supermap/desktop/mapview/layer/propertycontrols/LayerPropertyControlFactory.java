package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.mapview.layer.propertymodel.*;

public class LayerPropertyControlFactory {
	private LayerBasePropertyControl basePropertyControl;
	private LayerRelocateDatasetPropertyControl relocateDatasetPropertyControl;
	private LayerVectorParamPropertyControl vectorParamPropertyControl;
	private LayerImageParamPropertyControl imageParamPropertyControl;
	private LayerStretchOptionPropertyControl stretchOptionPropertyControl;
	private LayerGridParamPropertyControl gridParamPropertyControl;

	public AbstractLayerPropertyControl createLayerPropertyControl(LayerPropertyModel model) {
		if (model instanceof LayerBasePropertyModel) {
			if (basePropertyControl == null) {
				basePropertyControl = new LayerBasePropertyControl();
			}
			basePropertyControl.setLayerPropertyModel(model);
			return basePropertyControl;
		} else if (model instanceof LayerRelocateDatasetPropertyModel) {
			if (relocateDatasetPropertyControl == null) {
				relocateDatasetPropertyControl = new LayerRelocateDatasetPropertyControl();
			}
			relocateDatasetPropertyControl.setLayerPropertyModel(model);
			return relocateDatasetPropertyControl;
		} else if (model instanceof LayerVectorParamPropertyModel) {
			if (vectorParamPropertyControl == null) {
				vectorParamPropertyControl = new LayerVectorParamPropertyControl();
			}
			vectorParamPropertyControl.setLayerPropertyModel(model);
			return vectorParamPropertyControl;
		} else if (model instanceof LayerImageParamPropertyModel) {
			if (imageParamPropertyControl == null) {
				imageParamPropertyControl = new LayerImageParamPropertyControl();
			}
			imageParamPropertyControl.setLayerPropertyModel(model);
			return imageParamPropertyControl;
		} else if (model instanceof LayerStretchOptionPropertyModel) {
			if (stretchOptionPropertyControl == null) {
				stretchOptionPropertyControl = new LayerStretchOptionPropertyControl();
			}
			stretchOptionPropertyControl.setLayerPropertyModel(model);
			return stretchOptionPropertyControl;
		} else if (model instanceof LayerGridParamPropertyModel) {
			if (gridParamPropertyControl == null) {
				gridParamPropertyControl = new LayerGridParamPropertyControl();
			}
			gridParamPropertyControl.setLayerPropertyModel(model);
			return gridParamPropertyControl;
		} else {
			return null;
		}
	}
}
