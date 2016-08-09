package com.supermap.desktop.implement;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;

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
	public void run() {
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
