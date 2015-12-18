package com.supermap.desktop.CtrlAction.LayerSetting;

import java.text.MessageFormat;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class CtrlActionLayerCurrentScaleToMaxVisible extends CtrlAction {

	public CtrlActionLayerCurrentScaleToMaxVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map currentMap = formMap.getMapControl().getMap();
			double currentScale = currentMap.getScale();
			for (Layer currentLayer : formMap.getActiveLayers()) {
				if (Double.compare(currentScale, currentLayer.getMinVisibleScale()) >= 0) {
					currentLayer.setMaxVisibleScale(currentScale);
				} else {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_CurrentLayerScaleBiger"), currentLayer.getCaption()));
				}
			}
			currentMap.refresh();
			// 手动触发更改事件
			formMap.setActiveLayers(formMap.getActiveLayers());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
