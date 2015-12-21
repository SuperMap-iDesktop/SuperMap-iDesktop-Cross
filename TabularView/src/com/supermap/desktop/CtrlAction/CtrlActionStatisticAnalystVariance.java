package com.supermap.desktop.CtrlAction;

import com.supermap.data.StatisticMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.utilties.TabularStatisticUtilties;

public class CtrlActionStatisticAnalystVariance extends CtrlAction{
	public CtrlActionStatisticAnalystVariance(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run(){
		((IFormTabular)Application.getActiveApplication().getActiveForm()).doStatisticAnalust(StatisticMode.VARIANCE, TabularViewProperties.getString("String_StatisticAnalyst_Variance"));
	}
	
	@Override
	public boolean enable(){
		return TabularStatisticUtilties.isStatisticsEnable();
	}
}
