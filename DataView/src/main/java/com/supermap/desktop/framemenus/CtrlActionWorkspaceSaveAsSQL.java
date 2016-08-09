package com.supermap.desktop.framemenus;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.JDialogWorkspaceSaveAs;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;

public class CtrlActionWorkspaceSaveAsSQL extends CtrlAction {

	public CtrlActionWorkspaceSaveAsSQL(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public void run(){
		JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
		JDialogWorkspaceSaveAs dialog = new JDialogWorkspaceSaveAs(parent, true,JDialogWorkspaceSaveAs.saveAsSQL);
		dialog.showDialog();
	}
	
	public boolean enable(){
		return SystemPropertyUtilities.isWindows();
	}
}
