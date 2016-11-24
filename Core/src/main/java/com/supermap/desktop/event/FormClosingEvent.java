package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IForm;

import java.util.EventObject;

/**
 * Created by highsad on 2016/11/24.
 */
public class FormClosingEvent extends EventObject {

	private IForm form;
	private boolean isCancel;

	/**
	 * @param form
	 * @param isCancel
	 */
	public FormClosingEvent(IForm form, boolean isCancel) {
		super(form);
		this.form = form;
		this.isCancel = isCancel;
	}

	public IForm getForm() {
		return form;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean cancel) {
		isCancel = cancel;
	}
}
