package com.supermap.desktop.CtrlAction;

import com.supermap.data.StatisticMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.utilties.TabularStatisticUtilties;

public class CtrlActionStatisticAnalystMin extends CtrlAction{
	public CtrlActionStatisticAnalystMin(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run(){
		((IFormTabular)Application.getActiveApplication().getActiveForm()).doStatisticAnalust(StatisticMode.MIN, TabularViewProperties.getString("String_StatisticAnalyst_Min"));
	}
	
	@Override
	public boolean enable(){
		return TabularStatisticUtilties.isStatisticsEnable();
	}
}
