package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
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
			ComponentUIUtilities.setName(this.basePropertyControl, "LayerPropertyControlFactory_basePropertyControl");
			return basePropertyControl;
		} else if (model instanceof LayerRelocateDatasetPropertyModel) {
			if (relocateDatasetPropertyControl == null) {
				relocateDatasetPropertyControl = new LayerRelocateDatasetPropertyControl();
			}
			relocateDatasetPropertyControl.setLayerPropertyModel(model);
			ComponentUIUtilities.setName(this.relocateDatasetPropertyControl, "LayerPropertyControlFactory_relocateDatasetPropertyControl");
			return relocateDatasetPropertyControl;
		} else if (model instanceof LayerVectorParamPropertyModel) {
			if (vectorParamPropertyControl == null) {
				vectorParamPropertyControl = new LayerVectorParamPropertyControl();
			}
			vectorParamPropertyControl.setLayerPropertyModel(model);
			ComponentUIUtilities.setName(this.vectorParamPropertyControl, "LayerPropertyControlFactory_vectorParamPropertyControl");
			return vectorParamPropertyControl;
		} else if (model instanceof LayerImageParamPropertyModel) {
			if (imageParamPropertyControl == null) {
				imageParamPropertyControl = new LayerImageParamPropertyControl();
			}
			imageParamPropertyControl.setLayerPropertyModel(model);
			ComponentUIUtilities.setName(this.imageParamPropertyControl, "LayerPropertyControlFactory_imageParamPropertyControl");
			return imageParamPropertyControl;
		} else if (model instanceof LayerStretchOptionPropertyModel) {
			if (stretchOptionPropertyControl == null) {
				stretchOptionPropertyControl = new LayerStretchOptionPropertyControl();
			}
			stretchOptionPropertyControl.setLayerPropertyModel(model);
			ComponentUIUtilities.setName(this.stretchOptionPropertyControl, "LayerPropertyControlFactory_stretchOptionPropertyControl");
			return stretchOptionPropertyControl;
		} else if (model instanceof LayerGridParamPropertyModel) {
			if (gridParamPropertyControl == null) {
				gridParamPropertyControl = new LayerGridParamPropertyControl();
			}
			gridParamPropertyControl.setLayerPropertyModel(model);
			ComponentUIUtilities.setName(this.gridParamPropertyControl, "LayerPropertyControlFactory_gridParamPropertyControl");
			return gridParamPropertyControl;
		} else {
			return null;
		}
	}
}
