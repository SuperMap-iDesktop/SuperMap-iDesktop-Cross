package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionMapProperty extends CtrlAction {
	private static final String MAP_PRPERTY_CONTROL_CLASS = "com.supermap.desktop.mapview.map.propertycontrols.MapPropertyContainer";

	public CtrlActionMapProperty(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		try {
			IDockbar dockbarPropertyContainer = null;
			for (int i = 0; i < Application.getActiveApplication().getMainFrame().getDockbarManager().getCount(); i++) {
				if (Application.getActiveApplication().getMainFrame().getDockbarManager().get(i).getLabel().equals(DataEditorProperties.getString("String_mapProperty"))) {
					dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(i);
					break;
				}
			}
			if (dockbarPropertyContainer != null && !dockbarPropertyContainer.isVisible()) {
				dockbarPropertyContainer.setVisible(true);
				dockbarPropertyContainer.active();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
