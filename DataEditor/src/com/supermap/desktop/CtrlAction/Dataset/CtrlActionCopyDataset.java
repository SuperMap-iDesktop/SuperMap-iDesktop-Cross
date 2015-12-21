package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionCopyDataset extends CtrlAction {

	public CtrlActionCopyDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
		JDialogDatasetCopy datasetCopy = null;
		if (null != datasets) {
			datasetCopy = new JDialogDatasetCopy(datasets);
		} else {
			datasetCopy = new JDialogDatasetCopy();
		}
		datasetCopy.showDialog();
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		Datasource[] datasources = Application.getActiveApplication().getActiveDatasources();
		if (datasources != null && datasources.length > 0) {
			for (Datasource datasource : datasources) {
				if (null != datasource && !datasource.isReadOnly()) {
					enable = true;
					break;
				}
			}
			if (datasources[0].getDatasets().getCount() <= 0) {
				enable = false;
			}
		}
		return enable;
	}
}
