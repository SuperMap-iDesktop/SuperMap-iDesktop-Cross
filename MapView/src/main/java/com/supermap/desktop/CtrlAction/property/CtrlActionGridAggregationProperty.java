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
				if (layer.getDataset() != null && layer.getDataset().getType() == DatasetType.POINT ) {
					if (layer instanceof LayerGridAggregation){

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
			LayerGridAggregation layerGridAggregation=((IFormMap) activeForm).getMapControl().getMap().getLayers().AddGridAggregation(result.getDataset(), maxColor, color);

//			System.out.println("test 组件");
//			System.out.println(layerGridAggregation.getMaxColor());
//			maxColor=new java.awt.Color(123,4,45);
//			layerGridAggregation.setMaxColor(maxColor);
//			layerGridAggregation.updateData();
//			System.out.println("直接设置测试");
//			System.out.println(layerGridAggregation.getMaxColor());
//			System.out.println(layerGridAggregation.getMaxColor().getRed());
//			System.out.println(layerGridAggregation.getMaxColor().getGreen());
//			System.out.println(layerGridAggregation.getMaxColor().getBlue());
//
//
//			System.out.println("test 空字符串的权重字段");
//			String text=layerGridAggregation.getWeightField();
//			if (text.isEmpty()){
//				System.out.println("空字符串");
//			}else{
//				System.out.println("非空");
//			}
//			try {
//				layerGridAggregation.setWeightField(text);
//				layerGridAggregation.updateData();
//			}catch (Exception e){
//				System.out.println("11111111111111111111111111111");
//				System.out.println(e);
//				System.out.println("11111111111111111111111111111");
//			}

//			System.out.println("test height:输出height默认值");
//			System.out.println(layerGridAggregation.getGridHeight());
//			layerGridAggregation.setGridHeight(12);
//			layerGridAggregation.updateData();
//			System.out.println("height设置为12后的结果");
//			System.out.println(layerGridAggregation.getGridHeight());
//
//			System.out.println("test width:输出width默认值");
//			System.out.println(layerGridAggregation.getGridwidth());
//			layerGridAggregation.setGridWidth(12);
//			layerGridAggregation.updateData();
//			System.out.println("width设置为12后的结果");
//			System.out.println(layerGridAggregation.getGridwidth());


			UICommonToolkit.getLayersManager().getLayersTree().reload();
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
