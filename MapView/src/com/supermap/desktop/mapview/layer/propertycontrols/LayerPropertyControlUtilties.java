package com.supermap.desktop.mapview.layer.propertycontrols;

import java.util.ArrayList;

import javax.swing.JSpinner;

import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.layer.propertymodel.LayerPropertyModel;
import com.supermap.desktop.mapview.layer.propertymodel.LayerPropertyModelFactory;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class LayerPropertyControlUtilties {
	private LayerPropertyControlFactory controlFactory = new LayerPropertyControlFactory();

	public AbstractLayerPropertyControl[] getLayerPropertyControls(Layer[] layers, IFormMap formMap) {
		ArrayList<AbstractLayerPropertyControl> controlList = new ArrayList<AbstractLayerPropertyControl>();
		LayerPropertyModel[] models = LayerPropertyModelFactory.getLayerPropertyModels(layers, formMap);

		if (models != null) {
			for (LayerPropertyModel layerPropertyModel : models) {
				AbstractLayerPropertyControl control = controlFactory.createLayerPropertyControl(layerPropertyModel);
				if (control != null) {
					controlList.add(control);
				}
			}
		}
		return controlList.toArray(new AbstractLayerPropertyControl[controlList.size()]);
	}

	public static void setSpinnerValue(JSpinner spinner, Integer i) {
		if (i == null) {
			spinner.setValue(-1);
		} else {
			spinner.setValue(i);
		}
	}

	public static Integer getSpinnerValue(JSpinner spinner) {
		Integer result = null;

		if ((Integer) spinner.getValue() == DefaultValues.NONE_VALUE) {
			result = null;
		} else {
			result = (Integer) spinner.getValue();
		}
		return result;
	}
}
