package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class CtrlActionLayerCurrentScaleToMinVisible extends CtrlAction{

	public CtrlActionLayerCurrentScaleToMinVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run(){
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map currentMap = formMap.getMapControl().getMap();
			double currentMapScale = currentMap.getScale();
			Layer currentLayer = formMap.getActiveLayers()[0];
			currentLayer.setMinVisibleScale(currentMapScale);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
