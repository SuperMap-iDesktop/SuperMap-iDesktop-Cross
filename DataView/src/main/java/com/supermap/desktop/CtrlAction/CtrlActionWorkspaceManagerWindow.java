package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindow;
import com.supermap.desktop.implement.CtrlAction;
import sun.font.TrueTypeFont;

import static com.supermap.desktop.Application.getActiveApplication;

/**
 * @author YuanR
 */

public class CtrlActionWorkspaceManagerWindow extends CtrlAction {
	public CtrlActionWorkspaceManagerWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	/**
	 * 将“窗口”加到小窗体中呈现
	 */
	IForm formWorkspaceManagerWindow;
	IFormManager formManager;

	public void run() {
		try {
			/*
			IForm childForm = Application.getActiveApplication().getActiveForm();
			if (childForm.equals(formWorkspaceManagerWindow)) {
				formWorkspaceManagerWindow=null;
			}
			*/
			formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			if (formWorkspaceManagerWindow == null) {
				//此处存疑：将IForm当做参数传递，目的是：当关闭窗口时，其workspaceManagerWindow不为空
				formWorkspaceManagerWindow = new WorkspaceManagerWindow();
				//通过窗体管理器show出“窗口”到小窗体上
				formManager.showChildForm(formWorkspaceManagerWindow);
			} else {
				//单纯的close窗口，会导致问题
				formManager.close(formWorkspaceManagerWindow);
				formWorkspaceManagerWindow = null;
			}

			// formManager = getActiveApplication().getMainFrame().getFormManager();
			// formManager.showChildForm( new WorkspaceManagerWindow());
			/*
			formManager=	Application.getActiveApplication().getMainFrame().getFormManager();
			formWorkspaceManagerWindow=  new WorkspaceManagerWindow();
			if (	formWorkspaceManagerWindow != null) {
				formManager.setVisible(!workspaceTreeDockbar.isVisible());
			}
			*/
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		//formWorkspaceManagerWindow.windowShown();
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}
}
