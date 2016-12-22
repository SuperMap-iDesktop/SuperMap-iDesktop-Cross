package com.supermap.desktop.CtrlAction.property;

import com.supermap.data.CursorType;
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
import com.supermap.mapping.Layer;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			// 获取当前活动图层对应的数据集
			Layer activeLayer = formMap.getActiveLayers()[0];
			Dataset dataset = activeLayer.getDataset();
			UICommonToolkit.getLayersManager().getLayersTree().getMouseListeners();
			if (null != dataset && dataset instanceof DatasetVector) {
				Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.DYNAMIC);
				BindUtilties.openTabular(dataset, recordset);
                BindUtilties.windowBindProperty(formMap);
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
