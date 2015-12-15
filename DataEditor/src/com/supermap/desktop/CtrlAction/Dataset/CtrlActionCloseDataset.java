package com.supermap.desktop.CtrlAction.Dataset;

import javax.swing.JOptionPane;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;

public class CtrlActionCloseDataset extends CtrlAction {

	public CtrlActionCloseDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(DataEditorProperties.getString("String_CloseDatasetMessage"))) {
			CommonToolkit.DatasetWrap.CloseDataset(Application.getActiveApplication().getActiveDatasets());
		}
	}

	@Override
	public boolean enable() {
		boolean result = false;
		if (Application.getActiveApplication().getActiveDatasets().length > 0) {
			result = true;
		}
		return result;
	}
}