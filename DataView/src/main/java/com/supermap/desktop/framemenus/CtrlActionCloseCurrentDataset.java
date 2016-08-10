package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.DatasetUtilities;

import javax.swing.*;

public class CtrlActionCloseCurrentDataset extends CtrlAction {

	public CtrlActionCloseCurrentDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(DataViewProperties.getString("String_CloseDatasetMessage"))) {
			DatasetUtilities.closeDataset(Application.getActiveApplication().getActiveDatasets());
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}