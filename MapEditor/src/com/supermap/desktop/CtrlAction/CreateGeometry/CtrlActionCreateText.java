package com.supermap.desktop.CtrlAction.CreateGeometry;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.ui.Action;

public class CtrlActionCreateText extends ActionCreateBase {

	private CreateTextAction createTextAction = new CreateTextAction();

	public CtrlActionCreateText(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public Action getAction() {
		return Action.CREATETEXT;
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			this.createTextAction.Start(formMap.getMapControl());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean isSupportDatasetType(DatasetType datasetType) {
		return DatasetType.TEXT == datasetType || DatasetType.CAD == datasetType;
	}
}
