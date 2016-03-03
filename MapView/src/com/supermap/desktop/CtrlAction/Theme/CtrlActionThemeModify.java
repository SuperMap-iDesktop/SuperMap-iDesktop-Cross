package com.supermap.desktop.CtrlAction.Theme;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.mapping.Layer;

public class CtrlActionThemeModify extends CtrlAction {

	public CtrlActionThemeModify(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		Layer layer = null;
		if (0 < formMap.getActiveLayers().length) {
			layer = formMap.getActiveLayers()[0];
		} else {
			layer = formMap.getMapControl().getMap().getLayers().get(0);
		}
		ThemeGuideFactory.modifyTheme(layer);
		ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
		ThemeGuideFactory.getDockbarThemeContainer().active();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		Layer layer = null;
		if (0 < formMap.getActiveLayers().length) {
			layer = formMap.getActiveLayers()[0];
		} else {
			layer = formMap.getMapControl().getMap().getLayers().get(0);
		}
		if (null != layer && null != layer.getDataset()) {
			enable = true;
		}
		return enable;
	}
}
