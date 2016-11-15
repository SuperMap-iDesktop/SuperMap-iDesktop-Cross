package com.supermap.desktop.CtrlAction.property;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.GeometryPropertyBindWindow.BindUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class CtrlActionGeometryPropertyForSelection extends CtrlAction {

	private Map map;

	public CtrlActionGeometryPropertyForSelection(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			this.map = formMap.getMapControl().getMap();
			TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
			// 获取当前活动图层对应的数据集
			Layer activeLayer = formMap.getActiveLayers()[0];
			Dataset dataset = activeLayer.getDataset();
			UICommonToolkit.getLayersManager().getLayersTree().getMouseListeners();
			if (null != dataset && dataset instanceof DatasetVector && map.findSelection(true).length > 0) {
				Recordset tempRecordset = map.findSelection(true)[0].toRecordset();
				BindUtilties.openTabular(dataset, tempRecordset);
				BindUtilties.windowBindProperty(formMap, tabWindow, activeLayer);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean enable() {
		return null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap;
	}
}
