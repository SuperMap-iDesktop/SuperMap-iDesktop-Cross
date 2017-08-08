package com.supermap.desktop.CtrlAction;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormTabular;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.TabularUtilities;

/**
 * @author XiaJT
 */
public class CtrlActionTabularAddRow extends CtrlAction {

	public CtrlActionTabularAddRow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormTabular && ((FormTabular) activeForm).getDataset().getType() == DatasetType.TABULAR) {
			FormTabular formTabular = (FormTabular) activeForm;
			formTabular.addRow(null);
			TabularUtilities.refreshTabularDatas(formTabular.getDataset());
			formTabular.goToRow(formTabular.getRowCount() - 1);
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormTabular && ((FormTabular) activeForm).getDataset() != null && ((FormTabular) activeForm).getDataset().getType() == DatasetType.TABULAR && !((FormTabular) activeForm).getDataset().isReadOnly()) {
			return true;
		}
		return false;
	}
}
