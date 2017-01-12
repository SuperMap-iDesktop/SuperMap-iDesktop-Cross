package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IForm;

import java.util.EventObject;

/**
 * Created by highsad on 2017/1/12.
 */
public class FormActivatedEvent extends EventObject {

	private IForm form;

	/**
	 * @param form
	 */
	public FormActivatedEvent(IForm form) {
		super(form);
		this.form = form;
	}

	public IForm getForm() {
		return form;
	}
}
