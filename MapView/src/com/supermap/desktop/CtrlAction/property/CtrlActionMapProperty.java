package com.supermap.desktop.CtrlAction.property;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.map.propertycontrols.MapNullException;
import com.supermap.desktop.mapview.map.propertycontrols.MapPropertyContainer;

public class CtrlActionMapProperty extends CtrlAction {

	private static final String MAP_PRPERTY_CONTROL_CLASS = "com.supermap.desktop.mapview.map.propertycontrols.MapPropertyContainer";

	public CtrlActionMapProperty(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		try {
			IDockbar dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
					.get(Class.forName(MAP_PRPERTY_CONTROL_CLASS));

			if (dockbarPropertyContainer != null) {
				MapPropertyContainer container = (MapPropertyContainer) dockbarPropertyContainer.getComponent();
				if(Application.getActiveApplication().getActiveForm() instanceof IFormMap){
					container.setFormMap((IFormMap) Application.getActiveApplication().getActiveForm());
				}
				dockbarPropertyContainer.setVisible(true);
			}
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
