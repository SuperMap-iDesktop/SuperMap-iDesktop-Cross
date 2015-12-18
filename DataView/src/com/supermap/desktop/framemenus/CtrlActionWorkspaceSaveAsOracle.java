package com.supermap.desktop.framemenus;

import javax.swing.JFrame;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.JDialogWorkspaceSaveAs;

public class CtrlActionWorkspaceSaveAsOracle extends CtrlAction {

	public CtrlActionWorkspaceSaveAsOracle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run(){
		JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
		JDialogWorkspaceSaveAs dialog = new JDialogWorkspaceSaveAs(parent, true,JDialogWorkspaceSaveAs.saveAsOracle);
		dialog.showDialog();
	}
	@Override
	public boolean enable() {
		return true;
	}
}
