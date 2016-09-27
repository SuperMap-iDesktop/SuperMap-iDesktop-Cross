package com.supermap.desktop.CtrlAction.Edit;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.implement.CtrlAction;

public class CtrlActionCut extends CtrlAction {

	public CtrlActionCut(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IForm form = Application.getActiveApplication().getActiveForm();
			if (form != null) {
				if (form instanceof IFormMap) {
					((IFormMap) form).getMapControl().cut();
					((IFormMap) form).getMapControl().getMap().refresh();
				} else if (form instanceof IFormScene) {
					// TODO 目前场景并不支持编辑，后续版本根据组件支持情况再行实现
				} else if (form instanceof IFormLayout) {
					((IFormLayout) form).getMapLayoutControl().cut();
					((IFormLayout) form).getMapLayoutControl().getMapLayout().refresh();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
 }

	@Override
	public boolean enable() {
		boolean enable = false;
		try {
			IForm form = Application.getActiveApplication().getActiveForm();
			if (form != null) {
				if (form instanceof IFormMap) {
					form = form;
					((IFormMap) form).getMapControl();
					enable = ((IFormMap) form).getMapControl().canCut();
				} else if (form instanceof IFormScene) {
					// TODO 目前场景并不支持编辑，后续版本根据组件支持情况再行实现
				} else if (form instanceof IFormLayout) {
					enable = ((IFormLayout) form).getMapLayoutControl().canCut();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return enable;
	}
}
