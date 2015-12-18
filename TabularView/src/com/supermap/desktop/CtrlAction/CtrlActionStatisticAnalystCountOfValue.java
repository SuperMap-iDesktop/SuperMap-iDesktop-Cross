package com.supermap.desktop.CtrlAction;

import java.text.MessageFormat;
import java.util.HashMap;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.utilties.TabularStatisticUtilties;

public class CtrlActionStatisticAnalystCountOfValue extends CtrlAction {
	public CtrlActionStatisticAnalystCountOfValue(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		HashMap hashMap = new HashMap();
		IFormTabular formTabular = (IFormTabular) Application.getActiveApplication().getActiveForm();
		int[] selectRows = formTabular.getSelectedRows();
		int[] selectColumns = formTabular.getSelectedColumns();
		for (int i = 0; i < selectRows.length; i++) {
			for (int j = 0; j < selectColumns.length; j++) {
				hashMap.put(formTabular.getValueAt(selectRows[i], selectColumns[j]), 0);
			}
		}
		TabularStatisticUtilties.updataStatisticsResult(MessageFormat.format(TabularViewProperties.getString("String_Output_ColumnCountOfValue"),
				hashMap.size()));
	}

	@Override
	public boolean enable() {
		int tabularSelectNumberCount = ((IFormTabular) Application.getActiveApplication().getActiveForm()).getSelectColumnCount();
		return tabularSelectNumberCount > 0 ? true : false;
	}
}
