package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;

public class CtrlActionClearVisibleScale extends CtrlAction {

	public CtrlActionClearVisibleScale(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Layer currentLayer = formMap.getActiveLayers()[0];
			currentLayer.setMaxVisibleScale(0);
			currentLayer.setMinVisibleScale(0);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

}
