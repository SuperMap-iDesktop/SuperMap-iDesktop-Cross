package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.DiglogMapOutputToImageDataSet;
import com.supermap.desktop.implement.CtrlAction;

import javax.swing.*;

/**
 * @author YuanR
 */
public class CtrlActionMapOutputToImageDataSet extends CtrlAction {
	public CtrlActionMapOutputToImageDataSet(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		DiglogMapOutputToImageDataSet diglogMapOutputToImageDT = new DiglogMapOutputToImageDataSet((JFrame) Application.getActiveApplication().getMainFrame(), true);
		diglogMapOutputToImageDT.showDialog();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
