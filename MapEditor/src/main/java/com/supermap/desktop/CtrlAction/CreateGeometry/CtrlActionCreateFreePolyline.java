package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionCreateFreePolyline extends ActionCreateBase {

	public CtrlActionCreateFreePolyline(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action getAction() {
		return Action.CREATE_FREE_POLYLINE;
	}

	@Override
	public boolean isSupportDatasetType(DatasetType datasetType) {
		return DatasetType.LINE == datasetType || DatasetType.LINE3D == datasetType || DatasetType.LINEM == datasetType || DatasetType.CAD == datasetType
				|| DatasetType.NETWORK == datasetType || DatasetType.NETWORK3D == datasetType;
	}
}
