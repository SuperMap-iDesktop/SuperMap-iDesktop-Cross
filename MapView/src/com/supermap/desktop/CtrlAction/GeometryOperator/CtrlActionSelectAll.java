package com.supermap.desktop.CtrlAction.GeometryOperator;

import java.util.ArrayList;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilties.MapViewUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.implement.SmLabel;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.StatusbarManager;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.GeometrySelectedListener;

public class CtrlActionSelectAll extends CtrlAction {
	public CtrlActionSelectAll(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			FormMap formMap = (FormMap) Application.getActiveApplication().getActiveForm();
			formMap.selectAll();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean result = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
		for (Layer layer : layers) {
			if (layer.isVisible() && layer.isSelectable()) {
				result = true;
			}
		}
		return result;
	}
}
