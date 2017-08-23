package com.supermap.desktop.CtrlAction.property;


import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.layer.propertycontrols.LayerPropertyContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGridAggregation;
import com.supermap.mapping.LayerHeatmap;

import javax.swing.*;


/**
 * Created by lixiaoyao on 2017/7/18.
 */
public class CtrlActionHeatMapProperty extends CtrlAction {
	private static final String LAYER_PRPERTY_CONTROL_CLASS = "com.supermap.desktop.mapview.layer.propertycontrols.LayerPropertyContainer";

	public CtrlActionHeatMapProperty(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public boolean enable() {
		boolean result = false;
		try {
			if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
				Layer layer = formMap.getActiveLayers()[0];
				if (layer.getDataset() != null && layer.getDataset().getType() == DatasetType.POINT) {
					if (layer instanceof LayerHeatmap || layer instanceof LayerGridAggregation) {

					} else {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public void run() {
		try {
			Layer result = null;
			IForm activeForm = Application.getActiveApplication().getActiveForm();
			if (activeForm instanceof IFormMap) {
				result = ((IFormMap) activeForm).getActiveLayers()[0];
			}
			ColorsComboBox colorsComboBox = new ColorsComboBox(ControlsProperties.getString("String_ColorSchemeManager_Map_GridAggregation"));

			java.awt.Color color = java.awt.Color.WHITE;
			java.awt.Color maxColor = colorsComboBox.getSelectedItem().get(0);
			LayerHeatmap layerHeatmap = ((IFormMap) activeForm).getMapControl().getMap().getLayers().AddHeatmap(result.getDataset(), 20, maxColor, color);

//			System.out.println(layerHeatmap.getIsUserDef());
//			System.out.println(layerHeatmap.getInternalMaxValue());
//			System.out.println(layerHeatmap.getInternalMinValue());
//			System.out.println(layerHeatmap.getMaxValue());
//			System.out.println(layerHeatmap.getMinValue());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					UICommonToolkit.getLayersManager().getLayersTree().updateUI();
				}
			});
			if (layerHeatmap != null) {
				final int selectRow = ((IFormMap) activeForm).getMapControl().getMap().getLayers().indexOf(layerHeatmap.getName());
				UICommonToolkit.getLayersManager().getLayersTree().setSelectionInterval(selectRow, selectRow);
			}

			IDockbar dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
					.get(Class.forName(LAYER_PRPERTY_CONTROL_CLASS));
			if (dockbarPropertyContainer != null) {
				LayerPropertyContainer container = (LayerPropertyContainer) dockbarPropertyContainer.getInnerComponent();
				if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
					container.setFormMap((IFormMap) Application.getActiveApplication().getActiveForm());
				}
				dockbarPropertyContainer.setVisible(true);
				dockbarPropertyContainer.active();
			}
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
