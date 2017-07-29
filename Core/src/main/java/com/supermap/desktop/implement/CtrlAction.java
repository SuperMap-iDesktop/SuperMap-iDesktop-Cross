package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.DesktopRuntimeManager;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.event.DesktopRuntimeEvent;

public abstract class CtrlAction implements ICtrlAction {

	private IForm formClass;
	private IBaseItem caller;

	public CtrlAction(IBaseItem caller, IForm formClass) {
		this.formClass = formClass;
		this.caller = caller;
	}

	@Override
	final public IBaseItem getCaller() {
		return this.caller;
	}

	@Override
	final public void setCaller(IBaseItem caller) {
		this.caller = caller;
	}

	@Override
	final public IForm getFormClass() {
		return this.formClass;
	}

	@Override
	final public void setFormClass(IForm form) {
		this.formClass = form;
	}


	@Override
	public final void doRun() {

		DesktopRuntimeEvent<CtrlAction> desktopRuntimeEvent = new DesktopRuntimeEvent<>();
		desktopRuntimeEvent.setCurrentObject(this);
		desktopRuntimeEvent.setStackTrace(Thread.currentThread().getStackTrace());
		desktopRuntimeEvent.setType(DesktopRuntimeEvent.START);
		DesktopRuntimeManager.getInstance().fireDesktopRuntimeStateListener(desktopRuntimeEvent);

		try {
			run();
		} catch (Exception e) {
			DesktopRuntimeManager.getInstance().fireDesktopRuntimeStateListener(
					new DesktopRuntimeEvent<>(this, Thread.currentThread().getStackTrace(), DesktopRuntimeEvent.CANCLE));
			Application.getActiveApplication().getOutput().output(e);
			return;
		}
		DesktopRuntimeManager.getInstance().fireDesktopRuntimeStateListener(
				new DesktopRuntimeEvent<>(this, Thread.currentThread().getStackTrace(), DesktopRuntimeEvent.STOP));
	}

	protected void run() {

	}

	@Override
	public boolean enable() {
		return true;
	}

	@Override
	public boolean check() {
		return false;
	}

}
