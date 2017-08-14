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
 * 属性表删除行
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

				// 当删除操作完成后，高量显示下一条数据-yuanR
				if (formTabular.getRowCount() > 0) {
					int intervalRow = selectedRows[0];
					if (intervalRow >= formTabular.getjTableTabular().getRowCount()) {
						intervalRow = intervalRow - 1;
					}
					formTabular.goToRow(intervalRow);
				}
			}
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormTabular) {
			boolean result = true;

			FormTabular formTabular = (FormTabular) activeForm;

			if (formTabular.getSelectedRow() == -1 || formTabular.getRecordset().isEmpty() || (formTabular.getSelectedRows().length == formTabular.getRowCount() && formTabular.getRowCount() != 1)) {
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
