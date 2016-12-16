package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IForm;

import java.util.EventObject;

/**
 * Created by highsad on 2016/11/24.
 */
public class FormClosedEvent extends EventObject {

	private IForm form;

	/**
	 * @param form
	 */
	public FormClosedEvent(IForm form) {
		super(form);
		this.form = form;
	}

	public IForm getForm() {
		return form;
	}
}
