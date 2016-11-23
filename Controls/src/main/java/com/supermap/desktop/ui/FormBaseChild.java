package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.ui.controls.DockbarManager;

import javax.swing.*;
import java.awt.*;

public abstract class FormBaseChild extends JPanel implements IForm {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private SmStatusbar statusbar;

	public FormBaseChild(String title, Icon icon, Component component) {
		this.statusbar = createStatusbar();
//		this.getWindowProperties().setMaximizeEnabled(false);
//		this.getWindowProperties().setMinimizeEnabled(false);
//		this.addListener(new DockingWindowAdapter() {
//			@Override
//			public void windowUndocked(DockingWindow window) {
//				if (window != null) {
//					// 在桌面上拖拽/Dock/Undock 窗口之后，设置其父容器的功能性按钮（Close、Dock 等）不可见，仅保留自己的。
//					DockbarManager.setTabWindowProperties(window.getWindowParent());
//				}
//			}
//
//			@Override
//			public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
//				if (addedWindow != null) {
//					// 在桌面上拖拽/Dock/Undock 窗口之后，设置其父容器的功能性按钮（Close、Dock 等）不可见，仅保留自己的。
//					DockbarManager.setTabWindowProperties(addedWindow.getWindowParent());
//				}
//			}
//		});
	}

	@Override
	public String getText() {

		return null;
	}

	@Override
	public void setText(String text) {
		// 默认实现，后续进行初始化操作
	}

	@Override
	public WindowType getWindowType() {
		return WindowType.UNKNOWN;
	}

	@Override
	public boolean isActivated() {
		boolean result = false;
		try {
			IForm childForm = Application.getActiveApplication().getActiveForm();
			if (childForm.equals(this)) {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean save(boolean notify, boolean newWindow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNeedSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean saveFormInfos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actived() {
		// 默认实现,后续进行初始化操作
	}

	@Override
	public void deactived() {
		// 默认实现,后续进行初始化操作
	}

	/**
	 * 窗体被激活时候触发
	 */
	@Override
	public void windowShown() {
		// 默认实现，后续进行初始化操作
	}

	/**
	 * 窗体被隐藏时候触发
	 */
	@Override
	public void windowHidden() {
		// 默认实现，后续进行初始化操作
	}

	public abstract void onClosing();

	private SmStatusbar createStatusbar() {
		SmStatusbar smstatusbar = null;
		Class<?> formClass = this.getClass();
		IFormMain formMain = Application.getActiveApplication().getMainFrame();
		StatusbarManager statusbarManager = (StatusbarManager) formMain.getStatusbarManager();
		if (formClass != null) {
			smstatusbar = statusbarManager.getStatusbar(formClass.getName());
			if (smstatusbar != null) {
//				this.setSouthComponent(smstatusbar);
				smstatusbar.build(this);
			}
		}
		return smstatusbar;
	}

	public SmStatusbar getStatusbar() {
		return statusbar;
	}

	public void setStatusbar(SmStatusbar statusbar) {
		this.statusbar = statusbar;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClosed() {
		return true;
	}

//	@Override
//	public boolean isClosed() {
//		return !isVisible();
//	}

}
