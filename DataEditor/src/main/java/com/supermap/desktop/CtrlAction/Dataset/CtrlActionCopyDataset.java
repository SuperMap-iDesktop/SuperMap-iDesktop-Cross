package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionCopyDataset extends CtrlAction {

	public static EngineType[] UN_SUPPORT_TYPE = new EngineType[]{EngineType.OGC, EngineType.ISERVERREST,
		EngineType.SUPERMAPCLOUD, EngineType.GOOGLEMAPS, EngineType.BAIDUMAPS, EngineType.OPENSTREETMAPS, EngineType.MAPWORLD};
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
				if (null != datasource && isSupportEngineType(datasource.getEngineType())) {
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
	
	public static boolean isSupportEngineType(EngineType engineType) {
		boolean result = true;

		for (EngineType type : UN_SUPPORT_TYPE) {
			if (engineType == type) {
				result = false;
				break;
			}
		}
		return result;
	}
}
