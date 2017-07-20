package com.supermap.desktop.CtrlAction.property;

import com.sun.prism.paint.*;
import com.sun.prism.paint.Color;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.layer.propertycontrols.LayerPropertyContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerHeatmap;

import java.awt.*;


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
				if (layer.getDataset() != null && layer.getDataset().getType() == DatasetType.POINT ) {
					if (layer instanceof LayerHeatmap){

					}else {
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

			java.awt.Color color= java.awt.Color.WHITE;
			java.awt.Color maxColor=colorsComboBox.getSelectedItem().get(0);
//			System.out.println(maxColor);
//			System.out.println(colorsComboBox.getSelectedItem().getCount());
			LayerHeatmap layerHeatmap=((IFormMap) activeForm).getMapControl().getMap().getLayers().AddHeatmap(result.getDataset(), 20, maxColor, color);

//			System.out.println("test 组件");
//			System.out.println(layerHeatmap.getMaxColor());
//			maxColor=new java.awt.Color(123,4,45);
//			layerHeatmap.setMaxColor(maxColor);
//			layerHeatmap.updateData();
//			System.out.println("直接设置测试");
//			System.out.println(layerHeatmap.getMaxColor());
//			System.out.println(layerHeatmap.getMaxColor().getRed());
//			System.out.println(layerHeatmap.getMaxColor().getGreen());
//			System.out.println(layerHeatmap.getMaxColor().getBlue());
//			System.out.println("test 空字符串的权重字段");
//			String text=layerHeatmap.getWeightField();
//			if (text.isEmpty()){
//				System.out.println("空字符串");
//			}else{
//				System.out.println("非空");
//			}
//			try {
//				layerHeatmap.setWeightField(text);
//				layerHeatmap.updateData();
//			}catch (Exception e){
//				System.out.println("11111111111111111111111111111");
//				System.out.println(e);
//				System.out.println("11111111111111111111111111111");
//			}
//			System.out.println("test FuzzyDegree");
//			System.out.println(layerHeatmap.getFuzzyDegree());
//			layerHeatmap.setFuzzyDegree(0.5);
//			layerHeatmap.updateData();
//			System.out.println("FuzzyDegree设置为0.5后的结果");
//			System.out.println(layerHeatmap.getFuzzyDegree());
//
//			System.out.println("test Intensity");
//			System.out.println(layerHeatmap.getIntensity());
//			layerHeatmap.setIntensity(0.9);
//			layerHeatmap.updateData();
//			System.out.println("Intensity设置为0.9后的结果");
//			System.out.println(layerHeatmap.getIntensity());
//
//			System.out.println("test maxValue");
//			System.out.println(layerHeatmap.getMaxValue());
//			layerHeatmap.setMaxValue(5);
//			layerHeatmap.updateData();
//			System.out.println("最大值设置为5后的结果");
//			System.out.println(layerHeatmap.getMaxValue());
//
//			System.out.println("test minValue");
//			System.out.println(layerHeatmap.getMinValue());
//			layerHeatmap.setMinValue(2);
//			layerHeatmap.updateData();
//			System.out.println("最小值设置为2后的结果");
//			System.out.println(layerHeatmap.getMinValue());


			UICommonToolkit.getLayersManager().getLayersTree().reload();
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
