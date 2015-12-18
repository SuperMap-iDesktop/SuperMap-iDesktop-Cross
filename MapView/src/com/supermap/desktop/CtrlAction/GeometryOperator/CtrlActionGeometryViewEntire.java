package com.supermap.desktop.CtrlAction.GeometryOperator;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

public class CtrlActionGeometryViewEntire extends CtrlAction {

	public CtrlActionGeometryViewEntire(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
			formMap.geometryViewEntire();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					enable = true;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

}
