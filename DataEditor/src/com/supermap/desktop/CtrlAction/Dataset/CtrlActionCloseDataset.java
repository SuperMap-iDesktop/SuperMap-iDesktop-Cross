package com.supermap.desktop.CtrlAction.Dataset;

import javax.swing.JOptionPane;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.DatasetUtilities;

public class CtrlActionCloseDataset extends CtrlAction {

	public CtrlActionCloseDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(DataEditorProperties.getString("String_CloseDatasetMessage"))) {
			DatasetUtilities.closeDataset(Application.getActiveApplication().getActiveDatasets());
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