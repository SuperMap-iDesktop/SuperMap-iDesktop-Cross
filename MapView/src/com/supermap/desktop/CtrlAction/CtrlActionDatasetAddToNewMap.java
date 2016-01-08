package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.utilties.MapViewUtilties;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionDatasetAddToNewMap extends CtrlAction {

	public CtrlActionDatasetAddToNewMap(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			MapViewUtilties.addDatasetsToNewWindow(datasets, true);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
			if (datasets != null && datasets.length > 0 && datasets[0].getType() != DatasetType.TABULAR && datasets[0].getType() != DatasetType.TOPOLOGY) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
