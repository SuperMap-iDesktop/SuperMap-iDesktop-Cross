package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilties.MapViewUtilties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.MapUtilities;
import com.supermap.mapping.Map;

public class CtrlActionDatasetAddToCurrentMap extends CtrlAction {

	public CtrlActionDatasetAddToCurrentMap(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map map = formMap.getMapControl().getMap();

			MapViewUtilties.addDatasetsToMap(map, datasets, true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			if ((Application.getActiveApplication().getActiveForm() instanceof IFormMap) && datasets != null && datasets.length > 0
					&& datasets[0].getType() != DatasetType.TABULAR && datasets[0].getType() != DatasetType.TOPOLOGY) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}

}
