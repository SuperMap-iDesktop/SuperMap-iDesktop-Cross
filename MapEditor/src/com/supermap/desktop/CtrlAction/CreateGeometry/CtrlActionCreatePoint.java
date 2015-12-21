package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.ui.Action;

public class CtrlActionCreatePoint extends ActionCreateBase {

	public CtrlActionCreatePoint(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action getAction() {
		return Action.CREATEPOINT;
	}

	@Override
	public boolean isSupportDatasetType(DatasetType datasetType) {
		return DatasetType.POINT3D == datasetType || DatasetType.CAD == datasetType || DatasetType.POINT == datasetType;
	}
}
