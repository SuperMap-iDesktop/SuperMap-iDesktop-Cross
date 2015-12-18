package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class CtrlActionLayerCurrentScaleToMaxVisible extends CtrlAction{

	public CtrlActionLayerCurrentScaleToMaxVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run(){
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map currentMap = formMap.getMapControl().getMap();
			double currentScale = currentMap.getScale();
			Layer currentLayer = formMap.getActiveLayers()[0];
			currentLayer.setMaxVisibleScale(currentScale);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
