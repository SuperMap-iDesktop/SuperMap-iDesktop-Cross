package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DiglogMapOutputToImageDT;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author YuanR
 */
public class CtrlActionMapOutputToImageDT extends CtrlAction {
	public CtrlActionMapOutputToImageDT(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Application.getActiveApplication().getOutput().output("输出为影像数据集");
		DiglogMapOutputToImageDT diglogMapOutputToImageDT = new DiglogMapOutputToImageDT();
		diglogMapOutputToImageDT.showDialog();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
