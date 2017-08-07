package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormTabular;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.TabularUtilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class CtrlActionTabularRemoveRow extends CtrlAction {
	public CtrlActionTabularRemoveRow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		FormTabular formTabular = (FormTabular) Application.getActiveApplication().getActiveForm();
		int[] selectedRows = formTabular.getSelectedRows();
		if (selectedRows.length > 0) {
			if (UICommonToolkit.showConfirmDialogWithCancel(CoreProperties.getString("String_TabularRemoveRow_Warning")) == JOptionPane.YES_OPTION) {
				formTabular.deleteRows(selectedRows);

				TabularUtilities.refreshTabularDatas(formTabular.getDataset());
			}
		}

	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormTabular) {
			boolean result = true;

			FormTabular formTabular = (FormTabular) activeForm;
			if (formTabular.getSelectedRow() == -1 || formTabular.getRecordset().isEmpty()) {
				result = false;
			}
			Dataset dataset = formTabular.getDataset();
			if (dataset == null || dataset.getDatasource().isReadOnly() || dataset.getType() == DatasetType.LINKTABLE) {
				result = false;
			}
			return result;
		}
		return false;
	}
}
