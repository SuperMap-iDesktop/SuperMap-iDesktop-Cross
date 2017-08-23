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
public class CtrlActionGridAggregationProperty extends CtrlAction {
	private static final String LAYER_PRPERTY_CONTROL_CLASS = "com.supermap.desktop.mapview.layer.propertycontrols.LayerPropertyContainer";

	public CtrlActionGridAggregationProperty(IBaseItem caller, IForm formClass) {
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
					if (layer instanceof LayerGridAggregation || layer instanceof LayerHeatmap) {

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
//			System.out.println("maxcolor");
//			System.out.println(maxColor);

//			System.out.println(colorsComboBox.getSelectedItem().getCount());
			LayerGridAggregation layerGridAggregation = ((IFormMap) activeForm).getMapControl().getMap().getLayers().AddGridAggregation(result.getDataset(), maxColor, color);
//			GeoStyle lineStyle=new GeoStyle();
//			lineStyle.setLineColor(Color.WHITE);
//			lineStyle.setLineWidth(0);
//			layerGridAggregation.setGridLineStyle(lineStyle);
//			layerGridAggregation.updateData();
//			if (layerGridAggregation.getGridLineStyle()==null){
//				System.out.println("lineStyle is null");
//			}

//			TextStyle textStyle=new TextStyle();
//			textStyle.setBackColor(Color.BLACK);
//			layerGridAggregation.setGridLabelStyle(textStyle);
//			layerGridAggregation.updateData();
//			if (layerGridAggregation.getGridLabelStyle()==null){
//				System.out.println("textStyle is null");
//			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					UICommonToolkit.getLayersManager().getLayersTree().updateUI();
				}
			});

			if (layerGridAggregation != null) {
				final int selectRow = ((IFormMap) activeForm).getMapControl().getMap().getLayers().indexOf(layerGridAggregation.getName());
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
