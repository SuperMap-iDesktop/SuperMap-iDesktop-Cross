package com.supermap.desktop.framemenus;

import javax.swing.JOptionPane;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;

public class CtrlActionCloseCurrentDataset extends CtrlAction {

	public CtrlActionCloseCurrentDataset(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(DataViewProperties.getString("String_CloseDatasetMessage"))) {
			CommonToolkit.DatasetWrap.CloseDataset(Application.getActiveApplication().getActiveDatasets());
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}